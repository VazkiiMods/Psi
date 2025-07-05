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
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.exosuit.ISensorHoldable;
import vazkii.psi.common.crafting.ModCraftingRecipes;

public class SensorRemoveRecipe extends CustomRecipe {
	public SensorRemoveRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
		boolean foundHoldable = false;

		for(int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				if(!foundHoldable && stack.getItem() instanceof ISensorHoldable && !((ISensorHoldable) stack.getItem()).getAttachedSensor(stack).isEmpty()) {
					foundHoldable = true;
				} else {
					return false;
				}
			}
		}

		return foundHoldable;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv, HolderLookup.Provider pRegistries) {
		ItemStack holdableItem = ItemStack.EMPTY;

		for(int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				holdableItem = stack;
			}
		}

		ItemStack copy = holdableItem.copy();
		ISensorHoldable holdable = (ISensorHoldable) holdableItem.getItem();
		holdable.attachSensor(copy, ItemStack.EMPTY);

		return copy;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
		NonNullList<ItemStack> list = NonNullList.withSize(inv.size(), ItemStack.EMPTY);

		for(int i = 0; i < list.size(); ++i) {
			ItemStack item = inv.getItem(i);
			if(item.getItem() instanceof ISensorHoldable) {
				list.set(i, ((ISensorHoldable) item.getItem()).getAttachedSensor(item));
				break;
			}
		}

		return list;
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return ModCraftingRecipes.SENSOR_REMOVE_TYPE.get();
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModCraftingRecipes.SENSOR_REMOVE_SERIALIZER.get();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

}
