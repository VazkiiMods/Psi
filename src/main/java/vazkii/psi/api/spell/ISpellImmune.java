/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.world.entity.Entity;

import vazkii.psi.api.PsiAPI;

/**
 * This interface defines an entity that's immune to spells. Any bosses (IBossDisplayData)
 * will also be immune.
 * <p>
 * If an entity provides a capability of type ISpellImmune,
 * they will also be immune if that returns true.
 */
public interface ISpellImmune {

	static boolean isImmune(Entity e) {
		return !e.canUsePortal(false) || e.getCapability(PsiAPI.SPELL_IMMUNE_CAPABILITY).isImmune();
	}

	boolean isImmune();

}
