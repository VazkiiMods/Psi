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
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.SpellRuntimeException;

/**
 * Base interface for a CAD. You probably shouldn't implement this,
 * unless you absolutely know what you are doing.
 */
public interface ICAD extends ISocketable {

	String TAG_COMPONENT_PREFIX = "component";

	static void setComponent(ItemStack stack, ItemStack componentStack) {
		if(!componentStack.isEmpty() && componentStack.getItem() instanceof ICADComponent) {
			ICADComponent component = (ICADComponent) componentStack.getItem();
			EnumCADComponent componentType = component.getComponentType(componentStack);
			String name = TAG_COMPONENT_PREFIX + componentType.name();

			CompoundNBT cmp = new CompoundNBT();
			componentStack.writeToNBT(cmp);
			ItemNBTHelper.setCompound(stack, name, cmp);
		}
	}

	/**
	 * Sets the component in this slot for the CAD.
	 */
	default void setCADComponent(ItemStack stack, ItemStack component) {
		setComponent(stack, component);
	}

	/**
	 * Gets the component used for this CAD in the given slot.
	 */
	ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type);

	/**
	 * Gets the value of a given CAD stat.
	 */
	int getStatValue(ItemStack stack, EnumCADStat stat);

	/**
	 * Gets how much Psi is stored in this CAD's battery.
	 */
	int getStoredPsi(ItemStack stack);

	/**
	 * Has the CAD regen psi equal to the amount passed in. Will never go above
	 * the value of the CAD's OVERFLOW stat.
	 */
	void regenPsi(ItemStack stack, int psi);

	/**
	 * Consumes psi from the CAD's battery equal to the amount passed in. Returns
	 * the remainder that couldn't be consumed. Used to prevent damage.
	 */
	int consumePsi(ItemStack stack, int psi);

	/**
	 * Gets how many vectors this CAD can store in memory.
	 */
	int getMemorySize(ItemStack stack);
	
	void setStoredVector(ItemStack stack, int memorySlot, Vector3 vec) throws SpellRuntimeException;
	
	Vector3 getStoredVector(ItemStack stack, int memorySlot) throws SpellRuntimeException;

	int getTime(ItemStack stack);

	void incrementTime(ItemStack stack);
	
	/**
	 * Gets the color of the spells projected by this CAD. Usually just goes back
	 * to ICADColorizer.getColor().
	 */
	@OnlyIn(Dist.CLIENT)
	int getSpellColor(ItemStack stack);

}
