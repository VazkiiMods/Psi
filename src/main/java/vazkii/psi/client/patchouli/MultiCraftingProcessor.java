/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.patchouli;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;

import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.psi.common.Psi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/client/patchouli/processor/MultiCraftingProcessor.java
public class MultiCraftingProcessor implements IComponentProcessor {
	private List<ICraftingRecipe> recipes;
	private boolean shapeless = true;
	private int longestIngredientSize = 0;
	private boolean hasCustomHeading;

	@Override
	public void setup(IVariableProvider variables) {
		Map<ResourceLocation, IRecipe<CraftingInventory>> recipeMap = Minecraft.getInstance().level.getRecipeManager().byType(IRecipeType.CRAFTING);
		List<String> names = variables.get("recipes").asStream().map(IVariable::asString).collect(Collectors.toList());
		this.recipes = new ArrayList<>();
		for (String name : names) {
			IRecipe<?> recipe = recipeMap.get(new ResourceLocation(name));
			if (recipe != null) {
				recipes.add((ICraftingRecipe) recipe);
				if (shapeless) {
					shapeless = !(recipe instanceof IShapedRecipe);
				}
				for (Ingredient ingredient : recipe.getIngredients()) {
					int size = ingredient.getItems().length;
					if (longestIngredientSize < size) {
						longestIngredientSize = size;
					}
				}
			} else {
				Psi.logger.warn("Missing crafting recipe " + name);
			}
		}
		this.hasCustomHeading = variables.has("heading");
	}

	@Override
	public IVariable process(String key) {
		if (recipes.isEmpty()) {
			return null;
		}
		if (key.equals("heading")) {
			if (!hasCustomHeading) {
				return IVariable.from(recipes.get(0).getResultItem().getHoverName());
			}
			return null;
		}
		if (key.startsWith("input")) {
			int index = Integer.parseInt(key.substring(5)) - 1;
			int shapedX = index % 3;
			int shapedY = index / 3;
			List<Ingredient> ingredients = new ArrayList<>();
			for (ICraftingRecipe recipe : recipes) {
				if (recipe instanceof IShapedRecipe) {
					IShapedRecipe<?> shaped = (IShapedRecipe<?>) recipe;
					if (shaped.getRecipeWidth() < shapedX + 1) {
						ingredients.add(Ingredient.EMPTY);
					} else {
						int realIndex = index - (shapedY * (3 - shaped.getRecipeWidth()));
						NonNullList<Ingredient> list = recipe.getIngredients();
						ingredients.add(list.size() > realIndex ? list.get(realIndex) : Ingredient.EMPTY);
					}

				} else {
					NonNullList<Ingredient> list = recipe.getIngredients();
					ingredients.add(list.size() > index ? list.get(index) : Ingredient.EMPTY);
				}
			}
			return PatchouliUtils.interweaveIngredients(ingredients, longestIngredientSize);
		}
		if (key.equals("output")) {
			return IVariable.wrapList(recipes.stream().map(ICraftingRecipe::getResultItem).map(IVariable::from).collect(Collectors.toList()));
		}
		if (key.equals("shapeless")) {
			return IVariable.wrap(shapeless);
		}
		return null;
	}
}
