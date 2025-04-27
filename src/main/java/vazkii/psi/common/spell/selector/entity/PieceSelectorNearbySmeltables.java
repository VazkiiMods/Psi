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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import java.util.function.Predicate;

public class PieceSelectorNearbySmeltables extends PieceSelectorNearby {

	public PieceSelectorNearbySmeltables(Spell spell) {
		super(spell);
	}

	public static ItemStack simulateSmelt(Level world, ItemStack input) {
		return world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(input), world)
				.map(foo -> foo.value().getResultItem(RegistryAccess.EMPTY))
				.orElse(ItemStack.EMPTY);
	}

	@Override
	public Predicate<Entity> getTargetPredicate(SpellContext context) {
		return this::accept;
	}

	public boolean accept(Entity e) {
		if(e instanceof ItemEntity eitem) {
			return !simulateSmelt(e.getCommandSenderWorld(), eitem.getItem()).isEmpty();
		}

		return false;
	}

}
