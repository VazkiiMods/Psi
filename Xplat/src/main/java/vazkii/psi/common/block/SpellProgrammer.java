/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block;

import net.minecraft.world.entity.player.Player;

import vazkii.psi.api.spell.Spell;

public interface SpellProgrammer {

	void setSpellFromEditor(Player player, Spell spell);

	boolean canCompile();

	Spell getSpellForDrive();

	boolean setSpellFromDrive(Player player, Spell spell);

}
