/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.patchouli;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.IVariable;
import vazkii.psi.api.spell.SpellPiece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/client/patchouli/PatchouliUtils.java
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
	public static IVariable interweaveIngredients(List<Ingredient> ingredients, int longestIngredientSize, HolderLookup.Provider registries) {
		if(ingredients.size() == 1) {
			return IVariable.wrapList(Arrays.stream(ingredients.getFirst().getItems()).map(d -> IVariable.from(d, registries)).collect(Collectors.toList()), registries);
		}

		ItemStack[] empty = { ItemStack.EMPTY };
		List<ItemStack[]> stacks = new ArrayList<>();
		for(Ingredient ingredient : ingredients) {
			if(ingredient != null && !ingredient.isEmpty()) {
				stacks.add(ingredient.getItems());
			} else {
				stacks.add(empty);
			}
		}
		List<IVariable> list = new ArrayList<>(stacks.size() * longestIngredientSize);
		for(int i = 0; i < longestIngredientSize; i++) {
			for(ItemStack[] stack : stacks) {
				list.add(IVariable.from(stack[i % stack.length], registries));
			}
		}
		return IVariable.wrapList(list, registries);
	}

	/**
	 * Overload of the method above that uses the provided list's longest ingredient size.
	 */
	public static IVariable interweaveIngredients(List<Ingredient> ingredients, HolderLookup.Provider registries) {
		return interweaveIngredients(ingredients, ingredients.stream().mapToInt(ingr -> ingr.getItems().length).max().orElse(1), registries);
	}

	/**
	 * Sets the tooltip to the passed spell piece's tooltip.
	 */
	public static void setPieceTooltip(IComponentRenderContext context, SpellPiece piece) {
		List<Component> tooltip = new ArrayList<>();
		piece.getTooltip(tooltip);
		context.setHoverTooltipComponents(tooltip);
	}
}
