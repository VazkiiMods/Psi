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

import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;

import javax.annotation.Nonnull;

public class AssemblyScavengeRecipe extends SpecialRecipe {
	public static final SpecialRecipeSerializer<AssemblyScavengeRecipe> SERIALIZER = new SpecialRecipeSerializer<>(AssemblyScavengeRecipe::new);

	public AssemblyScavengeRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundTarget = false;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ICAD) {
					if (foundTarget) {
						return false;
					}

					for (EnumCADComponent comp : EnumCADComponent.class.getEnumConstants()) {
						if (comp == EnumCADComponent.ASSEMBLY) {
							continue;
						}

						ItemStack compStack = ((ICAD) stack.getItem()).getComponentInSlot(stack, comp);

						if (!compStack.isEmpty()) {
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

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack target = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				target = stack;
			}
		}

		ItemStack compStack = ((ICAD) target.getItem()).getComponentInSlot(target, EnumCADComponent.ASSEMBLY);
		return compStack.copy();
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
