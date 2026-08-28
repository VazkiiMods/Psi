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

import vazkii.psi.api.cad.CADStatEvent;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.item.base.ModCADComponents;

public class DefaultStats {

	public static void registerStats() {
		registerAssemblyStats();
		registerCoreStats();
		registerSocketStats();
		registerBatteryStats();
	}

	public static void registerAssemblyStats() {
		//Iron
		ItemCADComponent.addStatToStack(ModCADComponents.cadAssemblyIron.get(), EnumCADStat.EFFICIENCY, 70);
		ItemCADComponent.addStatToStack(ModCADComponents.cadAssemblyIron.get(), EnumCADStat.POTENCY, 100);

		// Gold
		ItemCADComponent.addStatToStack(ModCADComponents.cadAssemblyGold.get(), EnumCADStat.EFFICIENCY, 75);
		ItemCADComponent.addStatToStack(ModCADComponents.cadAssemblyGold.get(), EnumCADStat.POTENCY, 175);

		// Psimetal
		ItemCADComponent.addStatToStack(ModCADComponents.cadAssemblyPsimetal.get(), EnumCADStat.EFFICIENCY, 85);
		ItemCADComponent.addStatToStack(ModCADComponents.cadAssemblyPsimetal.get(), EnumCADStat.POTENCY, 250);

		// Ebony Psimetal
		ItemCADComponent.addStatToStack(ModCADComponents.cadAssemblyEbony.get(), EnumCADStat.EFFICIENCY, 90);
		ItemCADComponent.addStatToStack(ModCADComponents.cadAssemblyEbony.get(), EnumCADStat.POTENCY, 350);

		// Ivory Psimetal
		ItemCADComponent.addStatToStack(ModCADComponents.cadAssemblyIvory.get(), EnumCADStat.EFFICIENCY, 95);
		ItemCADComponent.addStatToStack(ModCADComponents.cadAssemblyIvory.get(), EnumCADStat.POTENCY, 320);

		// Creative
		ItemCADComponent.addStatToStack(ModCADComponents.cadAssemblyCreative.get(), EnumCADStat.EFFICIENCY, -1);
		ItemCADComponent.addStatToStack(ModCADComponents.cadAssemblyCreative.get(), EnumCADStat.POTENCY, -1);
	}

	public static void registerCoreStats() {
		// Basic
		ItemCADComponent.addStatToStack(ModCADComponents.cadCoreBasic.get(), EnumCADStat.COMPLEXITY, 14);
		ItemCADComponent.addStatToStack(ModCADComponents.cadCoreBasic.get(), EnumCADStat.PROJECTION, 1);

		// Overclocked
		ItemCADComponent.addStatToStack(ModCADComponents.cadCoreOverclocked.get(), EnumCADStat.COMPLEXITY, 24);
		ItemCADComponent.addStatToStack(ModCADComponents.cadCoreOverclocked.get(), EnumCADStat.PROJECTION, 3);

		// Conductive
		ItemCADComponent.addStatToStack(ModCADComponents.cadCoreConductive.get(), EnumCADStat.COMPLEXITY, 20);
		ItemCADComponent.addStatToStack(ModCADComponents.cadCoreConductive.get(), EnumCADStat.PROJECTION, 4);

		// Hyperclocked
		ItemCADComponent.addStatToStack(ModCADComponents.cadCoreHyperClocked.get(), EnumCADStat.COMPLEXITY, 36);
		ItemCADComponent.addStatToStack(ModCADComponents.cadCoreHyperClocked.get(), EnumCADStat.PROJECTION, 6);

		// Radiative
		ItemCADComponent.addStatToStack(ModCADComponents.cadCoreRadiative.get(), EnumCADStat.COMPLEXITY, 30);
		ItemCADComponent.addStatToStack(ModCADComponents.cadCoreRadiative.get(), EnumCADStat.PROJECTION, 7);
	}

	public static void registerSocketStats() {
		//Basic
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketBasic.get(), EnumCADStat.BANDWIDTH, 5);
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketBasic.get(), EnumCADStat.SOCKETS, 4);
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketBasic.get(), EnumCADStat.SAVED_VECTORS, 7);

		// Signaling
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketSignaling.get(), EnumCADStat.BANDWIDTH, 7);
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketSignaling.get(), EnumCADStat.SOCKETS, 6);
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketSignaling.get(), EnumCADStat.SAVED_VECTORS, 14);

		// Large
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketLarge.get(), EnumCADStat.BANDWIDTH, 6);
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketLarge.get(), EnumCADStat.SOCKETS, 8);
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketLarge.get(), EnumCADStat.SAVED_VECTORS, 14);

		// Transmissive
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketTransmissive.get(), EnumCADStat.BANDWIDTH, 9);
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketTransmissive.get(), EnumCADStat.SOCKETS, 10);
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketTransmissive.get(), EnumCADStat.SAVED_VECTORS, 18);

		// Huge
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketHuge.get(), EnumCADStat.BANDWIDTH, 8);
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketHuge.get(), EnumCADStat.SOCKETS, 12);
		ItemCADComponent.addStatToStack(ModCADComponents.cadSocketHuge.get(), EnumCADStat.SAVED_VECTORS, 21);
	}

	public static void registerBatteryStats() {
		// Basic
		ItemCADComponent.addStatToStack(ModCADComponents.cadBatteryBasic.get(), EnumCADStat.OVERFLOW, 100);

		// Extended
		ItemCADComponent.addStatToStack(ModCADComponents.cadBatteryExtended.get(), EnumCADStat.OVERFLOW, 200);

		// Ultradense
		ItemCADComponent.addStatToStack(ModCADComponents.cadBatteryUltradense.get(), EnumCADStat.OVERFLOW, 400);
	}

	public static void modifyCreativeAssemblyStats(CADStatEvent event) {
		ItemStack cad = event.getCad();
		ICAD cadItem = (ICAD) cad.getItem();
		ItemStack assembly = cadItem.getComponentInSlot(cad, EnumCADComponent.ASSEMBLY);
		if(!assembly.isEmpty() && assembly.getItem() == ModCADComponents.cadAssemblyCreative.get()) {
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
