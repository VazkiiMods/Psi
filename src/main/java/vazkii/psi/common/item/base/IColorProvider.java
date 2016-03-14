/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [14/03/2016, 16:42:38 (GMT)]
 */
package vazkii.psi.common.item.base;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IColorProvider {
	
	// TODO 1.9: actually register these damn things
	
	@SideOnly(Side.CLIENT)
	public IItemColor getColor();

}
