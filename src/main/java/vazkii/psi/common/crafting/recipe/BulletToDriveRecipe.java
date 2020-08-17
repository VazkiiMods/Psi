/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
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

import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.ItemSpellDrive;

import javax.annotation.Nonnull;

public class BulletToDriveRecipe extends SpecialRecipe {
	public static final SpecialRecipeSerializer<BulletToDriveRecipe> SERIALIZER = new SpecialRecipeSerializer<>(BulletToDriveRecipe::new);

	public BulletToDriveRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundSource = false;
		boolean foundTarget = false;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (ISpellAcceptor.hasSpell(stack)) {
					if (foundTarget) {
						return false;
					}
					foundTarget = true;
				} else if (stack.getItem() instanceof ItemSpellDrive && ItemSpellDrive.getSpell(stack) == null) {
					if (foundSource) {
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

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		Spell source = null;
		ItemStack target = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (ISpellAcceptor.hasSpell(stack)) {
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

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

}
