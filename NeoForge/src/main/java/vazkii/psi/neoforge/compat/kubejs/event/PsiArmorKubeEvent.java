/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import vazkii.psi.api.exosuit.PsiArmorEvent;

public class PsiArmorKubeEvent extends PsiKubeEvent {

	private final PsiArmorEvent event;

	public PsiArmorKubeEvent(PsiArmorEvent event) {
		super(event);
		this.event = event;
	}

	public Player getPlayer() {
		return event.getPlayer();
	}

	public String getType() {
		return event.type;
	}

	public double getDamage() {
		return event.damage;
	}

	public LivingEntity getAttacker() {
		return event.attacker;
	}

}
