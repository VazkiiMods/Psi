/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.item.base.ModItems;

import javax.annotation.Nullable;

import java.util.Optional;

public class TrickRecipe implements ITrickRecipe {
	public static final RecipeSerializer<TrickRecipe> SERIALIZER = new Serializer();

	@Nullable
	protected final PieceCraftingTrick piece;
	protected final Ingredient input;
	protected final ItemStack output;
	protected final ItemStack cad;

	public TrickRecipe(@Nullable PieceCraftingTrick piece, Ingredient input, ItemStack output, ItemStack cad) {
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
	public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
		return output;
	}

	@Override
	public ItemStack getAssembly() {
		return cad;
	}

	@Override
	public boolean matches(SingleRecipeInput inv, Level world) {
		return getInput().test(inv.getItem(0));
	}

	@Override
	public ItemStack assemble(SingleRecipeInput inv, HolderLookup.Provider pRegistries) {
		return getResultItem(pRegistries);
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ModItems.cad);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public RecipeType<?> getType() {
		return ModCraftingRecipes.TRICK_RECIPE_TYPE;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public static class Serializer implements RecipeSerializer<TrickRecipe> {
		public static final StreamCodec<RegistryFriendlyByteBuf, TrickRecipe> STREAM_CODEC = StreamCodec.of(
				TrickRecipe.Serializer::toNetwork, TrickRecipe.Serializer::fromNetwork
		);
		private static final MapCodec<TrickRecipe> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
						Ingredient.CODEC.fieldOf("input").forGetter(d -> d.input),
						ItemStack.CODEC.fieldOf("output").forGetter(d -> d.output),
						ItemStack.CODEC.fieldOf("cad").forGetter(d -> d.cad),
						ResourceLocation.CODEC.optionalFieldOf("piece").forGetter(d -> d.piece != null ? Optional.of(d.piece.registryKey) : Optional.empty())
				)
						.apply(instance, (ingredient, output, cadAssembly, trick) -> new TrickRecipe((PieceCraftingTrick) SpellPiece.create(trick.orElse(null)), ingredient, output, cadAssembly))
		);

		private static TrickRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
			Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
			ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
			ItemStack cadAssembly = ItemStack.STREAM_CODEC.decode(buf);
			PieceCraftingTrick trick = null;
			if(ByteBufCodecs.BOOL.decode(buf)) {
				trick = (PieceCraftingTrick) SpellPiece.create(ResourceLocation.STREAM_CODEC.decode(buf));
			}
			return new TrickRecipe(trick, ingredient, output, cadAssembly);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buf, TrickRecipe recipe) {
			Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input);
			ItemStack.STREAM_CODEC.encode(buf, recipe.output);
			ItemStack.STREAM_CODEC.encode(buf, recipe.cad);
			if(recipe.piece != null) {
				ByteBufCodecs.BOOL.encode(buf, true);
				ResourceLocation.STREAM_CODEC.encode(buf, recipe.piece.registryKey);
			} else {
				ByteBufCodecs.BOOL.encode(buf, false);
			}
		}

		@Override
		public MapCodec<TrickRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, TrickRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}

}
