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

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;
import vazkii.psi.common.entity.EntitySpellCharge;

import java.util.Objects;
import java.util.function.Predicate;

public class PieceSelectorNearbyCharges extends PieceSelectorNearby {

	public PieceSelectorNearbyCharges(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate(SpellContext context) {
		return (Entity e) -> e instanceof EntitySpellCharge && (Objects.requireNonNull(((EntitySpellCharge) e).getThrower()).getName().equals(context.caster.getName()));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		return (double) ((EntityListWrapper) super.execute(context)).unwrap().size();
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
