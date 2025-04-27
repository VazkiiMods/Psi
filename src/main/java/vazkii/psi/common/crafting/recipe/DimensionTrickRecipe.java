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

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import vazkii.psi.api.spell.piece.PieceCraftingTrick;

import javax.annotation.Nullable;

public class DimensionTrickRecipe extends TrickRecipe {
	public static final RecipeSerializer<DimensionTrickRecipe> SERIALIZER = new Serializer();
	private final ResourceKey<Level> dimensionKey;

	public DimensionTrickRecipe(@Nullable PieceCraftingTrick piece, Ingredient input, ItemStack output, ItemStack cad, ResourceKey<Level> dimensionKey) {
		super(piece, input, output, cad);
		this.dimensionKey = dimensionKey;
	}

	public DimensionTrickRecipe(@Nullable PieceCraftingTrick piece, Ingredient input, ItemStack output, ItemStack cad, ResourceLocation dimensionKey) {
		this(piece, input, output, cad, ResourceKey.create(Registries.DIMENSION, dimensionKey));
	}

	@Override
	public boolean matches(SingleRecipeInput inv, Level world) {
		return super.matches(inv, world) && world.dimension() == dimensionKey;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public static class Serializer implements RecipeSerializer<DimensionTrickRecipe> {
		public static final StreamCodec<RegistryFriendlyByteBuf, DimensionTrickRecipe> STREAM_CODEC = StreamCodec.of(
				DimensionTrickRecipe.Serializer::toNetwork, DimensionTrickRecipe.Serializer::fromNetwork
		);
		private static final MapCodec<DimensionTrickRecipe> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
						TrickRecipe.SERIALIZER.codec().forGetter(d -> d),
						ResourceLocation.CODEC.fieldOf("dimensionKey").forGetter(d -> d.dimensionKey.location())
				)
						.apply(instance, (recipe, dimensionId) -> new DimensionTrickRecipe(recipe.getPiece(), recipe.getInput(), recipe.getResultItem(RegistryAccess.EMPTY), recipe.getAssembly(), dimensionId))
		);

		private static DimensionTrickRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
			TrickRecipe recipe = TrickRecipe.SERIALIZER.streamCodec().decode(buf);
			ResourceLocation dimensionId = ResourceLocation.STREAM_CODEC.decode(buf);
			return new DimensionTrickRecipe(recipe.getPiece(), recipe.getInput(), recipe.getResultItem(RegistryAccess.EMPTY), recipe.getAssembly(), dimensionId);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buf, DimensionTrickRecipe recipe) {
			TrickRecipe.SERIALIZER.streamCodec().encode(buf, recipe);
			ResourceLocation.STREAM_CODEC.encode(buf, recipe.dimensionKey.location());
		}

		@Override
		public MapCodec<DimensionTrickRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, DimensionTrickRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}

}
