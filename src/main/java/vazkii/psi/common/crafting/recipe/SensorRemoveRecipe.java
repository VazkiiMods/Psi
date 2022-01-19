/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import vazkii.psi.api.exosuit.ISensorHoldable;

import javax.annotation.Nonnull;

public class SensorRemoveRecipe extends CustomRecipe {

	public static final SimpleRecipeSerializer<SensorRemoveRecipe> SERIALIZER = new SimpleRecipeSerializer<>(SensorRemoveRecipe::new);

	public SensorRemoveRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundHoldable = false;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
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
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		ItemStack holdableItem = ItemStack.EMPTY;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				holdableItem = stack;
			}
		}

		ItemStack copy = holdableItem.copy();
		ISensorHoldable holdable = (ISensorHoldable) holdableItem.getItem();
		holdable.attachSensor(copy, ItemStack.EMPTY);

		return copy;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		NonNullList<ItemStack> list = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

		for (int i = 0; i < list.size(); ++i) {
			ItemStack item = inv.getItem(i);
			if (item.getItem() instanceof ISensorHoldable) {
				list.set(i, ((ISensorHoldable) item.getItem()).getAttachedSensor(item));
				break;
			}
		}

		return list;
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
