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

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
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
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CAPABILITY;
	}

	@Nullable
	@Override
	@SuppressWarnings("ConstantConditions")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CAPABILITY ? CAPABILITY.cast(this) : null;
	}

	@Override
	public boolean shouldShow(IPlayerData data) {
		return item.shouldShow(stack, data);
	}
}
