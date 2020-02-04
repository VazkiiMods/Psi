/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [15/02/2016, 15:37:40 (GMT)]
 */
package vazkii.psi.api.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;

import javax.annotation.Nullable;

/**
 * An item that provides this can have a spell set through right clicking the
 * Spell Programmer, as well as being a valid item for a CAD (if it
 * returns true from {@link #castableFromSocket()}).
 */
public interface ISpellAcceptor {


	static boolean isAcceptor(ItemStack stack) {
		return stack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY).isPresent();
	}

	static boolean isContainer(ItemStack stack) {
		return stack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY).map(ISpellAcceptor::castableFromSocket).orElse(false);
	}

	static boolean hasSpell(ItemStack stack) {
		return isContainer(stack) && stack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY)
				.map(ISpellAcceptor::containsSpell)
				.orElse(false);
	}

	static ISpellAcceptor acceptor(ItemStack stack) {
		return stack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY).orElseThrow(NullPointerException::new);
	}

	void setSpell(PlayerEntity player, Spell spell);

	boolean castableFromSocket();

	@Nullable
	Spell getSpell();

	boolean containsSpell();

	void castSpell(SpellContext context);

	double getCostModifier();

	boolean isCADOnlyContainer();
	
	default boolean requiresSneakForSpellSet() {
		return false;
	}

}
