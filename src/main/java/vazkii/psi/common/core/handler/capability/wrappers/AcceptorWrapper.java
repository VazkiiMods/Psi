/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler.capability.wrappers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.ISpellSettable;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

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

	@Nonnull
	@Override
	@SuppressWarnings("ConstantConditions")
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return PsiAPI.SPELL_ACCEPTOR_CAPABILITY.orEmpty(capability, LazyOptional.of(() -> this));
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
		return null;
	}

	@Override
	public void setSpell(PlayerEntity player, Spell spell) {
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
		if (container != null) {
			container.castSpell(stack, context);
		}
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
