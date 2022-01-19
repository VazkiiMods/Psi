/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.psi.api.PsiAPI;

import java.util.List;

public interface ICADAssembly {

	default ItemStack createCADStack(ItemStack stack, List<ItemStack> allComponents) {
		return PsiAPI.internalHandler.createDefaultCAD(allComponents);
	}

	/**
	 * @return Path to a model json file, e.g. <code>psi:item/cad_iron</code>
	 */
	@OnlyIn(Dist.CLIENT)
	ResourceLocation getCADModel(ItemStack stack, ItemStack cad);

	@OnlyIn(Dist.CLIENT)
	ResourceLocation getCadTexture(ItemStack stack, ItemStack cad);

}
