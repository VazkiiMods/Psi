package vazkii.psi.api.cad;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import vazkii.psi.api.internal.IPlayerData;

/**
 * Posted every tick as a player regenerates PSI.
 * <p>
 * This event is posted even when the player is on regen cooldown,
 * and can be used to control the cooldown time.
 * <p>
 * This event is {@link Cancelable}.
 * If canceled, no regen will occur and the regen cooldown will not change.
 */
@Cancelable
public class RegenPsiEvent extends Event {

    private final int playerPsiCapacity;
    private final int playerPsi;
    private final int cadPsiCapacity;
    private final int cadPsi;
    private final int regenRate;
    private final boolean wasOverflowed;
    private final EntityPlayer player;
    private final IPlayerData playerData;
    private final ItemStack cad;
    private final int previousRegenCooldown;

    // Cannot be set externally.
    private int cadRegenCost = 0;

    private int playerRegen = 0;
    private int cadRegen = 0;
    private boolean healOverflow = false;

    private int regenCooldown;

    public RegenPsiEvent(EntityPlayer player, IPlayerData playerData, ItemStack cad) {
        this.playerPsiCapacity = playerData.getTotalPsi();
        this.playerPsi = playerData.getAvailablePsi();
        this.regenRate = playerData.getRegenPerTick();
        this.wasOverflowed = playerData.isOverflowed();
        this.previousRegenCooldown = playerData.getRegenCooldown();
        this.regenCooldown = Math.max(0, this.previousRegenCooldown - 1);

        if (!cad.isEmpty()) {
            ICAD cadItem = (ICAD) cad.getItem();
            this.cadPsiCapacity = cadItem.getStatValue(cad, EnumCADStat.OVERFLOW);
            this.cadPsi = cadItem.getStoredPsi(cad);
        } else
            this.cadPsiCapacity = this.cadPsi = 0;

        this.player = player;
        this.playerData = playerData;
        this.cad = cad;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public IPlayerData getPlayerData() {
        return playerData;
    }

    public ItemStack getCad() {
        return cad;
    }

    /**
     * Gets the capacity of the player's internal Psi reserves. Is usually 5000.
     */
    public int getPlayerPsiCapacity() {
        return playerPsiCapacity;
    }

    /**
     * Gets the current Psi energy the player has, before regeneration.
     * Is always less than or equal to {@link #getPlayerPsiCapacity()}.
     */
    public int getPlayerPsi() {
        return playerPsi;
    }

    /**
     * Gets the capacity of the CAD's internal Psi reserves. Varies by CAD battery.
     */
    public int getCadPsiCapacity() {
        return cadPsiCapacity;
    }

    /**
     * Gets the current Psi energy the player has.
     * In the case of a capacity of -1, this will most likely be 0.
     * Otherwise, is always less than or equal to {@link #getCadPsiCapacity()}.
     */
    public int getCadPsi() {
        return cadPsi;
    }

    /**
     * Gets the current base Psi regen rate the player has.
     */
    public int getRegenRate() {
        return regenRate;
    }

    /**
     * Gets the regen cooldown from the previous tick.
     */
    public int getPreviousRegenCooldown() {
        return previousRegenCooldown;
    }

    /**
     * Gets whether the player was overflowed before regen started.
     * @see #healOverflow(boolean)
     */
    public boolean wasOverflowed() {
        return wasOverflowed;
    }

    /**
     * Gets the amount of Psi the player's reserves will regenerate this tick.
     * If the CAD is not full, this is typically 0. Addons may alter this behavior.
     */
    public int getPlayerRegen() {
        return playerRegen;
    }

    /**
     * Sets the amount of Psi the player's reserves will regenerate this tick.
     */
    public void setPlayerRegen(int playerRegen) {
        this.playerRegen = playerRegen;
    }

    /**
     * Gets the amount of Psi the CAD's reserves will regenerate this tick.
     * If the CAD has no battery, this value has no effect.
     */
    public int getCadRegen() {
        return cadRegen;
    }

    /**
     * Sets the amount of Psi the CAD's reserves will regenerate this tick.
     * If the CAD has no battery, this value has no effect.
     */
    public void setCadRegen(int cadRegen) {
        this.cadRegen = cadRegen;
    }

    /**
     * Gets how much of the player's regen it cost to regenerate CAD Psi.
     * This value exists because batteries regenerate half as fast as the player's reserves,
     * meaning that they cost twice as much "regen points" to fill.
     * This value has no effect on the final values of regen.
     */
    public int getCadRegenCost() {
        return cadRegenCost;
    }

    /**
     * Gets whether the player will heal from overflow this tick.
     * Typically, this value indicates the player has regenerated more Psi than they can store.
     */
    public boolean willHealOverflow() {
        return healOverflow;
    }

    /**
     * Sets whether the player will heal from overflow this tick.
     */
    public void healOverflow(boolean healOverflow) {
        this.healOverflow = healOverflow;
    }

    /**
     * Gets the player's current regen cooldown.
     */
    public int getRegenCooldown() {
        return regenCooldown;
    }

    /**
     * Sets the player's current regen cooldown.
     * Note that setting this value to 0 will not automatically regenerate Psi this tick.
     * @see #applyNaturalRegen()
     */
    public void setRegenCooldown(int regenCooldown) {
        this.regenCooldown = regenCooldown;
    }

    /**
     * Applies the default logic for regeneration.
     * If regen is not on cooldown, this method is called before the event is fired.
     * <p>
     * The logic is as follows:
     * If the CAD has missing Psi, attempt to fill that.
     * If the player has missing Psi, attempt to fill that.
     * If there is any regeneration remaining, heal from overflow.
     */
    public void applyNaturalRegen() {
        int regenLeft = regenRate;

        cadRegenCost = Math.min(regenLeft, (cadPsiCapacity - cadPsi) * 2);
        if (cadRegenCost > 0) {
            cadRegen += Math.min(Math.max(1, cadRegenCost / 2), cadPsiCapacity - cadPsi);
            regenLeft -= cadRegenCost;
        }

        if (regenLeft > 0 && playerPsi < playerPsiCapacity) {
            playerRegen += Math.max(playerPsiCapacity - playerPsi, regenLeft);
            regenLeft -= playerRegen;
        }

        if (regenLeft > 0)
            healOverflow = true;
    }
}
