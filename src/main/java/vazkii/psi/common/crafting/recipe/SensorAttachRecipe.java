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

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.ISensorHoldable;

import javax.annotation.Nonnull;

public class SensorAttachRecipe extends SpecialRecipe {
	public static final SpecialRecipeSerializer<SensorAttachRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SensorAttachRecipe::new);

	public SensorAttachRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundSensor = false;
		boolean foundTarget = false;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ISensorHoldable && ((ISensorHoldable) stack.getItem()).getAttachedSensor(stack).isEmpty()) {
					if (foundTarget)
						return false;
					foundTarget = true;
				} else if (stack.getItem() instanceof IExosuitSensor) {
					if (foundSensor)
						return false;
					foundSensor = true;
				} else return false;
			}
		}

		return foundSensor && foundTarget;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack sensor = ItemStack.EMPTY;
		ItemStack target = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof IExosuitSensor)
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
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

}
