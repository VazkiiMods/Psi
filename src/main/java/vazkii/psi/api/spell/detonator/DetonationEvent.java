/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.detonator;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

/**
 * Posted before a spell executes.
 *
 * This event is {@link Cancelable}.
 * Canceling it will cause the detonator's default behavior to be suppressed.
 */
@Cancelable
public class DetonationEvent extends Event {

	private final Player player;
	private final Entity focalPoint;
	private final double range;
	private final List<IDetonationHandler> charges;

	public DetonationEvent(Player player, Entity focalPoint, double range, List<IDetonationHandler> charges) {
		this.player = player;
		this.focalPoint = focalPoint;
		this.range = range;
		this.charges = charges;
	}

	public Player getPlayer() {
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
