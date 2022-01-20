/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;

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
	public static void registerSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
		event.getRegistry().registerAll(
				name(AssemblyScavengeRecipe.SERIALIZER, "scavenge"),
				name(BulletToDriveRecipe.SERIALIZER, "bullet_to_drive"),
				name(BulletUpgradeRecipe.SERIALIZER, "bullet_upgrade"),
				name(ColorizerChangeRecipe.SERIALIZER, "colorizer_change"),
				name(DriveDuplicateRecipe.SERIALIZER, "drive_duplicate"),
				name(SensorAttachRecipe.SERIALIZER, "sensor_attach"),
				name(SensorRemoveRecipe.SERIALIZER, "sensor_remove"),

				name(TrickRecipe.SERIALIZER, "trick_crafting"),
				name(DimensionTrickRecipe.SERIALIZER, "dimension_trick_crafting")
		);

		CraftingHelper.register(MagicalPsiCondition.Serializer.INSTANCE);
		Registry.register(Registry.RECIPE_TYPE, ITrickRecipe.TYPE_ID, TRICK_RECIPE_TYPE);
	}

	private static <T extends IForgeRegistryEntry<? extends T>> T name(T entry, String name) {
		return entry.setRegistryName(new ResourceLocation(LibMisc.MOD_ID, name));
	}

	private static class PsiRecipeType<T extends Recipe<?>> implements RecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}
}
