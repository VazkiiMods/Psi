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
import net.minecraft.world.entity.monster.Enemy;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import java.util.function.Predicate;

public class PieceSelectorNearbyEnemies extends PieceSelectorNearby {

    public PieceSelectorNearbyEnemies(Spell spell) {
        super(spell);
    }

    @Override
    public Predicate<Entity> getTargetPredicate(SpellContext context) {
        return (Entity e) -> e instanceof Enemy;
    }

}
