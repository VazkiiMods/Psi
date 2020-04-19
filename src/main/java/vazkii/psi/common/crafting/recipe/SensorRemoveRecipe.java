/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import vazkii.psi.api.exosuit.ISensorHoldable;

import javax.annotation.Nonnull;

public class SensorRemoveRecipe extends SpecialRecipe {

	public static final SpecialRecipeSerializer<SensorRemoveRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SensorRemoveRecipe::new);

	public SensorRemoveRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundHoldable = false;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (!foundHoldable && stack.getItem() instanceof ISensorHoldable && !((ISensorHoldable) stack.getItem()).getAttachedSensor(stack).isEmpty()) {
					foundHoldable = true;
				} else {
					return false;
				}
			}
		}

		return foundHoldable;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack holdableItem = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				holdableItem = stack;
			}
		}

		ItemStack copy = holdableItem.copy();
		ISensorHoldable holdable = (ISensorHoldable) holdableItem.getItem();
		holdable.attachSensor(copy, ItemStack.EMPTY);

		return copy;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

}
