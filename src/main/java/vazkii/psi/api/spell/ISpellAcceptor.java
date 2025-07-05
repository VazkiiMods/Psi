/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.PsiAPI;

import java.util.ArrayList;
import java.util.Objects;

/**
 * An item that provides this can have a spell set through right clicking the
 * Spell Programmer, as well as being a valid item for a CAD (if it
 * returns true from {@link #castableFromSocket()}).
 * <p>
 * If the item this counts as a Spell Container, by which
 * a {@link Spell} can be derived and cast from it. This is used by Spell Bullets.
 */
public interface ISpellAcceptor {

	static boolean isAcceptor(ItemStack stack) {
		return !stack.isEmpty() && Objects.nonNull(stack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY));
	}

	static boolean isContainer(ItemStack stack) {
		ISpellAcceptor capability = stack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY);
		if(capability == null)
			return false;
		return capability.castableFromSocket();
	}

	static boolean hasSpell(ItemStack stack) {
		ISpellAcceptor capability = stack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY);
		if(capability == null)
			return false;
		return capability.containsSpell();
	}

	static ISpellAcceptor acceptor(ItemStack stack) {
		return Objects.requireNonNull(stack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY));
	}

	void setSpell(Player player, Spell spell);

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

	/**
	 * Casts this spell in a loopcast.
	 *
	 * @return whether to continue loopcasting
	 */
	default boolean loopcastSpell(SpellContext context) {
		castSpell(context);
		return false;
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
