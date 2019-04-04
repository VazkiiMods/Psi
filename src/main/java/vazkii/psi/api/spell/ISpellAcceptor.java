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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

/**
 * An item that provides this can have a spell set through right clicking the
 * Spell Programmer, as well as being a valid item for a CAD (if it
 * returns true from {@link #castableFromSocket()}).
 */
public interface ISpellAcceptor {
	@CapabilityInject(ISpellAcceptor.class)
	Capability<ISpellAcceptor> CAPABILITY = null;

	static boolean isAcceptor(ItemStack stack) {
		return stack.hasCapability(CAPABILITY, null);
	}

	static boolean isContainer(ItemStack stack) {
		return isAcceptor(stack) && acceptor(stack).castableFromSocket();
	}

	static boolean hasSpell(ItemStack stack) {
		return isContainer(stack) && acceptor(stack).containsSpell();
	}

	static ISpellAcceptor acceptor(ItemStack stack) {
		return stack.getCapability(CAPABILITY, null);
	}

	void setSpell(EntityPlayer player, Spell spell);

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
