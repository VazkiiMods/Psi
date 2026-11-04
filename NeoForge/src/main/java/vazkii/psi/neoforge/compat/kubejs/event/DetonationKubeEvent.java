/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import vazkii.psi.api.spell.detonator.DetonationEvent;
import vazkii.psi.api.spell.detonator.IDetonationHandler;

import java.util.List;

public class DetonationKubeEvent extends PsiKubeEvent {

	private final DetonationEvent event;

	public DetonationKubeEvent(DetonationEvent event) {
		super(event);
		this.event = event;
	}

	public Player getPlayer() {
		return event.getPlayer();
	}

	public Entity getFocalPoint() {
		return event.getFocalPoint();
	}

	public double getRange() {
		return event.getRange();
	}

	public List<IDetonationHandler> getCharges() {
		return event.getCharges();
	}

	public void addCharge(IDetonationHandler charge) {
		event.addCharge(charge);
	}

	public void removeCharge(IDetonationHandler charge) {
		event.removeCharge(charge);
	}

}
