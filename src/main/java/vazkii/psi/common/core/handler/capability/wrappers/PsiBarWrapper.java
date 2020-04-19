/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler.capability.wrappers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.IShowPsiBar;
import vazkii.psi.api.internal.IPlayerData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PsiBarWrapper implements IPsiBarDisplay, ICapabilityProvider {

	private final ItemStack stack;
	private final IShowPsiBar item;

	public PsiBarWrapper(ItemStack stack) {
		this.stack = stack;
		this.item = (IShowPsiBar) stack.getItem();
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return PsiAPI.PSI_BAR_DISPLAY_CAPABILITY.orEmpty(capability, LazyOptional.of(() -> this));
	}

	@Override
	public boolean shouldShow(IPlayerData data) {
		return item.shouldShow(stack, data);
	}
}
