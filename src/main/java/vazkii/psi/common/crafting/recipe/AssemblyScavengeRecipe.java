/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;

import javax.annotation.Nonnull;

public class AssemblyScavengeRecipe extends CustomRecipe {
	public static final SimpleCraftingRecipeSerializer<AssemblyScavengeRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(AssemblyScavengeRecipe::new);

	public AssemblyScavengeRecipe(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundTarget = false;

		for(int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ICAD) {
					if(foundTarget) {
						return false;
					}

					for(EnumCADComponent comp : EnumCADComponent.class.getEnumConstants()) {
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

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingContainer inv, RegistryAccess access) {
		ItemStack target = ItemStack.EMPTY;

		for(int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				target = stack;
			}
		}

		ItemStack compStack = ((ICAD) target.getItem()).getComponentInSlot(target, EnumCADComponent.ASSEMBLY);
		return compStack.copy();
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
