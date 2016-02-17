/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/01/2016, 22:51:28 (GMT)]
 */
package vazkii.psi.common.block.base;

import net.minecraft.block.properties.IProperty;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import vazkii.psi.common.item.base.IVariantHolder;

public interface IPsiBlock extends IVariantHolder, IVariantEnumHolder {

	public String getBareName();

	public IProperty[] getIgnoredProperties();
	
	public EnumRarity getBlockRarity(ItemStack stack);

}
