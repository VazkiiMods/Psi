/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector.entity;

import net.minecraft.world.entity.Entity;

import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.interval.IntervalNumber;
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
		return (Entity e) -> e instanceof EntitySpellCharge && (Objects.requireNonNull(((EntitySpellCharge) e).getOwner()).getName().equals(context.caster.getName()));
	}
	
	@Override
	public @NotNull IntervalNumber evaluate() {
		return IntervalNumber.fromRange(0, Double.POSITIVE_INFINITY);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		return (double) ((EntityListWrapper) super.execute(context)).size();
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
