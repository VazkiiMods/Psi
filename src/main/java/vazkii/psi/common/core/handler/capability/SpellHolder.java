/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Apr 04, 2019, 13:08 AM (EST)]
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
