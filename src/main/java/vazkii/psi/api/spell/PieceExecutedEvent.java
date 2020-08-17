/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;

/**
 * Posted after a piece executed.
 * Hook in here to add your own locking mechanics like 1.12 Psi had
 * This event has no result and is not cancellable
 */
public class PieceExecutedEvent extends Event {

	@Nonnull
	private final SpellPiece piece;

	@Nonnull
	private final PlayerEntity playerEntity;

	public PieceExecutedEvent(@Nonnull SpellPiece piece, @Nonnull PlayerEntity playerEntity) {
		this.piece = piece;
		this.playerEntity = playerEntity;
	}

	@Nonnull
	public SpellPiece getPiece() {
		return piece;
	}

	@Nonnull
	public PlayerEntity getPlayerEntity() {
		return playerEntity;
	}
}
