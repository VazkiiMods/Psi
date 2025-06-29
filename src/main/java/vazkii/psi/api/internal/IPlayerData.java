/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import vazkii.psi.api.spell.SpellPiece;

import javax.annotation.Nullable;

public interface IPlayerData {

    /**
     * Gets the total amount of psi energy the player has.
     * Does not include the amount in the player's CAD battery.
     */
    int getTotalPsi();

    /**
     * Gets the current amount of psi energy the player has.
     * Does not include the amount in the player's CAD battery.
     */
    int getAvailablePsi();

    /**
     * Gets the current amount of psi energy the player had last tick.
     * Not guaranteed to synchronise properly or be 100% accurate. Use with caution.
     * Does not include the amount in the player's CAD battery.
     */
    int getLastAvailablePsi();

    /**
     * Gets the cooldown before the player can start regenerating psi energy.
     */
    int getRegenCooldown();

    /**
     * Gets how much psi the player regenerates per tick.
     */
    int getRegenPerTick();

    /**
     * Gets whether or not the player is currently unable to cast spells
     * due to overdrawing Psi energy.
     */
    boolean isOverflowed();

    /**
     * Deducts the amount of psi given from the player's psi energy.
     * This will not check against the available amount. Any extra will be either
     * deducted from the player's CAD battery, or deducted as damage.
     */
    void deductPsi(int psi, int cd, boolean sync, boolean shatter);

    /**
     * Gets if the piece group name is unlocked.
     */
    default boolean isPieceGroupUnlocked(ResourceLocation group) {
        return isPieceGroupUnlocked(group, null);
    }

    /**
     * Gets if the piece and group name are unlocked.
     */
    boolean isPieceGroupUnlocked(ResourceLocation group, @Nullable ResourceLocation piece);

    /**
     * Unlocks the given piece group.
     */
    void unlockPieceGroup(ResourceLocation group);

    /**
     * Marks a spell piece as executed. Used for leveling.
     */
    void markPieceExecuted(SpellPiece piece);

    /**
     * Gets a tag compound where you can put your own stuff. If you're
     * going to write any data here, please ensure it's prefixed with
     * your mod ID so stuff doesn't get written over other stuff.
     */
    CompoundTag getCustomData();

    /**
     * Saves the data to the player entity's NBT tags.
     */
    void save();

}
