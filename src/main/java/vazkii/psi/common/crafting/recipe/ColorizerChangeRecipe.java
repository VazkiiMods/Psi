/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [28/02/2016, 12:17:15 (GMT)]
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.item.ItemCAD;

public class ColorizerChangeRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundColorizer = false;
		boolean foundCAD = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() instanceof ICAD) {
					if(foundCAD)
						return false;
					foundCAD = true;
				} else if(stack.getItem() instanceof ICADColorizer) {
					if(foundColorizer)
						return false;
					foundColorizer = true;
				} else return false;
			}
		}

		return foundColorizer && foundCAD;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack colorizer = null;
		ItemStack cad = null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() instanceof ICADColorizer)
					colorizer = stack;
				else cad = stack;
			}
		}

		if (cad == null || colorizer == null) return null;

		ItemStack copy = cad.copy();
		ItemCAD.setComponent(copy, colorizer);

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
		ItemStack[] ret = new ItemStack[inv.getSizeInventory()];
		int dyeIndex = -1;
		ItemStack cad = null;
		for (int i = 0; i < ret.length; i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null && stack.getItem() instanceof ICAD) {
				cad = stack;
			} else {
				if (stack != null && stack.getItem() instanceof ICADColorizer)
					dyeIndex = i;
				ret[i] = ForgeHooks.getContainerItem(stack);
			}
		}
		if (cad != null && dyeIndex != -1) {
			ICAD icad = (ICAD) cad.getItem();
			ret[dyeIndex] = icad.getComponentInSlot(cad, EnumCADComponent.DYE);
		}

		return ret;
	}

}
