/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.common.crafting.recipe.*;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.data.MagicalPsiCondition;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = LibMisc.MOD_ID)
public class ModCraftingRecipes {
	public static final RecipeType<ITrickRecipe> TRICK_RECIPE_TYPE = new PsiRecipeType<>();
	public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, PsiAPI.MOD_ID);
	public static DeferredHolder<MapCodec<? extends ICondition>, MapCodec<MagicalPsiCondition>> MAGICALPSI_CONDITION = CONDITION_CODECS.register("magipsi_enabled", () -> MagicalPsiCondition.CODEC);

	@SubscribeEvent
	public static void registerSerializers(RegisterEvent event) {
		event.register(Registries.RECIPE_SERIALIZER, helper -> {
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "scavenge"), AssemblyScavengeRecipe.SERIALIZER);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "bullet_to_drive"), BulletToDriveRecipe.SERIALIZER);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "bullet_upgrade"), RecipeSerializer.SHAPELESS_RECIPE);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "colorizer_change"), ColorizerChangeRecipe.SERIALIZER);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "drive_duplicate"), DriveDuplicateRecipe.SERIALIZER);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "sensor_attach"), SensorAttachRecipe.SERIALIZER);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "sensor_remove"), SensorRemoveRecipe.SERIALIZER);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "trick_crafting"), TrickRecipe.SERIALIZER);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "dimension_trick_crafting"), DimensionTrickRecipe.SERIALIZER);
		});

		event.register(Registries.RECIPE_TYPE, helper -> {
			helper.register(ITrickRecipe.TYPE_ID, TRICK_RECIPE_TYPE);
		});
	}

	private static class PsiRecipeType<T extends Recipe<?>> implements RecipeType<T> {
		@Override
		public String toString() {
			return BuiltInRegistries.RECIPE_TYPE.getKey(this).toString();
		}
	}
}
