/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.event;

import net.minecraft.world.entity.player.Player;

import vazkii.psi.api.spell.PieceExecutedEvent;
import vazkii.psi.api.spell.SpellPiece;

public class PieceExecutedKubeEvent extends PsiKubeEvent {

	private final PieceExecutedEvent event;

	public PieceExecutedKubeEvent(PieceExecutedEvent event) {
		super(event);
		this.event = event;
	}

	public SpellPiece getPiece() {
		return event.getPiece();
	}

	public Player getPlayer() {
		return event.getPlayerEntity();
	}

}
