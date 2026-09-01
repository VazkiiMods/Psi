/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.programmer;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.event.PsiEvent;
import vazkii.psi.api.spell.SpellPieceType;

public class ProgrammerPopulateEvent extends PsiEvent {

	@NotNull
	private final Player entity;
	@NotNull
	private Registry<SpellPieceType> spellPieceRegistry;

	public ProgrammerPopulateEvent(@NotNull Player entity, @NotNull Registry<SpellPieceType> registry) {
		this.entity = entity;
		this.spellPieceRegistry = registry;
	}

	@NotNull
	public Player getPlayer() {
		return entity;
	}

	@NotNull
	public Registry<SpellPieceType> getSpellPieceRegistry() {
		return spellPieceRegistry;
	}

	public void setSpellPieceRegistry(@NotNull Registry<SpellPieceType> registry) {
		spellPieceRegistry = registry;
	}

}
