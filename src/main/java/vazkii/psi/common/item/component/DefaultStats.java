/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.component;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.CADStatEvent;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.item.base.ModItems;

@EventBusSubscriber(modid = PsiAPI.MOD_ID)
public class DefaultStats {

	public static void registerStats() {
		registerAssemblyStats();
		registerCoreStats();
		registerSocketStats();
		registerBatteryStats();
	}

	public static void registerAssemblyStats() {
		//Iron
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyIron.get(), EnumCADStat.EFFICIENCY, 70);
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyIron.get(), EnumCADStat.POTENCY, 100);

		// Gold
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyGold.get(), EnumCADStat.EFFICIENCY, 75);
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyGold.get(), EnumCADStat.POTENCY, 175);

		// Psimetal
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyPsimetal.get(), EnumCADStat.EFFICIENCY, 85);
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyPsimetal.get(), EnumCADStat.POTENCY, 250);

		// Ebony Psimetal
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyEbony.get(), EnumCADStat.EFFICIENCY, 90);
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyEbony.get(), EnumCADStat.POTENCY, 350);

		// Ivory Psimetal
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyIvory.get(), EnumCADStat.EFFICIENCY, 95);
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyIvory.get(), EnumCADStat.POTENCY, 320);

		// Creative
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyCreative.get(), EnumCADStat.EFFICIENCY, -1);
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyCreative.get(), EnumCADStat.POTENCY, -1);
	}

	public static void registerCoreStats() {
		// Basic
		ItemCADComponent.addStatToStack(ModItems.cadCoreBasic.get(), EnumCADStat.COMPLEXITY, 14);
		ItemCADComponent.addStatToStack(ModItems.cadCoreBasic.get(), EnumCADStat.PROJECTION, 1);

		// Overclocked
		ItemCADComponent.addStatToStack(ModItems.cadCoreOverclocked.get(), EnumCADStat.COMPLEXITY, 24);
		ItemCADComponent.addStatToStack(ModItems.cadCoreOverclocked.get(), EnumCADStat.PROJECTION, 3);

		// Conductive
		ItemCADComponent.addStatToStack(ModItems.cadCoreConductive.get(), EnumCADStat.COMPLEXITY, 20);
		ItemCADComponent.addStatToStack(ModItems.cadCoreConductive.get(), EnumCADStat.PROJECTION, 4);

		// Hyperclocked
		ItemCADComponent.addStatToStack(ModItems.cadCoreHyperClocked.get(), EnumCADStat.COMPLEXITY, 36);
		ItemCADComponent.addStatToStack(ModItems.cadCoreHyperClocked.get(), EnumCADStat.PROJECTION, 6);

		// Radiative
		ItemCADComponent.addStatToStack(ModItems.cadCoreRadiative.get(), EnumCADStat.COMPLEXITY, 30);
		ItemCADComponent.addStatToStack(ModItems.cadCoreRadiative.get(), EnumCADStat.PROJECTION, 7);
	}

	public static void registerSocketStats() {
		//Basic
		ItemCADComponent.addStatToStack(ModItems.cadSocketBasic.get(), EnumCADStat.BANDWIDTH, 5);
		ItemCADComponent.addStatToStack(ModItems.cadSocketBasic.get(), EnumCADStat.SOCKETS, 4);
		ItemCADComponent.addStatToStack(ModItems.cadSocketBasic.get(), EnumCADStat.SAVED_VECTORS, 7);

		// Signaling
		ItemCADComponent.addStatToStack(ModItems.cadSocketSignaling.get(), EnumCADStat.BANDWIDTH, 7);
		ItemCADComponent.addStatToStack(ModItems.cadSocketSignaling.get(), EnumCADStat.SOCKETS, 6);
		ItemCADComponent.addStatToStack(ModItems.cadSocketSignaling.get(), EnumCADStat.SAVED_VECTORS, 14);

		// Large
		ItemCADComponent.addStatToStack(ModItems.cadSocketLarge.get(), EnumCADStat.BANDWIDTH, 6);
		ItemCADComponent.addStatToStack(ModItems.cadSocketLarge.get(), EnumCADStat.SOCKETS, 8);
		ItemCADComponent.addStatToStack(ModItems.cadSocketLarge.get(), EnumCADStat.SAVED_VECTORS, 14);

		// Transmissive
		ItemCADComponent.addStatToStack(ModItems.cadSocketTransmissive.get(), EnumCADStat.BANDWIDTH, 9);
		ItemCADComponent.addStatToStack(ModItems.cadSocketTransmissive.get(), EnumCADStat.SOCKETS, 10);
		ItemCADComponent.addStatToStack(ModItems.cadSocketTransmissive.get(), EnumCADStat.SAVED_VECTORS, 18);

		// Huge
		ItemCADComponent.addStatToStack(ModItems.cadSocketHuge.get(), EnumCADStat.BANDWIDTH, 8);
		ItemCADComponent.addStatToStack(ModItems.cadSocketHuge.get(), EnumCADStat.SOCKETS, 12);
		ItemCADComponent.addStatToStack(ModItems.cadSocketHuge.get(), EnumCADStat.SAVED_VECTORS, 21);
	}

	public static void registerBatteryStats() {
		// Basic
		ItemCADComponent.addStatToStack(ModItems.cadBatteryBasic.get(), EnumCADStat.OVERFLOW, 100);

		// Extended
		ItemCADComponent.addStatToStack(ModItems.cadBatteryExtended.get(), EnumCADStat.OVERFLOW, 200);

		// Ultradense
		ItemCADComponent.addStatToStack(ModItems.cadBatteryUltradense.get(), EnumCADStat.OVERFLOW, 400);
	}

	@SubscribeEvent
	public static void modifyCreativeAssemblyStats(CADStatEvent event) {
		ItemStack cad = event.getCad();
		ICAD cadItem = (ICAD) cad.getItem();
		ItemStack assembly = cadItem.getComponentInSlot(cad, EnumCADComponent.ASSEMBLY);
		if(!assembly.isEmpty() && assembly.getItem() == ModItems.cadAssemblyCreative.get()) {
			switch(event.getStat()) {
			case BANDWIDTH:
				event.setStatValue(9);
				break;
			case SOCKETS:
				event.setStatValue(12);
				break;
			default:
				event.setStatValue(-1);
				break;
			}
		}
	}
}
