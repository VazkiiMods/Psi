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

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.crafting.ModCraftingRecipes;

import java.util.EnumSet;

public class AssemblyScavengeRecipe extends CustomRecipe {
	public AssemblyScavengeRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
		boolean foundTarget = false;

		for(int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ICAD) {
					if(foundTarget) {
						return false;
					}

					for(EnumCADComponent comp : EnumSet.allOf(EnumCADComponent.class)) {
						if(comp == EnumCADComponent.ASSEMBLY) {
							continue;
						}

						ItemStack compStack = ((ICAD) stack.getItem()).getComponentInSlot(stack, comp);

						if(!compStack.isEmpty()) {
							return false;
						}
					}

					foundTarget = true;
				} else {
					return false;
				}
			}
		}

		return foundTarget;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv, HolderLookup.Provider access) {
		ItemStack target = ItemStack.EMPTY;

		for(int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				target = stack;
			}
		}

		ItemStack compStack = ((ICAD) target.getItem()).getComponentInSlot(target, EnumCADComponent.ASSEMBLY);
		return compStack.copy();
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return ModCraftingRecipes.SCAVENGE_TYPE.get();
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModCraftingRecipes.SCAVENGE_SERIALIZER.get();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

}
