/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [09/01/2016, 22:51:28 (GMT)]
 */
package vazkii.psi.common.block.base;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import vazkii.psi.common.item.base.IVariantHolder;

public interface IPsiBlock extends IVariantHolder {

	public String getBareName();
	
	public EnumRarity getBlockRarity(ItemStack stack);
	
}
