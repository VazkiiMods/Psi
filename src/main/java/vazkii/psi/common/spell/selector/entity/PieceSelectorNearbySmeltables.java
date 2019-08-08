/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [22/02/2016, 13:33:56 (GMT)]
 */
package vazkii.psi.common.spell.selector.entity;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import vazkii.psi.api.spell.Spell;

public class PieceSelectorNearbySmeltables extends PieceSelectorNearby {

	public PieceSelectorNearbySmeltables(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate() {
		return this::accept;
	}
	
	public boolean accept(Entity e) {
		if(e instanceof ItemEntity) {
			ItemEntity eitem = (ItemEntity) e;
			ItemStack stack = eitem.getItem();
			ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
		
			return !result.isEmpty();
		}
		
		return false;
	}

}
