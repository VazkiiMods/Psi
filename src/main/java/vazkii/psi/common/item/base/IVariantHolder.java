/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [08/01/2016, 23:42:16 (GMT)]
 */
package vazkii.psi.common.item.base;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IVariantHolder {

	public String[] getVariants();

	@SideOnly(Side.CLIENT)
	public ItemMeshDefinition getCustomMeshDefinition();
	
}
