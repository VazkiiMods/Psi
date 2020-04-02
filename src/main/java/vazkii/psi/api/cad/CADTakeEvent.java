package vazkii.psi.api.cad;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;


/**
 * Posted when a CAD is being taken from the assembler.
 * <p>
 * This event is {@link Cancelable}.
 */
@Cancelable
public class CADTakeEvent extends Event {

	private final ItemStack cad;
	private final ITileCADAssembler assembler;
	private final PlayerEntity player;

	@Nullable
	private String cancellationMessage = "psimisc.cancelled_cad_take";

	private float sound = 0.5f;

	public CADTakeEvent(ItemStack cad, ITileCADAssembler assembler, PlayerEntity player) {
		this.cad = cad;
		this.assembler = assembler;
		this.player = player;
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

	public ITileCADAssembler getAssembler() {
		return assembler;
	}

	public ItemStack getCad() {
		return cad;
	}

	public PlayerEntity getPlayer() {
		return player;
	}


}
