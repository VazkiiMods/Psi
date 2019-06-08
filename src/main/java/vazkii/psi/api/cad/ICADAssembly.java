/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [05/02/2016, 00:40:08 (GMT)]
 */
package vazkii.psi.api.cad;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;

public interface ICADAssembly {

	default ItemStack createCADStack(ItemStack... components) {
		return PsiAPI.internalHandler.createDefaultCAD(components);
	}

	@SideOnly(Side.CLIENT)
	ModelResourceLocation getCADModel(ItemStack stack, ItemStack cad);

}
