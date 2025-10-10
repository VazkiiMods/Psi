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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.common.crafting.recipe.*;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.data.MagicalPsiCondition;

public class ModCraftingRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, LibMisc.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, LibMisc.MOD_ID);

	public static final DeferredHolder<RecipeType<?>, RecipeType<AssemblyScavengeRecipe>> SCAVENGE_TYPE = RECIPE_TYPES.register("scavenge", PsiRecipeType::new);
	public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<AssemblyScavengeRecipe>> SCAVENGE_SERIALIZER = RECIPE_SERIALIZERS.register("scavenge", () -> new SimpleCraftingRecipeSerializer<>(AssemblyScavengeRecipe::new));

	public static final DeferredHolder<RecipeType<?>, RecipeType<BulletToDriveRecipe>> BULLET_TO_DRIVE_TYPE = RECIPE_TYPES.register("bullet_to_drive", PsiRecipeType::new);
	public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<BulletToDriveRecipe>> BULLET_TO_DRIVE_SERIALIZER = RECIPE_SERIALIZERS.register("bullet_to_drive", () -> new SimpleCraftingRecipeSerializer<>(BulletToDriveRecipe::new));

	public static final DeferredHolder<RecipeType<?>, RecipeType<BulletUpgradeRecipe>> BULLET_UPGRADE_TYPE = RECIPE_TYPES.register("bullet_upgrade", PsiRecipeType::new);
	public static final DeferredHolder<RecipeSerializer<?>, BulletUpgradeRecipe.Serializer> BULLET_UPGRADE_SERIALIZER = RECIPE_SERIALIZERS.register("bullet_upgrade", BulletUpgradeRecipe.Serializer::new);

	public static final DeferredHolder<RecipeType<?>, RecipeType<ColorizerChangeRecipe>> COLORIZER_CHANGE_TYPE = RECIPE_TYPES.register("colorizer_change", PsiRecipeType::new);
	public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<ColorizerChangeRecipe>> COLORIZER_CHANGE_SERIALIZER = RECIPE_SERIALIZERS.register("colorizer_change", () -> new SimpleCraftingRecipeSerializer<>(ColorizerChangeRecipe::new));

	public static final DeferredHolder<RecipeType<?>, RecipeType<DriveDuplicateRecipe>> DRIVE_DUPLICATE_TYPE = RECIPE_TYPES.register("drive_duplicate", PsiRecipeType::new);
	public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<DriveDuplicateRecipe>> DRIVE_DUPLICATE_SERIALIZER = RECIPE_SERIALIZERS.register("drive_duplicate", () -> new SimpleCraftingRecipeSerializer<>(DriveDuplicateRecipe::new));

	public static final DeferredHolder<RecipeType<?>, RecipeType<SensorAttachRecipe>> SENSOR_ATTACH_TYPE = RECIPE_TYPES.register("sensor_attach", PsiRecipeType::new);
	public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<SensorAttachRecipe>> SENSOR_ATTACH_SERIALIZER = RECIPE_SERIALIZERS.register("sensor_attach", () -> new SimpleCraftingRecipeSerializer<>(SensorAttachRecipe::new));

	public static final DeferredHolder<RecipeType<?>, RecipeType<SensorRemoveRecipe>> SENSOR_REMOVE_TYPE = RECIPE_TYPES.register("sensor_remove", PsiRecipeType::new);
	public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<SensorRemoveRecipe>> SENSOR_REMOVE_SERIALIZER = RECIPE_SERIALIZERS.register("sensor_remove", () -> new SimpleCraftingRecipeSerializer<>(SensorRemoveRecipe::new));

	public static final DeferredHolder<RecipeType<?>, PsiTrickRecipeType<ITrickRecipe>> TRICK_RECIPE_TYPE = RECIPE_TYPES.register("trick_crafting", PsiTrickRecipeType::new);
	public static final DeferredHolder<RecipeSerializer<?>, TrickRecipe.Serializer> TRICK_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("trick_crafting", TrickRecipe.Serializer::new);

	public static final DeferredHolder<RecipeType<?>, PsiTrickRecipeType<DimensionTrickRecipe>> DIMENSION_TRICK_RECIPE_TYPE = RECIPE_TYPES.register("dimension_trick_crafting", PsiTrickRecipeType::new);
	public static final DeferredHolder<RecipeSerializer<?>, DimensionTrickRecipe.Serializer> DIMENSION_TRICK_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("dimension_trick_crafting", DimensionTrickRecipe.Serializer::new);

	public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, PsiAPI.MOD_ID);
	public static DeferredHolder<MapCodec<? extends ICondition>, MapCodec<MagicalPsiCondition>> MAGICALPSI_CONDITION = CONDITION_CODECS.register("magipsi_enabled", () -> MagicalPsiCondition.CODEC);

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
