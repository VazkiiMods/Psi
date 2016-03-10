/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/01/2016, 17:08:06 (GMT)]
 */
package vazkii.psi.api.cad;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.SpellRuntimeException;

/**
 * Base interface for a CAD. You probably shouldn't implement this.
 */
public interface ICAD extends ISocketable {

	/**
	 * Gets the component used for this CAD in the given slot.
	 */
	public ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type);

	/**
	 * Gets the value of a given CAD stat.
	 */
	public int getStatValue(ItemStack stack, EnumCADStat stat);

	/**
	 * Gets how much Psi is stored in this CAD's battery.
	 */
	public int getStoredPsi(ItemStack stack);

	/**
	 * Has the CAD regen psi equal to the amount passed in. Will never go above
	 * the value of the CAD's OVERFLOW stat.
	 */
	public void regenPsi(ItemStack stack, int psi);

	/**
	 * Consumes psi from the CAD's battery equal to the amount passed in. Returns
	 * the remainder that couldn't be consumed. Used to prevent damage.
	 */
	public int consumePsi(ItemStack stack, int psi);

	/**
	 * Gets how many vectors this CAD can store in memory.
	 */
	public int getMemorySize(ItemStack stack);
	
	public void setStoredVector(ItemStack stack, int memorySlot, Vector3 vec) throws SpellRuntimeException;
	
	public Vector3 getStoredVector(ItemStack stack, int memorySlot) throws SpellRuntimeException;
	
	/**
	 * Gets the color of the spells projected by this CAD. Usually just goes back
	 * to ICADColorizer.getColor().
	 */
	@SideOnly(Side.CLIENT)
	public int getSpellColor(ItemStack stack);

}
