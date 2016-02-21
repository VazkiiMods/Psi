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

import net.minecraft.item.ItemStack;

/**
 * An Item that implements this can have a spell set through right clicking the
 * Spell Programmer.
 */
public interface ISpellSettable {

	public void setSpell(ItemStack stack, Spell spell);

}
