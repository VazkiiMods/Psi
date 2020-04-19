/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler.capability;

import net.minecraft.entity.player.PlayerEntity;

import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import javax.annotation.Nullable;

public class SpellHolder implements ISpellAcceptor {
	private Spell spell;

	@Override
	public void setSpell(PlayerEntity player, Spell spell) {
		this.spell = spell;
	}

	@Override
	public boolean castableFromSocket() {
		return false;
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
	public void castSpell(SpellContext context) {
		// NO-OP
	}

	@Override
	public double getCostModifier() {
		return 1;
	}

	@Override
	public boolean isCADOnlyContainer() {
		return false;
	}
}
