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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * An Item that implements this can have a spell set through right clicking the
 * Spell Programmer.
 *
 * As of version 73, this interface should not be used directly,
 * instead interacting with the item via its {@link ISpellAcceptor}.
 */
public interface ISpellSettable {

	void setSpell(EntityPlayer player, ItemStack stack, Spell spell);
	
	boolean requiresSneakForSpellSet(ItemStack stack);

}
