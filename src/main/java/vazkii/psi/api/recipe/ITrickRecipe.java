package vazkii.psi.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ITrickRecipe extends IRecipe<RecipeWrapper> {
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
	@Nonnull
	ItemStack getRecipeOutput();

	/**
	 * @return a recommended minimum CAD assembly that can craft this recipe, for JEI display purposes.
	 */
	ItemStack getAssembly();

	@Nonnull
	@Override
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getValue(TYPE_ID).get();
	}

	@Nonnull
	@Override
	default NonNullList<Ingredient> getIngredients() {
		return NonNullList.withSize(1, getInput());
	}
}
