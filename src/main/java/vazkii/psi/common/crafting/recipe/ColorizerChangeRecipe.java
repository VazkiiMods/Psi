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
import net.neoforged.neoforge.common.CommonHooks;

import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.item.ItemCAD;

import org.jetbrains.annotations.NotNull;

public class ColorizerChangeRecipe extends CustomRecipe {
	public static final SimpleCraftingRecipeSerializer<ColorizerChangeRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(ColorizerChangeRecipe::new);

	public ColorizerChangeRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
		boolean foundColorizer = false;
		boolean foundCAD = false;

		for(int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ICAD) {
					if(foundCAD) {
						return false;
					}
					foundCAD = true;
				} else if(stack.getItem() instanceof ICADColorizer) {
					if(foundColorizer) {
						return false;
					}
					foundColorizer = true;
				} else {
					return false;
				}
			}
		}

		return foundColorizer && foundCAD;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv, HolderLookup.Provider access) {
		ItemStack colorizer = ItemStack.EMPTY;
		ItemStack cad = ItemStack.EMPTY;

		for(int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ICADColorizer) {
					colorizer = stack;
				} else {
					cad = stack;
				}
			}
		}

		if(cad.isEmpty() || colorizer.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack copy = cad.copy();
		ItemCAD.setComponent(copy, colorizer);

		return copy;
	}

	@NotNull
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.size(), ItemStack.EMPTY);
		int dyeIndex = -1;
		ItemStack cad = ItemStack.EMPTY;
		for(int i = 0; i < ret.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty() && stack.getItem() instanceof ICAD) {
				cad = stack;
			} else {
				if(!stack.isEmpty() && stack.getItem() instanceof ICADColorizer) {
					dyeIndex = i;
				}
				ret.set(i, CommonHooks.getCraftingRemainingItem(stack));
			}
		}
		if(!cad.isEmpty() && dyeIndex != -1) {
			ICAD icad = (ICAD) cad.getItem();
			ret.set(dyeIndex, icad.getComponentInSlot(cad, EnumCADComponent.DYE));
		}

		return ret;
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

}
