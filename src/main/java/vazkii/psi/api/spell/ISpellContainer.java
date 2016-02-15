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
 * a {@link Spell} can be derived and casted sfrom it. This is used by Spell Bullets.
 */
public interface ISpellContainer extends ISpellSettable {

	public Spell getSpell(ItemStack stack);

	public boolean containsSpell(ItemStack stack);

	/**
	 * Casts this spell given the passed in context. The spell should be casted
	 * using {@link CompiledSpell#execute(SpellContext)} on {@link SpellContext#cspell}. Thrown exceptions
	 * must be handled and not leaked. Ideal implementation of exception catching is to
	 * alarm the player through a chat message.
	 */
	public void castSpell(ItemStack stack, SpellContext context);

	public double getCostModifier(ItemStack stack);

}
