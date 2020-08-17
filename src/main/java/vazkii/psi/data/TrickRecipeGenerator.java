/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import com.google.gson.JsonObject;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.Tags;

import vazkii.patchouli.api.PatchouliAPI;
import vazkii.psi.api.recipe.TrickRecipeBuilder;
import vazkii.psi.common.Psi;
import vazkii.psi.common.crafting.recipe.DimensionTrickRecipe;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibPieceNames;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;

import java.util.function.Consumer;

public class TrickRecipeGenerator extends RecipeProvider {
	public TrickRecipeGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		TrickRecipeBuilder.of(ModItems.psidust).input(Tags.Items.DUSTS_REDSTONE).cad(ModItems.cadAssemblyIron).build(consumer);
		TrickRecipeBuilder.of(PatchouliAPI.instance.getBookStack(LibResources.PATCHOULI_BOOK)).input(Items.BOOK).cad(ModItems.cadAssemblyIron).build(consumer);

		TrickRecipeBuilder.of(ModItems.cadAssemblyPsimetal)
				.input(ModItems.cadAssemblyGold)
				.trick(Psi.location(LibPieceNames.TRICK_INFUSION))
				.cad(ModItems.cadAssemblyIron)
				.build(consumer, Psi.location("gold_to_psimetal_assembly_upgrade"));

		TrickRecipeBuilder.of(ModItems.psimetal)
				.input(Tags.Items.INGOTS_GOLD)
				.trick(Psi.location(LibPieceNames.TRICK_INFUSION))
				.cad(ModItems.cadAssemblyIron).build(consumer);

		TrickRecipeBuilder.of(ModItems.psigem)
				.input(Tags.Items.GEMS_DIAMOND)
				.trick(Psi.location(LibPieceNames.TRICK_GREATER_INFUSION))
				.cad(ModItems.cadAssemblyPsimetal).build(consumer);

		TrickRecipeBuilder builder = TrickRecipeBuilder.of(ModItems.ebonySubstance)
				.input(ItemTags.COALS)
				.trick(Psi.location(LibPieceNames.TRICK_EBONY_IVORY))
				.cad(ModItems.cadAssemblyPsimetal);
		dimension(builder, consumer, ModItems.ebonySubstance.getRegistryName(), DimensionType.THE_END_REGISTRY_KEY);

		builder = TrickRecipeBuilder.of(ModItems.ivorySubstance)
				.input(Tags.Items.GEMS_QUARTZ)
				.trick(Psi.location(LibPieceNames.TRICK_EBONY_IVORY))
				.cad(ModItems.cadAssemblyPsimetal);
		dimension(builder, consumer, ModItems.ivorySubstance.getRegistryName(), DimensionType.THE_END_REGISTRY_KEY);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Psi trick crafting recipes";
	}

	public static void dimension(TrickRecipeBuilder builder, Consumer<IFinishedRecipe> parent,
			ResourceLocation id, RegistryKey<DimensionType> type) {
		parent.accept(new DimensionResult(id, builder, type));
	}

	public static class DimensionResult extends TrickRecipeBuilder.Result {
		private final ResourceLocation dimensionId;

		protected DimensionResult(ResourceLocation id, TrickRecipeBuilder builder, RegistryKey<DimensionType> type) {
			super(id, builder);
			this.dimensionId = type.getRegistryName();
		}

		@Override
		public void serialize(@Nonnull JsonObject json) {
			super.serialize(json);
			json.addProperty("dimension", dimensionId.toString());
		}

		@Nonnull
		@Override
		public IRecipeSerializer<?> getSerializer() {
			return DimensionTrickRecipe.SERIALIZER;
		}
	}
}
