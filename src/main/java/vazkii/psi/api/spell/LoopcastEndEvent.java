/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.eventbus.api.Event;

import vazkii.psi.api.internal.IPlayerData;

/**
 * Posted when loopcast ends.
 * This event has no result and is not cancellable
 */
public class LoopcastEndEvent extends Event {

	private final PlayerEntity player;
	private final IPlayerData playerData;
	private final Hand hand;
	private final int loopcastAmount;

	public LoopcastEndEvent(PlayerEntity player, IPlayerData playerData, Hand hand, int loopcastAmount) {
		this.player = player;
		this.playerData = playerData;
		this.hand = hand;
		this.loopcastAmount = loopcastAmount;
	}

	public PlayerEntity getPlayer() {
		return player;
	}

	public IPlayerData getPlayerData() {
		return playerData;
	}

	public Hand getHand() {
		return hand;
	}

	public int getLoopcastAmount() {
		return loopcastAmount;
	}
}
