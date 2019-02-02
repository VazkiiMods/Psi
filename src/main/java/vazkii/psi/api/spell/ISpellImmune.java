/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [11/03/2016, 19:44:40 (GMT)]
 */
package vazkii.psi.api.spell;

import net.minecraft.entity.Entity;

/**
 * This interface defines an entity that's immune to spells. Any bosses (IBossDisplayData)
 * will also be immune.
 */
public interface ISpellImmune {

	boolean isImmune();
	
	static boolean isImmune(Entity e) {
		return !e.isNonBoss() || (e instanceof ISpellImmune && ((ISpellImmune) e).isImmune());
	}
	
}
