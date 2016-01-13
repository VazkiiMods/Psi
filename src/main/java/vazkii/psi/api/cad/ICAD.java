/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [09/01/2016, 17:08:06 (GMT)]
 */
package vazkii.psi.api.cad;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICAD {

	public ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type);
	
	public int getStatValue(ItemStack stack, EnumCADStat stat);
	
	public int getStoredPsi(ItemStack stack);
	
	public void regenPsi(ItemStack stack, int psi);
	
	public int consumePsi(ItemStack stack, int psi);
	
	@SideOnly(Side.CLIENT)
	public int getSpellColor(ItemStack stack);
	
}
