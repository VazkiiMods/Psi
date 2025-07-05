/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler.capability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import java.util.ArrayList;

public class SpellHolder implements ISpellAcceptor {
	private Spell spell;

	@Override
	public void setSpell(Player player, Spell spell) {
		this.spell = spell;
	}

	@Nullable
	@Override
	public Spell getSpell() {
		return spell;
	}

	@Override
	public boolean containsSpell() {
		return spell != null;
	}

	@Override
	public ArrayList<Entity> castSpell(SpellContext context) {
		// NO-OP
		return null;
	}

	@Override
	public double getCostModifier() {
		return 1;
	}

}
