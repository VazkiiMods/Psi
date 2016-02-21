/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [21/02/2016, 16:57:14 (GMT)]
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import vazkii.psi.api.exosuit.ISensorHoldable;

public class SensorRemoveRecipe implements IRecipe {
	
	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundHoldable = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(!foundHoldable && stack.getItem() instanceof ISensorHoldable && ((ISensorHoldable) stack.getItem()).getAttachedSensor(stack) != null)
					foundHoldable = true;
				else return false;
			}
		}

		return foundHoldable;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack holdableItem = null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null)
				holdableItem = stack;
		}

		ItemStack copy = holdableItem.copy();
		ISensorHoldable holdable = (ISensorHoldable) holdableItem.getItem();
		holdable.attachSensor(copy, null);
		
		return copy;
	}
	
	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}

}
