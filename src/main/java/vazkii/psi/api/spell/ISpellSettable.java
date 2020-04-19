/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * An Item that implements this can have a spell set through right clicking the
 * Spell Programmer.
 *
 * As of version 73, this interface should not be used directly,
 * instead interacting with the item via its {@link ISpellAcceptor}.
 */
public interface ISpellSettable {

	void setSpell(PlayerEntity player, ItemStack stack, Spell spell);

	boolean requiresSneakForSpellSet(ItemStack stack);

}
