package vazkii.psi.client.patchouli;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

//https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/client/patchouli/PatchouliUtils.java
public class PatchouliUtils {

	/**
	 * Combines the ingredients, returning the first matching stack of each, then the second stack of each, etc.
	 * looping back ingredients that run out of matched stacks, until the ingredients reach the length
	 * of the longest ingredient in the recipe set.
	 *
	 * @param ingredients           List of ingredients in the specific slot
	 * @param longestIngredientSize Longest ingredient in the entire recipe
	 * @return Serialized Patchouli ingredient string
	 */
	public static String interweaveIngredients(List<Ingredient> ingredients, int longestIngredientSize) {
		if (ingredients.size() == 1) {
			return PatchouliAPI.instance.serializeIngredient(ingredients.get(0));
		}

		ItemStack[] empty = {ItemStack.EMPTY};
		List<ItemStack[]> stacks = new ArrayList<>();
		for (Ingredient ingredient : ingredients) {
			if (ingredient != null && !ingredient.hasNoMatchingItems()) {
				stacks.add(ingredient.getMatchingStacks());
			} else {
				stacks.add(empty);
			}
		}
		StringJoiner joiner = new StringJoiner(",");
		for (int i = 0; i < longestIngredientSize; i++) {
			for (ItemStack[] stack : stacks) {
				joiner.add(PatchouliAPI.instance.serializeItemStack(stack[i % stack.length]));
			}
		}
		return joiner.toString();
	}

	/**
	 * Overload of the method above that uses the provided list's longest ingredient size.
	 */
	public static String interweaveIngredients(List<Ingredient> ingredients) {
		return interweaveIngredients(ingredients, ingredients.stream().mapToInt(ingr -> ingr.getMatchingStacks().length).max().orElse(1));
	}
}