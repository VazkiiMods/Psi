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

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import vazkii.psi.api.spell.piece.PieceCraftingTrick;

import javax.annotation.Nullable;

public class DimensionTrickRecipe extends TrickRecipe {
	public static final RecipeSerializer<DimensionTrickRecipe> SERIALIZER = new Serializer();
	private final ResourceKey<Level> dimensionKey;

	public DimensionTrickRecipe(ResourceLocation id, @Nullable PieceCraftingTrick piece, Ingredient input, ItemStack output, ItemStack cad, ResourceKey<Level> dimensionKey) {
		super(id, piece, input, output, cad);
		this.dimensionKey = dimensionKey;
	}

	public DimensionTrickRecipe(ResourceLocation id, @Nullable PieceCraftingTrick piece, Ingredient input, ItemStack output, ItemStack cad, ResourceLocation dimensionKey) {
		this(id, piece, input, output, cad, ResourceKey.create(Registry.DIMENSION_REGISTRY, dimensionKey));
	}

	@Override
	public boolean matches(RecipeWrapper inv, Level world) {
		return super.matches(inv, world) && world.dimension() == dimensionKey;
	}

	public static class Serializer implements RecipeSerializer<DimensionTrickRecipe> {
		@Override
		public DimensionTrickRecipe fromJson(ResourceLocation id, JsonObject json) {
			TrickRecipe recipe = TrickRecipe.SERIALIZER.fromJson(id, json);
			ResourceLocation dimensionId = new ResourceLocation(GsonHelper.getAsString(json, "dimension"));
			return new DimensionTrickRecipe(id, recipe.getPiece(), recipe.getInput(), recipe.getResultItem(), recipe.getAssembly(), dimensionId);
		}

		@Nullable
		@Override
		public DimensionTrickRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			TrickRecipe recipe = TrickRecipe.SERIALIZER.fromNetwork(id, buf);
			ResourceLocation dimensionId = buf.readResourceLocation();
			return new DimensionTrickRecipe(id, recipe.getPiece(), recipe.getInput(), recipe.getResultItem(), recipe.getAssembly(), dimensionId);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, DimensionTrickRecipe recipe) {
			TrickRecipe.SERIALIZER.toNetwork(buf, recipe);
			buf.writeResourceLocation(recipe.dimensionKey.location());
		}
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

}
