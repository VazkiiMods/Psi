/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ITrickRecipe extends Recipe<RecipeWrapper> {
	/** ID of the trick recipe type and the base recipe serializer. */
	ResourceLocation TYPE_ID = new ResourceLocation(PsiAPI.MOD_ID, "trick_crafting");

	/**
	 * Returns the piece that can craft this recipe.
	 * Certain tricks act as other crafting tricks,
	 * eg. Greater Infusion can craft everything Infusion can.
	 * Note that only tricks capable of crafting will work.
	 *
	 * @return trick crafting this recipe, or null if empty CAD (and Infusion tricks) can craft
	 */
	@Nullable
	PieceCraftingTrick getPiece();

	@Nonnull
	Ingredient getInput();

	@Override
	ItemStack getResultItem(RegistryAccess pRegistryAccess);

	/**
	 * @return a recommended minimum CAD assembly that can craft this recipe, for JEI display purposes.
	 */
	ItemStack getAssembly();

	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return ForgeRegistries.RECIPE_TYPES.getValue(TYPE_ID);
	}

	@Nonnull
	@Override
	default NonNullList<Ingredient> getIngredients() {
		return NonNullList.withSize(1, getInput());
	}
}
