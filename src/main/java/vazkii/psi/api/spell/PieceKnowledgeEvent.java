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
import net.neoforged.bus.api.ICancellableEvent;
import vazkii.psi.api.internal.IPlayerData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Posted when a piece's unlocked status is checked.
 * <p>
 * This is not fired for players in creative mode.
 * Those players are always assumed to have each piece unlocked.
 * <p>
 * This event is Cancelable.
 * Canceling it will result in no CAD being assembled.
 */
public class PieceKnowledgeEvent extends Event implements ICancellableEvent {
    @Nonnull
    private final ResourceLocation pieceGroup;

    @Nullable
    private final ResourceLocation pieceName;

    @Nonnull
    private final Player player;

    @Nonnull
    private final IPlayerData data;

    private final boolean isUnlocked;

    public PieceKnowledgeEvent(@Nonnull ResourceLocation pieceGroup, @Nullable ResourceLocation pieceName, @Nonnull Player player, @Nonnull IPlayerData data, boolean isUnlocked) {
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
    public ResourceLocation getPieceGroup() {
        return pieceGroup;
    }

    /**
     * The piece name which is being checked for.
     * May be null if called from legacy code or in a situation where only the group's presence matters.
     */
    @Nullable
    public ResourceLocation getPieceName() {
        return pieceName;
    }

    /**
     * The player who is being checked.
     */
    @Nonnull
    public Player getPlayer() {
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
