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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import vazkii.arl.recipe.ModRecipe;
import vazkii.psi.api.exosuit.ISensorHoldable;

import javax.annotation.Nonnull;

public class SensorRemoveRecipe extends ModRecipe {

	public SensorRemoveRecipe() {
		super(new ResourceLocation("psi", "sensor_remove"));
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundHoldable = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(!foundHoldable && stack.getItem() instanceof ISensorHoldable && !((ISensorHoldable) stack.getItem()).getAttachedSensor(stack).isEmpty())
					foundHoldable = true;
				else return false;
			}
		}

		return foundHoldable;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack holdableItem = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty())
				holdableItem = stack;
		}

		ItemStack copy = holdableItem.copy();
		ISensorHoldable holdable = (ISensorHoldable) holdableItem.getItem();
		holdable.attachSensor(copy, ItemStack.EMPTY);

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
