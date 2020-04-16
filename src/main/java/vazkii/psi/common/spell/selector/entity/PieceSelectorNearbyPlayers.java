package vazkii.psi.common.spell.selector.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import java.util.function.Predicate;

public class PieceSelectorNearbyPlayers extends PieceSelectorNearby {


	public PieceSelectorNearbyPlayers(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate(SpellContext context) {
		return (Entity e) -> e instanceof PlayerEntity;
	}
}
