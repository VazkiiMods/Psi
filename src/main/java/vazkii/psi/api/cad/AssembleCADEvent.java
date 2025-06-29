/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * Posted when a CAD ItemStack is constructed within the assembler,
 * before it has been taken out of the slot or crafted.
 * <p>
 * Editing the ItemStack will change the resulting item.
 * <p>
 * Note that this event may be fired several times.
 * The firing of an {@link AssembleCADEvent} does not necessarily mean that
 * anything has changed, meaning you shouldn't take in-world actions based on this event.
 * <p>
 * This event is Cancelable.
 * Canceling it will result in no CAD being assembled.
 */
public class AssembleCADEvent extends Event implements ICancellableEvent {

    private final ITileCADAssembler assembler;
    private final Player player;
    private ItemStack cad;

    public AssembleCADEvent(ItemStack cad, ITileCADAssembler assembler, Player player) {
        this.cad = cad;
        this.assembler = assembler;
        this.player = player;
    }

    public ITileCADAssembler getAssembler() {
        return assembler;
    }

    public ItemStack getCad() {
        return cad;
    }

    public void setCad(ItemStack cad) {
        if (!cad.isEmpty() && !(cad.getItem() instanceof ICAD)) {
            throw new IllegalStateException("Only a CAD can be crafted by the CAD Assembler!");
        }
        this.cad = cad;
    }

    public Player getPlayer() {
        return player;
    }
}
