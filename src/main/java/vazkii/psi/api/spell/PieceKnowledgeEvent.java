/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Jun 24, 2019, 11:21 AM (EST)]
 */
package vazkii.psi.api.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Event;
import vazkii.psi.api.internal.IPlayerData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Posted when a piece's unlocked status is checked.
 *
 * This is not fired for players in creative mode.
 * Those players are always assumed to have each piece unlocked.
 *
 * This event has a result. {@link HasResult}
 * {@link Result#DENY} will prevent the piece from being known, whether or not the player would normally know it.
 * {@link Result#DEFAULT} will defer to default behavior.
 * {@link Result#ALLOW} will force the piece to be unlocked, whether or not the player would know it.
 */
@Event.HasResult
public class PieceKnowledgeEvent extends Event {
	@Nonnull
	private final String pieceGroup;

	@Nullable
	private final String pieceName;

	@Nonnull
	private final PlayerEntity player;

	@Nonnull
	private final IPlayerData data;

	private final boolean isUnlocked;

	public PieceKnowledgeEvent(@Nonnull String pieceGroup, @Nullable String pieceName, @Nonnull PlayerEntity player, @Nonnull IPlayerData data, boolean isUnlocked) {
		this.pieceGroup = pieceGroup;
		this.pieceName = pieceName;
		this.player = player;
		this.data = data;
		this.isUnlocked = isUnlocked;
	}

	/**
	 * The group which is being checked for.
	 */
	@Nonnull
	public String getPieceGroup() {
		return pieceGroup;
	}

	/**
	 * The piece name which is being checked for.
	 * May be null if called from legacy code or in a situation where only the group's presence matters.
	 */
	@Nullable
	public String getPieceName() {
		return pieceName;
	}

	/**
	 * The player who is being checked.
	 */
	@Nonnull
	public PlayerEntity getPlayer() {
		return player;
	}

	/**
	 * The player data which is being checked.
	 */
	@Nonnull
	public IPlayerData getData() {
		return data;
	}

	/**
	 * Whether, under default behavior, the piece would be unlocked.
	 */
	public boolean isUnlocked() {
		return isUnlocked;
	}
}
