/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [17/01/2016, 14:19:03 (GMT)]
 */
package vazkii.psi.api.spell;

import net.minecraft.item.ItemStack;

public interface ISpellContainer {

	public void setSpell(ItemStack stack, Spell spell);
	
	public Spell getSpell(ItemStack stack);
	
	public boolean containsSpell(ItemStack stack);
	
	public default boolean canSetSpell(ItemStack stack) {
		return !containsSpell(stack);
	}
	
}
