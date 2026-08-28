/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.capability;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.core.handler.PlayerData;
import vazkii.psi.common.core.handler.PsiPlayerData;

public record CapabilityTriggerSensor(
		Player player) implements IDetonationHandler {

	@Override
	public void detonate() {
		PlayerData playerData = PsiPlayerData.get(player);
		long worldTime = player.level().getGameTime();

		if(playerData.lastTriggeredDetonation != worldTime) {
			playerData.lastTriggeredDetonation = worldTime;
			PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.DETONATE));
		}
	}

	@Override
	public Vec3 objectLocus() {
		return player.position();
	}
}
