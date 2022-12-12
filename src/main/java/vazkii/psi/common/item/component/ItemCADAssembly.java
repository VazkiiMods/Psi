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

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import vazkii.arl.interf.IExtraVariantHolder;
import vazkii.arl.util.ModelHandler;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADAssembly;
import vazkii.psi.common.lib.LibItemNames;

public class ItemCADAssembly extends ItemCADComponent implements ICADAssembly, IExtraVariantHolder {

	public static final String[] VARIANTS = {
			"cad_assembly_iron",
			"cad_assembly_gold",
			"cad_assembly_psimetal",
			"cad_assembly_ebony_psimetal",
			"cad_assembly_ivory_psimetal",
			"cad_assembly_creative"
	};

	public static final String[] CAD_MODELS = {
			"cad_iron",
			"cad_gold",
			"cad_psimetal",
			"cad_ebony_psimetal",
			"cad_ivory_psimetal",
			"cad_creative"
	};

	public ItemCADAssembly() {
		super(LibItemNames.CAD_ASSEMBLY, VARIANTS);
	}

	@Override
	public void registerStats() {
		//Increased the potency for CAD's 

		// Iron
		addStat(EnumCADStat.EFFICIENCY, 0, 70);
		addStat(EnumCADStat.POTENCY, 0, 1000);

		// Gold
		addStat(EnumCADStat.EFFICIENCY, 1, 65);
		addStat(EnumCADStat.POTENCY, 1, 1500);

		// Psimetal
		addStat(EnumCADStat.EFFICIENCY, 2, 80);
		addStat(EnumCADStat.POTENCY, 2, 2500);

		// Ebony Psimetal
		addStat(EnumCADStat.EFFICIENCY, 3, 85);
		addStat(EnumCADStat.POTENCY, 3, 4200);

		// Ivory Psimetal
		addStat(EnumCADStat.EFFICIENCY, 4, 95);
		addStat(EnumCADStat.POTENCY, 4, 3200);

		// Creative
		addStat(EnumCADStat.EFFICIENCY, 5, -1);
		addStat(EnumCADStat.POTENCY, 5, -1);
	}

	@Override
	public EnumCADComponent getComponentType(ItemStack stack) {
		return EnumCADComponent.ASSEMBLY;
	}

	@Override
	public String[] getExtraVariants() {
		return CAD_MODELS;
	}

	@Override
	public ModelResourceLocation getCADModel(ItemStack stack, ItemStack cad) {
		return ModelHandler.resourceLocations.get(CAD_MODELS[Math.min(CAD_MODELS.length - 1, stack.getItemDamage())]);
	}

}
