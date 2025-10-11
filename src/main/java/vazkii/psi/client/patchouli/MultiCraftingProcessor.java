/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.patchouli;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.psi.common.Psi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/client/patchouli/processor/MultiCraftingProcessor.java
public class MultiCraftingProcessor implements IComponentProcessor {
	private List<CraftingRecipe> recipes;
	private boolean shapeless = true;
	private int longestIngredientSize = 0;
	private boolean hasCustomHeading;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		if(Minecraft.getInstance().level == null) {
			return;
		}

		List<RecipeHolder<CraftingRecipe>> recipeMap = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
		List<String> names = variables.get("recipes", level.registryAccess()).asStream(level.registryAccess()).map(IVariable::asString).toList();
		this.recipes = new ArrayList<>();
		for(String name : names) {
			Optional<RecipeHolder<CraftingRecipe>> recipe = recipeMap.stream().filter(x -> x.id() == ResourceLocation.parse(name)).findFirst();
			if(recipe.isPresent()) {
				recipes.add(recipe.get().value());
				if(shapeless) {
					shapeless = !(recipe.get().value() instanceof ShapedRecipe);
				}
				for(Ingredient ingredient : recipe.get().value().getIngredients()) {
					int size = ingredient.getItems().length;
					if(longestIngredientSize < size) {
						longestIngredientSize = size;
					}
				}
			} else {
				Psi.logger.warn("Missing crafting recipe {}", name);
			}
		}
		this.hasCustomHeading = variables.has("heading");
	}

	@Override
	public @NotNull IVariable process(Level level, String key) {
		if(recipes.isEmpty()) {
			return null;
		}
		if(key.equals("heading")) {
			if(!hasCustomHeading) {
				return IVariable.from(recipes.getFirst().getResultItem(RegistryAccess.EMPTY).getHoverName(), level.registryAccess());
			}
			return null;
		}
		if(key.startsWith("input")) {
			int index = Integer.parseInt(key.substring(5)) - 1;
			int shapedX = index % 3;
			int shapedY = index / 3;
			List<Ingredient> ingredients = new ArrayList<>();
			for(CraftingRecipe recipe : recipes) {
				if(recipe instanceof ShapedRecipe shaped) {
					if(shaped.getWidth() < shapedX + 1) {
						ingredients.add(Ingredient.EMPTY);
					} else {
						int realIndex = index - (shapedY * (3 - shaped.getWidth()));
						NonNullList<Ingredient> list = recipe.getIngredients();
						ingredients.add(list.size() > realIndex ? list.get(realIndex) : Ingredient.EMPTY);
					}

				} else {
					NonNullList<Ingredient> list = recipe.getIngredients();
					ingredients.add(list.size() > index ? list.get(index) : Ingredient.EMPTY);
				}
			}
			return PatchouliUtils.interweaveIngredients(ingredients, longestIngredientSize, level.registryAccess());
		}
		if(key.equals("output")) {
			return IVariable.wrapList(recipes.stream().map(recipe -> recipe.getResultItem(RegistryAccess.EMPTY)).map(d -> IVariable.from(d, level.registryAccess())).collect(Collectors.toList()), level.registryAccess());
		}
		if(key.equals("shapeless")) {
			return IVariable.wrap(shapeless, level.registryAccess());
		}
		return null;
	}
}
