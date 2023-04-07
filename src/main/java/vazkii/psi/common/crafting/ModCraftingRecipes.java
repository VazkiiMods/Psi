/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.common.crafting.recipe.AssemblyScavengeRecipe;
import vazkii.psi.common.crafting.recipe.BulletToDriveRecipe;
import vazkii.psi.common.crafting.recipe.BulletUpgradeRecipe;
import vazkii.psi.common.crafting.recipe.ColorizerChangeRecipe;
import vazkii.psi.common.crafting.recipe.DimensionTrickRecipe;
import vazkii.psi.common.crafting.recipe.DriveDuplicateRecipe;
import vazkii.psi.common.crafting.recipe.SensorAttachRecipe;
import vazkii.psi.common.crafting.recipe.SensorRemoveRecipe;
import vazkii.psi.common.crafting.recipe.TrickRecipe;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.data.MagicalPsiCondition;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = LibMisc.MOD_ID)
public class ModCraftingRecipes {
	public static final RecipeType<ITrickRecipe> TRICK_RECIPE_TYPE = new PsiRecipeType<>();

	@SubscribeEvent
	public static void registerSerializers(RegisterEvent event) {
		event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, helper -> {
			helper.register(new ResourceLocation(LibMisc.MOD_ID, "scavenge"), AssemblyScavengeRecipe.SERIALIZER);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, "bullet_to_drive"), BulletToDriveRecipe.SERIALIZER);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, "bullet_upgrade"), BulletUpgradeRecipe.SERIALIZER);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, "colorizer_change"), ColorizerChangeRecipe.SERIALIZER);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, "drive_duplicate"), DriveDuplicateRecipe.SERIALIZER);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, "sensor_attach"), SensorAttachRecipe.SERIALIZER);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, "sensor_remove"), SensorRemoveRecipe.SERIALIZER);

			helper.register(new ResourceLocation(LibMisc.MOD_ID, "trick_crafting"), TrickRecipe.SERIALIZER);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, "dimension_trick_crafting"), DimensionTrickRecipe.SERIALIZER);

			CraftingHelper.register(MagicalPsiCondition.Serializer.INSTANCE);
		});

		event.register(ForgeRegistries.Keys.RECIPE_TYPES, helper -> {
			helper.register(ITrickRecipe.TYPE_ID, TRICK_RECIPE_TYPE);
		});
	}

	private static class PsiRecipeType<T extends Recipe<?>> implements RecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}
}
