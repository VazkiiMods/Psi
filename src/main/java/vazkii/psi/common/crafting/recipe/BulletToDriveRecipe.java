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

import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.ItemSpellDrive;

import org.jetbrains.annotations.NotNull;

public class BulletToDriveRecipe extends CustomRecipe {
	public static final SimpleCraftingRecipeSerializer<BulletToDriveRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(BulletToDriveRecipe::new);

	public BulletToDriveRecipe(CraftingBookCategory craftingBookCategory) {
		super(craftingBookCategory);
	}

	@Override
	public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
		boolean foundSource = false;
		boolean foundTarget = false;

		for(int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if(!stack.isEmpty()) {
				if(ISpellAcceptor.hasSpell(stack)) {
					if(foundTarget) {
						return false;
					}
					foundTarget = true;
				} else if(stack.getItem() instanceof ItemSpellDrive && ItemSpellDrive.getSpell(stack) == null) {
					if(foundSource) {
						return false;
					}
					foundSource = true;
				} else {
					return false;
				}
			}
		}

		return foundSource && foundTarget;
	}

	@Override
	public ItemStack assemble(CraftingInput pContainer, HolderLookup.Provider pRegistryAccess) {
		Spell source = null;
		ItemStack target = ItemStack.EMPTY;

		for(int i = 0; i < pContainer.size(); i++) {
			ItemStack stack = pContainer.getItem(i);
			if(!stack.isEmpty()) {
				if(ISpellAcceptor.hasSpell(stack)) {
					source = ISpellAcceptor.acceptor(stack).getSpell();
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
	public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
		NonNullList<ItemStack> list = NonNullList.withSize(inv.size(), ItemStack.EMPTY);

		for(int i = 0; i < list.size(); ++i) {
			ItemStack item = inv.getItem(i);
			if(ISpellAcceptor.hasSpell(item)) {
				list.set(i, item.copy());
				break;
			}
		}

		return list;
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
