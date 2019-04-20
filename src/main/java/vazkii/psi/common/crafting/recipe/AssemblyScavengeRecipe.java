/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [05/02/2016, 21:23:39 (GMT)]
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.arl.recipe.ModRecipe;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;

import javax.annotation.Nonnull;

public class AssemblyScavengeRecipe extends ModRecipe {

	public AssemblyScavengeRecipe() {
		super(new ResourceLocation("psi", "scavenge"));
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundTarget = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ICAD) {
					if(foundTarget)
						return false;

					for(EnumCADComponent comp : EnumCADComponent.class.getEnumConstants()) {
						if(comp == EnumCADComponent.ASSEMBLY)
							continue;

						ItemStack compStack = ((ICAD) stack.getItem()).getComponentInSlot(stack, comp);

						if(!compStack.isEmpty())
							return false;
					}

					foundTarget = true;
				} else return false;
			}
		}

		return foundTarget;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack target = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty())
				target = stack;
		}

		ItemStack compStack = ((ICAD) target.getItem()).getComponentInSlot(target, EnumCADComponent.ASSEMBLY);
		return compStack.copy();
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
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
