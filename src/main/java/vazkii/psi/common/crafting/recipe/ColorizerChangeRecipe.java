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

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import vazkii.arl.recipe.ModRecipe;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.item.ItemCAD;

import javax.annotation.Nonnull;

public class ColorizerChangeRecipe extends ModRecipe {

	public ColorizerChangeRecipe() {
		super(new ResourceLocation("psi", "colorizer_change"));
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory var1, @Nonnull World var2) {
		boolean foundColorizer = false;
		boolean foundCAD = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
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

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory var1) {
		ItemStack colorizer = ItemStack.EMPTY;
		ItemStack cad = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ICADColorizer)
					colorizer = stack;
				else cad = stack;
			}
		}

		if(cad.isEmpty() || colorizer.isEmpty()) 
			return ItemStack.EMPTY;

		ItemStack copy = cad.copy();
		ItemCAD.setComponent(copy, colorizer);

		return copy;
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		int dyeIndex = -1;
		ItemStack cad = ItemStack.EMPTY;
		for (int i = 0; i < ret.size(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ICAD) {
				cad = stack;
			} else {
				if (!stack.isEmpty() && stack.getItem() instanceof ICADColorizer)
					dyeIndex = i;
				ret.set(i, ForgeHooks.getContainerItem(stack));
			}
		}
		if (!cad.isEmpty() && dyeIndex != -1) {
			ICAD icad = (ICAD) cad.getItem();
			ret.set(dyeIndex, icad.getComponentInSlot(cad, EnumCADComponent.DYE));
		}

		return ret;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

}
