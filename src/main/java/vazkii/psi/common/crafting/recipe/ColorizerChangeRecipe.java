/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.item.ItemCAD;

import javax.annotation.Nonnull;

public class ColorizerChangeRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<ColorizerChangeRecipe> SERIALIZER = new SimpleRecipeSerializer<>(ColorizerChangeRecipe::new);

	public ColorizerChangeRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundColorizer = false;
		boolean foundCAD = false;

		for(int i = 0; i < inv.getContainerSize(); i++) {
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

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		ItemStack colorizer = ItemStack.EMPTY;
		ItemStack cad = ItemStack.EMPTY;

		for(int i = 0; i < inv.getContainerSize(); i++) {
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

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
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
				ret.set(i, ForgeHooks.getContainerItem(stack));
			}
		}
		if(!cad.isEmpty() && dyeIndex != -1) {
			ICAD icad = (ICAD) cad.getItem();
			ret.set(dyeIndex, icad.getComponentInSlot(cad, EnumCADComponent.DYE));
		}

		return ret;
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
