/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import vazkii.psi.common.item.ItemSpellBullet;

public class BulletUpgradeRecipe extends ShapelessRecipe {

	public BulletUpgradeRecipe(String pGroup, CraftingBookCategory pCategory, ItemStack pResult, NonNullList<Ingredient> pIngredients) {
		super(pGroup, pCategory, pResult, pIngredients);
	}

	@Override
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider access) {
		ItemStack output = super.assemble(inv, access);
		for(int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if(stack.getItem() instanceof ItemSpellBullet) {
				output = stack.transmuteCopy(output.getItem());
			}
		}
		return output;
	}
}
