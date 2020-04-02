/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/01/2016, 21:53:27 (GMT)]
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
