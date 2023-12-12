/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityTriggerSensor implements IDetonationHandler, ICapabilityProvider {

	public final Player player;
	public static final String TRIGGER_TICK = LibMisc.MOD_ID + ":LastTriggeredDetonation";

	public CapabilityTriggerSensor(Player player) {
		this.player = player;
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return PsiAPI.DETONATION_HANDLER_CAPABILITY.orEmpty(capability, LazyOptional.of(() -> this));
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
