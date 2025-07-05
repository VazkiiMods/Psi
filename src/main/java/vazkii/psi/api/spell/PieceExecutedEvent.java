/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Posted after a piece executed.
 * Hook in here to add your own locking mechanics like 1.12 Psi had
 * This event has no result and is not cancellable
 */
public class PieceExecutedEvent extends Event {

	@NotNull
	private final SpellPiece piece;

	@NotNull
	private final Player playerEntity;

	public PieceExecutedEvent(@NotNull SpellPiece piece, @NotNull Player playerEntity) {
		this.piece = piece;
		this.playerEntity = playerEntity;
	}

	@NotNull
	public SpellPiece getPiece() {
		return piece;
	}

	@NotNull
	public Player getPlayerEntity() {
		return playerEntity;
	}
}
