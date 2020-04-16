package vazkii.psi.common.core.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.item.ItemTriggerExosuitSensor;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityTriggerSensor implements IDetonationHandler, ICapabilityProvider {

	public final PlayerEntity player;
	public static final String EVENT_TRIGGER = LibMisc.MOD_ID + ".event.spell_detonate";
	public static final String TRIGGER_TICK = LibMisc.MOD_ID + ":LastTriggeredDetonation";

	public CapabilityTriggerSensor(PlayerEntity player){
		this.player = player;
	}


	@Override
	@SuppressWarnings("ConstantConditions")
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return PsiAPI.DETONATION_HANDLER_CAPABILITY.orEmpty(capability, LazyOptional.of(() -> this));
	}



	@Override
	public void detonate() {
		CompoundNBT playerData = player.getPersistentData();
		long detonated = playerData.getLong(TRIGGER_TICK);
		long worldTime = player.world.getGameTime();

		if (detonated != worldTime) {
			playerData.putLong(TRIGGER_TICK, worldTime);

			PsiArmorEvent.post(new PsiArmorEvent(player, ItemTriggerExosuitSensor.EVENT_TRIGGER));
		}
	}

	@Override
	public Vec3d objectLocus() {
		return player.getPositionVector();
	}
}
