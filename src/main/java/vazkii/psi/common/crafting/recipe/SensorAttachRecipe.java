/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [21/02/2016, 16:54:03 (GMT)]
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import vazkii.arl.recipe.ModRecipe;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.ISensorHoldable;

import javax.annotation.Nonnull;

public class SensorAttachRecipe extends ModRecipe {

	public SensorAttachRecipe() {
		super(new ResourceLocation("psi", "sensor_attach"));
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundSensor = false;
		boolean foundTarget = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ISensorHoldable && ((ISensorHoldable) stack.getItem()).getAttachedSensor(stack).isEmpty()) {
					if(foundTarget)
						return false;
					foundTarget = true;
				} else if(stack.getItem() instanceof IExosuitSensor) {
					if(foundSensor)
						return false;
					foundSensor = true;
				} else return false;
			}
		}

		return foundSensor && foundTarget;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack sensor = ItemStack.EMPTY;
		ItemStack target = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof IExosuitSensor)
					sensor = stack;
				else target = stack;
			}
		}

		ItemStack copy = target.copy();
		ISensorHoldable holdable = (ISensorHoldable) copy.getItem();
		holdable.attachSensor(copy, sensor);

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
