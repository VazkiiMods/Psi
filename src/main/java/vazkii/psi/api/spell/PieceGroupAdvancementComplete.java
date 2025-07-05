/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Posted after a piece executed.
 * Hook in here to add your own locking mechanics like 1.12 Psi had
 * This event has no result and is not cancellable
 */
public class PieceGroupAdvancementComplete extends Event {

	@Nullable
	private final SpellPiece piece;

	@NotNull
	private final Player playerEntity;

	@NotNull
	private final ResourceLocation pieceGroup;

	public PieceGroupAdvancementComplete(@Nullable SpellPiece piece, @NotNull Player playerEntity, @NotNull ResourceLocation pieceGroup) {
		this.piece = piece;
		this.playerEntity = playerEntity;
		this.pieceGroup = pieceGroup;
	}

	@NotNull
	public ResourceLocation getPieceGroup() {
		return pieceGroup;
	}

	@Nullable
	public SpellPiece getPiece() {
		return piece;
	}

	@NotNull
	public Player getPlayerEntity() {
		return playerEntity;
	}
}
