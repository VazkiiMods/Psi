/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import vazkii.psi.api.PsiAPI;

import javax.annotation.Nullable;

import java.util.ArrayList;

/**
 * An item that provides this can have a spell set through right clicking the
 * Spell Programmer, as well as being a valid item for a CAD (if it
 * returns true from {@link #castableFromSocket()}).
 *
 * If the item this counts as a Spell Container, by which
 * a {@link Spell} can be derived and cast from it. This is used by Spell Bullets.
 */
public interface ISpellAcceptor {

	static boolean isAcceptor(ItemStack stack) {
		return !stack.isEmpty() && stack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY).isPresent();
	}

	static boolean isContainer(ItemStack stack) {
		return stack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY).map(ISpellAcceptor::castableFromSocket).orElse(false);
	}

	static boolean hasSpell(ItemStack stack) {
		return stack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY)
				.map(ISpellAcceptor::containsSpell)
				.orElse(false);
	}

	static ISpellAcceptor acceptor(ItemStack stack) {
		return stack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY).orElseThrow(NullPointerException::new);
	}

	void setSpell(PlayerEntity player, Spell spell);

	/**
	 * @return true if this can be placed in a CAD. Override the following methods in that case.
	 */
	default boolean castableFromSocket() {
		return false;
	}

	@Nullable
	default Spell getSpell() {
		return null;
	}

	default boolean containsSpell() {
		return false;
	}

	/**
	 * Casts this spell given the passed in context. The spell should be casted
	 * using {@link CompiledSpell#execute(SpellContext)} on {@link SpellContext#cspell}. Thrown exceptions
	 * must be handled and not leaked. Ideal implementation of exception catching is to
	 * alarm the player through a chat message.
	 */
	default ArrayList<Entity> castSpell(SpellContext context) {
		return null;
	}

	default void loopcastSpell(SpellContext context) {
		castSpell(context);
	}

	default double getCostModifier() {
		return 1.0;
	}

	default boolean isCADOnlyContainer() {
		return false;
	}

	default boolean requiresSneakForSpellSet() {
		return false;
	}

}
