/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector.entity;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import java.util.function.Predicate;

public class PieceSelectorNearbySmeltables extends PieceSelectorNearby {
	private static final SimpleContainer DUMMY_INV = new SimpleContainer(3);

	public PieceSelectorNearbySmeltables(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate(SpellContext context) {
		return this::accept;
	}

	public static ItemStack simulateSmelt(Level world, ItemStack input) {
		DUMMY_INV.clearContent();
		DUMMY_INV.setItem(0, input);
		return world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, DUMMY_INV, world)
				.map(foo -> foo.getResultItem(RegistryAccess.EMPTY))
				.orElse(ItemStack.EMPTY);
	}

	public boolean accept(Entity e) {
		if(e instanceof ItemEntity) {
			ItemEntity eitem = (ItemEntity) e;
			return !simulateSmelt(e.getCommandSenderWorld(), eitem.getItem()).isEmpty();
		}

		return false;
	}

}
