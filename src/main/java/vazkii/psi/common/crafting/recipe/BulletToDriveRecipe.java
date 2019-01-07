/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [14/02/2016, 21:54:35 (GMT)]
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import vazkii.arl.recipe.ModRecipe;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.ItemSpellDrive;

import javax.annotation.Nonnull;

public class BulletToDriveRecipe extends ModRecipe {

	public BulletToDriveRecipe() {
		super(new ResourceLocation("psi", "bullet_to_drive"));
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundSource = false;
		boolean foundTarget = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ISpellContainer && ((ISpellContainer) stack.getItem()).getSpell(stack) != null) {
					if(foundTarget)
						return false;
					foundTarget = true;
				} else if(stack.getItem() instanceof ItemSpellDrive && ItemSpellDrive.getSpell(stack) == null) {
					if(foundSource)
						return false;
					foundSource = true;
				} else return false;
			}
		}

		return foundSource && foundTarget;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		Spell source = null;
		ItemStack target = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ISpellContainer)
					source = ((ISpellContainer) stack.getItem()).getSpell(stack);
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

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}

	@Override
	public boolean canFit(int p_194133_1_, int p_194133_2_) {
		return false;
	}

}
