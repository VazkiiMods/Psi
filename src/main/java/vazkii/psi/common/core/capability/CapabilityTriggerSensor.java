/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
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
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityTriggerSensor implements IDetonationHandler, ICapabilityProvider<EntityCapability<?, Void>, Void, CapabilityTriggerSensor> {

    public static final String TRIGGER_TICK = LibMisc.MOD_ID + ":LastTriggeredDetonation";
    public final Player player;

    public CapabilityTriggerSensor(Player player) {
        this.player = player;
    }

    @Nullable
    @Override
    public CapabilityTriggerSensor getCapability(@Nonnull EntityCapability<?, Void> capability, @Nullable Void facing) {
        if (capability == PsiAPI.DETONATION_HANDLER_CAPABILITY) {
            return this;
        }
        return null;
    }

    @Override
    public void detonate() {
        CompoundTag playerData = player.getPersistentData();
        long detonated = playerData.getLong(TRIGGER_TICK);
        long worldTime = player.level().getGameTime();

        if (detonated != worldTime) {
            playerData.putLong(TRIGGER_TICK, worldTime);

            PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.DETONATE));
        }
    }

    @Override
    public Vec3 objectLocus() {
        return player.position();
    }
}
