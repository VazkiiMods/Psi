package vazkii.psi.api.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Posted after a piece executed.
 * Hook in here to add your own locking mechanics like 1.12 Psi had
 * This event has no result and is not cancellable
 */
public class PieceGroupAdvancementComplete extends Event {

	@Nullable
	private final SpellPiece piece;

	@Nonnull
	private final PlayerEntity playerEntity;

	@Nonnull
	private final ResourceLocation pieceGroup;

	public PieceGroupAdvancementComplete(@Nullable SpellPiece piece, @Nonnull PlayerEntity playerEntity, @Nonnull ResourceLocation pieceGroup) {
		this.piece = piece;
		this.playerEntity = playerEntity;
		this.pieceGroup = pieceGroup;
	}

	@Nonnull
	public ResourceLocation getPieceGroup() {
		return pieceGroup;
	}

	@Nullable
	public SpellPiece getPiece() {
		return piece;
	}

	@Nonnull
	public PlayerEntity getPlayerEntity() {
		return playerEntity;
	}
}
