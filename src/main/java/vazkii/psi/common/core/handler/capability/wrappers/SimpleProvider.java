/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Apr 03, 2019, 15:19 AM (EST)]
 */
package vazkii.psi.common.core.handler.capability.wrappers;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleProvider<CAP> implements ICapabilityProvider {

	private final Capability<CAP> capability;
	private final CAP value;

	public SimpleProvider(Capability<CAP> capability, CAP value) {
		this.capability = capability;
		this.value = value;
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return this.capability.orEmpty(capability, LazyOptional.of(() -> value));
	}
}
