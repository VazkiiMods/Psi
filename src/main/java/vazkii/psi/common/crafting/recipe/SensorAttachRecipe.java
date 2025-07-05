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
import net.neoforged.neoforge.data.loading.DatagenModLoader;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.ISensorHoldable;
import vazkii.psi.common.crafting.ModCraftingRecipes;

public class SensorAttachRecipe extends CustomRecipe {
	public SensorAttachRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
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

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv, HolderLookup.Provider pRegistries) {
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

	@Override
	public @NotNull RecipeType<?> getType() {
		return !DatagenModLoader.isRunningDataGen() ? RecipeType.CRAFTING : ModCraftingRecipes.SENSOR_ATTACH_TYPE.get();
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModCraftingRecipes.SENSOR_ATTACH_SERIALIZER.get();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

}
