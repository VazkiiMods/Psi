/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.ISensorHoldable;

import javax.annotation.Nonnull;

public class SensorAttachRecipe extends CustomRecipe {
	public static final SimpleCraftingRecipeSerializer<SensorAttachRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(SensorAttachRecipe::new);

	public SensorAttachRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(@Nonnull CraftingInput inv, @Nonnull Level world) {
		boolean foundSensor = false;
		boolean foundTarget = false;

		for(int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ISensorHoldable && ((ISensorHoldable) stack.getItem()).getAttachedSensor(stack).isEmpty()) {
					if(foundTarget) {
						return false;
					}
					foundTarget = true;
				} else if(stack.getItem() instanceof IExosuitSensor) {
					if(foundSensor) {
						return false;
					}
					foundSensor = true;
				} else {
					return false;
				}
			}
		}

		return foundSensor && foundTarget;
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingInput inv, HolderLookup.Provider pRegistries) {
		ItemStack sensor = ItemStack.EMPTY;
		ItemStack target = ItemStack.EMPTY;

		for(int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof IExosuitSensor) {
					sensor = stack;
				} else {
					target = stack;
				}
			}
		}

		ItemStack copy = target.copy();
		ISensorHoldable holdable = (ISensorHoldable) copy.getItem();
		holdable.attachSensor(copy, sensor);

		return copy;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}
}
