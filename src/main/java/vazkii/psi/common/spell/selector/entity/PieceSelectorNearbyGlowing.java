package vazkii.psi.common.spell.selector.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EyeOfEnderEntity;
import net.minecraft.potion.Effects;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import java.util.function.Predicate;

public class PieceSelectorNearbyGlowing extends PieceSelectorNearby {


	public PieceSelectorNearbyGlowing(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate(SpellContext context) {
		return (Entity e) -> e != null && (e instanceof EyeOfEnderEntity || e.isBurning() || e.isGlowing() ||
				(e instanceof LivingEntity && ((LivingEntity) e).isPotionActive(Effects.GLOWING)));
	}
}
