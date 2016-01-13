/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [13/01/2016, 12:30:38 (GMT)]
 */
package vazkii.psi.api.cad;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * An item that implements this works as CAD colorizer, by which it can change
 * the CAD's spell color.
 */
public interface ICADColorizer extends ICADComponent {

	public static final int DEFAULT_SPELL_COLOR = 0x13C5FF;
	
	/**
	 * Gets the color of the spells projected by the CAD that has
	 * this colorizer.
	 */
	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack);

	@Override
	public default EnumCADComponent getComponentType(ItemStack stack) {
		return EnumCADComponent.DYE;
	}
}
