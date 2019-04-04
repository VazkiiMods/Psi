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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.psi.api.spell.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AcceptorWrapper implements ISpellAcceptor, ICapabilityProvider {

	private final ItemStack stack;
	private final ISpellSettable item;
	private final ISpellContainer container;

	public AcceptorWrapper(ItemStack stack) {
		this.stack = stack;
		this.item = (ISpellSettable) stack.getItem();
		this.container = item instanceof ISpellContainer ? (ISpellContainer) item : null;
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
	public void setSpell(EntityPlayer player, Spell spell) {
		item.setSpell(player, stack, spell);
	}

	@Override
	public boolean castableFromSocket() {
		return container != null;
	}

	@Nullable
	@Override
	public Spell getSpell() {
		return container != null ? container.getSpell(stack) : null;
	}

	@Override
	public boolean containsSpell() {
		return container != null && container.containsSpell(stack);
	}

	@Override
	public void castSpell(SpellContext context) {
		if (container != null)
			container.castSpell(stack, context);
	}

	@Override
	public double getCostModifier() {
		return container != null ? container.getCostModifier(stack) : 1;
	}

	@Override
	public boolean isCADOnlyContainer() {
		return container != null && container.isCADOnlyContainer(stack);
	}
}
