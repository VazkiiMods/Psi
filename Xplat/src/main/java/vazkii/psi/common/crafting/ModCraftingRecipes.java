/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.common.crafting.recipe.*;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.function.Supplier;

public class ModCraftingRecipes {
	public static final RegistryEntry<RecipeType<AssemblyScavengeRecipe>> SCAVENGE_TYPE = type("scavenge", PsiRecipeType::new);
	public static final RegistryEntry<SimpleCraftingRecipeSerializer<AssemblyScavengeRecipe>> SCAVENGE_SERIALIZER = serializer("scavenge", () -> new SimpleCraftingRecipeSerializer<>(AssemblyScavengeRecipe::new));
	public static final RegistryEntry<RecipeType<BulletToDriveRecipe>> BULLET_TO_DRIVE_TYPE = type("bullet_to_drive", PsiRecipeType::new);
	public static final RegistryEntry<SimpleCraftingRecipeSerializer<BulletToDriveRecipe>> BULLET_TO_DRIVE_SERIALIZER = serializer("bullet_to_drive", () -> new SimpleCraftingRecipeSerializer<>(BulletToDriveRecipe::new));
	public static final RegistryEntry<RecipeType<BulletUpgradeRecipe>> BULLET_UPGRADE_TYPE = type("bullet_upgrade", PsiRecipeType::new);
	public static final RegistryEntry<BulletUpgradeRecipe.Serializer> BULLET_UPGRADE_SERIALIZER = serializer("bullet_upgrade", BulletUpgradeRecipe.Serializer::new);
	public static final RegistryEntry<RecipeType<ColorizerChangeRecipe>> COLORIZER_CHANGE_TYPE = type("colorizer_change", PsiRecipeType::new);
	public static final RegistryEntry<SimpleCraftingRecipeSerializer<ColorizerChangeRecipe>> COLORIZER_CHANGE_SERIALIZER = serializer("colorizer_change", () -> new SimpleCraftingRecipeSerializer<>(ColorizerChangeRecipe::new));
	public static final RegistryEntry<RecipeType<DriveDuplicateRecipe>> DRIVE_DUPLICATE_TYPE = type("drive_duplicate", PsiRecipeType::new);
	public static final RegistryEntry<SimpleCraftingRecipeSerializer<DriveDuplicateRecipe>> DRIVE_DUPLICATE_SERIALIZER = serializer("drive_duplicate", () -> new SimpleCraftingRecipeSerializer<>(DriveDuplicateRecipe::new));
	public static final RegistryEntry<RecipeType<SensorAttachRecipe>> SENSOR_ATTACH_TYPE = type("sensor_attach", PsiRecipeType::new);
	public static final RegistryEntry<SimpleCraftingRecipeSerializer<SensorAttachRecipe>> SENSOR_ATTACH_SERIALIZER = serializer("sensor_attach", () -> new SimpleCraftingRecipeSerializer<>(SensorAttachRecipe::new));
	public static final RegistryEntry<RecipeType<SensorRemoveRecipe>> SENSOR_REMOVE_TYPE = type("sensor_remove", PsiRecipeType::new);
	public static final RegistryEntry<SimpleCraftingRecipeSerializer<SensorRemoveRecipe>> SENSOR_REMOVE_SERIALIZER = serializer("sensor_remove", () -> new SimpleCraftingRecipeSerializer<>(SensorRemoveRecipe::new));
	public static final RegistryEntry<PsiTrickRecipeType<ITrickRecipe>> TRICK_RECIPE_TYPE = type("trick_crafting", PsiTrickRecipeType::new);
	public static final RegistryEntry<TrickRecipe.Serializer> TRICK_RECIPE_SERIALIZER = serializer("trick_crafting", TrickRecipe.Serializer::new);
	public static final RegistryEntry<PsiTrickRecipeType<DimensionTrickRecipe>> DIMENSION_TRICK_RECIPE_TYPE = type("dimension_trick_crafting", PsiTrickRecipeType::new);
	public static final RegistryEntry<DimensionTrickRecipe.Serializer> DIMENSION_TRICK_RECIPE_SERIALIZER = serializer("dimension_trick_crafting", DimensionTrickRecipe.Serializer::new);

	private static <T extends RecipeType<?>> RegistryEntry<T> type(String name, Supplier<T> factory) {
		return PsiRegistries.register(BuiltInRegistries.RECIPE_TYPE, PsiAPI.location(name), factory);
	}

	private static <T extends RecipeSerializer<?>> RegistryEntry<T> serializer(String name, Supplier<T> factory) {
		return PsiRegistries.register(BuiltInRegistries.RECIPE_SERIALIZER, PsiAPI.location(name), factory);
	}

	public static void register() {}

	public static class PsiRecipeType<T extends Recipe<?>> implements RecipeType<T> {
		@Override
		public String toString() {
			return BuiltInRegistries.RECIPE_TYPE.getKey(this).toString();
		}
	}

	public static class PsiTrickRecipeType<T extends ITrickRecipe> implements RecipeType<T> {
		@Override
		public String toString() {
			return BuiltInRegistries.RECIPE_TYPE.getKey(this).toString();
		}
	}
}
