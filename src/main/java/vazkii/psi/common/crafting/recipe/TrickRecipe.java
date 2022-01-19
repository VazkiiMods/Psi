/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting.recipe;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.item.base.ModItems;

import javax.annotation.Nullable;

public class TrickRecipe implements ITrickRecipe {
	public static final IRecipeSerializer<TrickRecipe> SERIALIZER = new Serializer();
	private static final Spell dummySpell = new Spell();

	@Nullable
	private final PieceCraftingTrick piece;
	private final Ingredient input;
	private final ItemStack output;
	private final ItemStack cad;
	private final ResourceLocation id;

	public TrickRecipe(ResourceLocation id, @Nullable PieceCraftingTrick piece, Ingredient input, ItemStack output, ItemStack cad) {
		this.id = id;
		this.piece = piece;
		this.input = input;
		this.output = output;
		this.cad = cad;
	}

	@Nullable
	@Override
	public PieceCraftingTrick getPiece() {
		return piece;
	}

	@Override
	public Ingredient getInput() {
		return input;
	}

	@Override
	public ItemStack getResultItem() {
		return output;
	}

	@Override
	public ItemStack getAssembly() {
		return cad;
	}

	@Override
	public boolean matches(RecipeWrapper inv, World world) {
		return getInput().test(inv.getItem(0));
	}

	@Override
	public ItemStack assemble(RecipeWrapper inv) {
		return getResultItem();
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ModItems.cad);
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public IRecipeType<?> getType() {
		return ModCraftingRecipes.TRICK_RECIPE_TYPE;
	}

	static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TrickRecipe> {
		@Override
		public TrickRecipe fromJson(ResourceLocation id, JsonObject json) {
			Ingredient ingredient = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "input"));
			ItemStack output = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "output"), true);
			ItemStack cadAssembly = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "cad"), true);

			PieceCraftingTrick trick = null;
			if (json.has("trick")) {
				trick = PsiAPI.getSpellPieceRegistry().getOptional(new ResourceLocation(JSONUtils.getAsString(json, "trick")))
						.filter(PieceCraftingTrick.class::isAssignableFrom)
						.map(clazz -> (PieceCraftingTrick) SpellPiece.create(clazz, dummySpell))
						.orElse(null);
			}
			return new TrickRecipe(id, trick, ingredient, output, cadAssembly);
		}

		@Nullable
		@Override
		public TrickRecipe fromNetwork(ResourceLocation id, PacketBuffer buf) {
			Ingredient ingredient = Ingredient.fromNetwork(buf);
			ItemStack output = buf.readItem();
			ItemStack cadAssembly = buf.readItem();
			PieceCraftingTrick trick = null;
			if (buf.readBoolean()) {
				trick = PsiAPI.getSpellPieceRegistry().getOptional(buf.readResourceLocation())
						.map(clazz -> (PieceCraftingTrick) SpellPiece.create(clazz, dummySpell))
						.orElse(null);
			}
			return new TrickRecipe(id, trick, ingredient, output, cadAssembly);
		}

		@Override
		public void toNetwork(PacketBuffer buf, TrickRecipe recipe) {
			recipe.input.toNetwork(buf);
			buf.writeItem(recipe.output);
			buf.writeItem(recipe.cad);
			if (recipe.piece != null) {
				buf.writeBoolean(true);
				buf.writeResourceLocation(recipe.piece.registryKey);
			} else {
				buf.writeBoolean(false);
			}
		}
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

}
