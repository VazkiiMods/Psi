/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [02/01/2019, 21:30:20 (GMT)]
 */
package vazkii.psi.api.cad;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Posted when a CAD ItemStack is constructed within the assembler,
 * before it has been taken out of the slot or crafted.
 *
 * Editing the ItemStack will change the resulting item.
 *
 * Note that this event may be fired several times.
 * The firing of an {@link AssembleCADEvent} does not necessarily mean that
 * anything has changed, meaning you shouldn't take in-world actions based on this event.
 *
 * This event is {@link Cancelable}.
 * Canceling it will result in no CAD being assembled.
 */
@Cancelable
public class AssembleCADEvent extends Event {

	private ItemStack cad;
	private final ITileCADAssembler assembler;
	private final PlayerEntity player;

	public AssembleCADEvent(ItemStack cad, ITileCADAssembler assembler, PlayerEntity player) {
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
		if (!cad.isEmpty() && !(cad.getItem() instanceof ICAD))
			throw new IllegalStateException("Only a CAD can be crafted by the CAD Assembler!");
		this.cad = cad;
	}

	public PlayerEntity getPlayer() {
		return player;
	}
}
