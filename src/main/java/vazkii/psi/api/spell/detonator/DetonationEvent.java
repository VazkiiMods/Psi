/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [May 19, 2019, 23:12 AM (EST)]
 */
package vazkii.psi.api.spell.detonator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

/**
 * Posted before a spell executes.
 *
 * This event is {@link Cancelable}.
 * Canceling it will cause the detonator's default behavior to be suppressed.
 */
@Cancelable
public class DetonationEvent extends Event {

	private final EntityPlayer player;
	private final Entity focalPoint;
	private final double range;
	private final List<IDetonationHandler> charges;

	public DetonationEvent(EntityPlayer player, Entity focalPoint, double range, List<IDetonationHandler> charges) {
		this.player = player;
		this.focalPoint = focalPoint;
		this.range = range;
		this.charges = charges;
	}

	public EntityPlayer getPlayer() {
		return player;
	}

	public Entity getFocalPoint() {
		return focalPoint;
	}

	public double getRange() {
		return range;
	}

	public List<IDetonationHandler> getCharges() {
		return charges;
	}

	public void addCharge(IDetonationHandler charge) {
		charges.add(charge);
	}

	public void removeCharge(IDetonationHandler charge) {
		charges.remove(charge);
	}
}
