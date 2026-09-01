/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.PsiAPI;

import java.util.List;

public interface ICADAssembly {

	default ItemStack createCADStack(HolderLookup.Provider registries, ItemStack stack, List<ItemStack> allComponents) {
		return PsiAPI.internalHandler.createDefaultCAD(registries, allComponents);
	}

	/**
	 * @return Path to a model json file, e.g. <code>psi:item/cad_iron</code>
	 */
	ResourceLocation getCADModel(ItemStack stack, ItemStack cad);

	ResourceLocation getCadTexture(ItemStack stack, ItemStack cad);

}
