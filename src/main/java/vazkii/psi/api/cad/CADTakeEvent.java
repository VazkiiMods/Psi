package vazkii.psi.api.cad;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import vazkii.psi.common.block.tile.container.slot.SlotCADOutput;

import javax.annotation.Nullable;


/**
 * Posted when a CAD is being taken from the assembler.
 * <p>
 * This event is {@link Cancelable}.
 */
@Cancelable
public class CADTakeEvent extends Event {

    private float sound;

    private final SlotCADOutput slot;
    private final EntityPlayer player;

    @Nullable
    private String cancellationMessage = "psimisc.cancelledCADTake";

    public CADTakeEvent(SlotCADOutput slot, EntityPlayer player, float sound) {
        this.sound = sound;
        this.player = player;
        this.slot = slot;
    }

    @Nullable
    public String getCancellationMessage() {
        return cancellationMessage;
    }

    public void setCancellationMessage(@Nullable String cancellationMessage) {
        this.cancellationMessage = cancellationMessage;
    }

    public float getSound() {
        return sound;
    }

    public void setSound(float sound) {
        this.sound = sound;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public SlotCADOutput getSlot() {
        return slot;
    }


}
