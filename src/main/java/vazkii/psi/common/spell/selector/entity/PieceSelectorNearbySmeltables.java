/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import java.util.function.Predicate;

public class PieceSelectorNearbySmeltables extends PieceSelectorNearby {
	private static final Inventory DUMMY_INV = new Inventory(3);

	public PieceSelectorNearbySmeltables(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate(SpellContext context) {
		return this::accept;
	}

	public static ItemStack simulateSmelt(World world, ItemStack input) {
		DUMMY_INV.clear();
		DUMMY_INV.setInventorySlotContents(0, input);
		return world.getRecipeManager().getRecipe(IRecipeType.SMELTING, DUMMY_INV, world)
				.map(IRecipe::getRecipeOutput)
				.orElse(ItemStack.EMPTY);
	}

	public boolean accept(Entity e) {
		if (e instanceof ItemEntity) {
			ItemEntity eitem = (ItemEntity) e;
			return !simulateSmelt(e.getEntityWorld(), eitem.getItem()).isEmpty();
		}

		return false;
	}

}
