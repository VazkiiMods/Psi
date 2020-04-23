/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.component;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADAssembly;
import vazkii.psi.common.lib.LibMisc;

public class ItemCADAssembly extends ItemCADComponent implements ICADAssembly {

	private final String model;

	public ItemCADAssembly(Item.Properties props, String model) {
		super(props);
		this.model = model;
	}

	@Override
	public EnumCADComponent getComponentType(ItemStack stack) {
		return EnumCADComponent.ASSEMBLY;
	}

	@Override
	public ResourceLocation getCADModel(ItemStack stack, ItemStack cad) {
		return new ResourceLocation(LibMisc.MOD_ID, "item/" + model);
	}

	@Override
	public ResourceLocation getCadTexture(ItemStack stack, ItemStack cad) {
		return new ResourceLocation(LibMisc.MOD_ID, model);
	}

}
