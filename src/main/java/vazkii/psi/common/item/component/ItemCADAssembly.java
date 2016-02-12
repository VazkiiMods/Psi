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

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADAssembly;
import vazkii.psi.client.core.handler.ModelHandler;
import vazkii.psi.common.item.base.IExtraVariantHolder;
import vazkii.psi.common.lib.LibItemNames;

public class ItemCADAssembly extends ItemCADComponent implements ICADAssembly, IExtraVariantHolder {

	public static final String[] VARIANTS = {
			"cadAssemblyIron",
			"cadAssemblyGold",
			"cadAssemblyPsimetal",
			"cadAssemblyEbonyPsimetal",
			"cadAssemblyIvoryPsimetal",
			"cadAssemblyCreative"
	};

	public static final String[] CAD_MODELS = {
			"cadIron",
			"cadGold",
			"cadPsimetal",
			"cadEbonyPsimetal",
			"cadIvoryPsimetal",
			"cadCreative"
	};

	public ItemCADAssembly() {
		super(LibItemNames.CAD_ASSEMBLY, VARIANTS);
	}

	@Override
	public void registerStats() {
		// Iron
		addStat(EnumCADStat.EFFICIENCY, 0, 70);
		addStat(EnumCADStat.POTENCY, 0, 100);

		// Gold
		addStat(EnumCADStat.EFFICIENCY, 1, 65);
		addStat(EnumCADStat.POTENCY, 1, 150);

		// Psimetal
		addStat(EnumCADStat.EFFICIENCY, 2, 80);
		addStat(EnumCADStat.POTENCY, 2, 250);

		// Ebony Psimetal
		addStat(EnumCADStat.EFFICIENCY, 3, 90);
		addStat(EnumCADStat.POTENCY, 3, 350);

		// Ivory Psimetal
		addStat(EnumCADStat.EFFICIENCY, 4, 95);
		addStat(EnumCADStat.POTENCY, 4, 320);

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
