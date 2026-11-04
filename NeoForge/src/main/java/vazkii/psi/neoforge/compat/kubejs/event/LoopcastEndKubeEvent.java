/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.LoopcastEndEvent;

public class LoopcastEndKubeEvent extends PsiKubeEvent {

	private final LoopcastEndEvent event;

	public LoopcastEndKubeEvent(LoopcastEndEvent event) {
		super(event);
		this.event = event;
	}

	public Player getPlayer() {
		return event.getPlayer();
	}

	public IPlayerData getPlayerData() {
		return event.getPlayerData();
	}

	public InteractionHand getHand() {
		return event.getHand();
	}

	public int getLoopcastAmount() {
		return event.getLoopcastAmount();
	}

}
