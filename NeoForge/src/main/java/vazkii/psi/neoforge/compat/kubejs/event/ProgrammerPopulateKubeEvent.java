/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.event;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;

import vazkii.psi.api.spell.SpellPieceType;
import vazkii.psi.api.spell.programmer.ProgrammerPopulateEvent;

public class ProgrammerPopulateKubeEvent extends PsiKubeEvent {

	private final ProgrammerPopulateEvent event;

	public ProgrammerPopulateKubeEvent(ProgrammerPopulateEvent event) {
		super(event);
		this.event = event;
	}

	public Player getPlayer() {
		return event.getPlayer();
	}

	public Registry<SpellPieceType> getSpellPieceRegistry() {
		return event.getSpellPieceRegistry();
	}

	public void setSpellPieceRegistry(Registry<SpellPieceType> registry) {
		event.setSpellPieceRegistry(registry);
	}

}
