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
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.ItemSpellDrive;

import javax.annotation.Nonnull;

public class DriveDuplicateRecipe extends CustomRecipe {
	public static final SimpleCraftingRecipeSerializer<DriveDuplicateRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(DriveDuplicateRecipe::new);

	public DriveDuplicateRecipe(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundSource = false;
		boolean foundTarget = false;

		for(int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ItemSpellDrive) {
					if(ItemSpellDrive.getSpell(stack) == null) {
						if(foundTarget) {
							return false;
						}
						foundTarget = true;
					} else {
						if(foundSource) {
							return false;
						}
						foundSource = true;
					}
				} else {
					return false;
				}
			}
		}

		return foundSource && foundTarget;
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingContainer inv, RegistryAccess access) {
		Spell source = null;
		ItemStack target = ItemStack.EMPTY;

		for(int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				Spell spell = ItemSpellDrive.getSpell(stack);
				if(spell != null) {
					source = spell;
				} else {
					target = stack;
				}
			}
		}

		ItemStack copy = target.copy();
		ItemSpellDrive.setSpell(copy, source);
		return copy;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		NonNullList<ItemStack> list = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

		for(int i = 0; i < list.size(); ++i) {
			ItemStack item = inv.getItem(i);
			if(!item.isEmpty() && ItemSpellDrive.getSpell(item) != null) {
				list.set(i, item.copy());
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
