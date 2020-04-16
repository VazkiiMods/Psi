package vazkii.psi.common.spell.selector.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import java.util.function.Predicate;

public class PieceSelectorNearbyVehicles extends PieceSelectorNearby {

	public PieceSelectorNearbyVehicles(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate(SpellContext context) {
		return (Entity e) -> e instanceof MinecartEntity || e instanceof BoatEntity || (e instanceof AbstractHorseEntity && ((AbstractHorseEntity) e).isHorseSaddled()) || (e instanceof PigEntity && ((PigEntity) e).getSaddled());
	}
}
