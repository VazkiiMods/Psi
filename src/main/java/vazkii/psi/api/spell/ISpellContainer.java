/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [17/01/2016, 14:19:03 (GMT)]
 */
package vazkii.psi.api.spell;

import net.minecraft.item.ItemStack;

/**
 * An ItemStack that implements this counts as a Spell Container, by which
 * a {@link Spell} can be derived and cast from it. This is used by Spell Bullets.
 *
 * As of version 73, this interface should not be used directly,
 * instead interacting with the item via its {@link ISpellAcceptor}.
 */
public interface ISpellContainer extends ISpellSettable {

	Spell getSpell(ItemStack stack);

	boolean containsSpell(ItemStack stack);

	/**
	 * Casts this spell given the passed in context. The spell should be casted
	 * using {@link CompiledSpell#execute(SpellContext)} on {@link SpellContext#cspell}. Thrown exceptions
	 * must be handled and not leaked. Ideal implementation of exception catching is to
	 * alarm the player through a chat message.
	 */
	void castSpell(ItemStack stack, SpellContext context);

	double getCostModifier(ItemStack stack);

	boolean isCADOnlyContainer(ItemStack stack);

}
