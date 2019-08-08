/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [31/01/2016, 18:46:31 (GMT)]
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.arl.recipe.ModRecipe;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.ItemSpellDrive;

import javax.annotation.Nonnull;

public class DriveDuplicateRecipe extends ModRecipe {

	public DriveDuplicateRecipe() {
		super(new ResourceLocation("psi", "drive_duplicate"));
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory var1, @Nonnull World var2) {
		boolean foundSource = false;
		boolean foundTarget = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if( stack.getItem() instanceof ItemSpellDrive) {
					if(ItemSpellDrive.getSpell(stack) == null) {
						if(foundTarget)
							return false;
						foundTarget = true;
					} else {
						if(foundSource)
							return false;
						foundSource = true;
					}
				} else return false;
			}
		}

		return foundSource && foundTarget;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory var1) {
		Spell source = null;
		ItemStack target = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				Spell spell = ItemSpellDrive.getSpell(stack);
				if(spell != null)
					source = spell;
				else target = stack;
			}
		}

		ItemStack copy = target.copy();
		ItemSpellDrive.setSpell(copy, source);
		return copy;
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
