/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.detonator.IDetonationHandler;

public record CapabilityTriggerSensor(
		Player player) implements IDetonationHandler, ICapabilityProvider<EntityCapability<?, Void>, Void, CapabilityTriggerSensor> {

	public static final String TRIGGER_TICK = PsiAPI.MOD_ID + ":LastTriggeredDetonation";

	@Nullable
	@Override
	public CapabilityTriggerSensor getCapability(@NotNull EntityCapability<?, Void> capability, @Nullable Void facing) {
		if(capability == PsiAPI.DETONATION_HANDLER_CAPABILITY) {
			return this;
		}
		return null;
	}

	@Override
	public void detonate() {
		CompoundTag playerData = player.getPersistentData();
		long detonated = playerData.getLong(TRIGGER_TICK);
		long worldTime = player.level().getGameTime();

		if(detonated != worldTime) {
			playerData.putLong(TRIGGER_TICK, worldTime);

			PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.DETONATE));
		}
	}

	@Override
	public Vec3 objectLocus() {
		return player.position();
	}
}
