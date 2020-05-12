/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.component;

import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.common.item.base.ModItems;

public class DefaultStats {

	public static void registerStats() {
		registerAssemblyStats();
		registerCoreStats();
		registerSocketStats();
		registerBatteryStats();
	}

	public static void registerAssemblyStats() {
		//Iron
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyIron, EnumCADStat.EFFICIENCY, 70);
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyIron, EnumCADStat.POTENCY, 100);

		// Gold
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyGold, EnumCADStat.EFFICIENCY, 75);
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyGold, EnumCADStat.POTENCY, 175);

		// Psimetal
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyPsimetal, EnumCADStat.EFFICIENCY, 85);
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyPsimetal, EnumCADStat.POTENCY, 250);

		// Ebony Psimetal
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyEbony, EnumCADStat.EFFICIENCY, 90);
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyEbony, EnumCADStat.POTENCY, 350);

		// Ivory Psimetal
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyIvory, EnumCADStat.EFFICIENCY, 95);
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyIvory, EnumCADStat.POTENCY, 320);

		// Creative
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyCreative, EnumCADStat.EFFICIENCY, -1);
		ItemCADComponent.addStatToStack(ModItems.cadAssemblyCreative, EnumCADStat.POTENCY, -1);
	}

	public static void registerCoreStats() {
		// Basic
		ItemCADComponent.addStatToStack(ModItems.cadCoreBasic, EnumCADStat.COMPLEXITY, 14);
		ItemCADComponent.addStatToStack(ModItems.cadCoreBasic, EnumCADStat.PROJECTION, 1);

		// Overclocked
		ItemCADComponent.addStatToStack(ModItems.cadCoreOverclocked, EnumCADStat.COMPLEXITY, 24);
		ItemCADComponent.addStatToStack(ModItems.cadCoreOverclocked, EnumCADStat.PROJECTION, 3);

		// Conductive
		ItemCADComponent.addStatToStack(ModItems.cadCoreConductive, EnumCADStat.COMPLEXITY, 20);
		ItemCADComponent.addStatToStack(ModItems.cadCoreConductive, EnumCADStat.PROJECTION, 4);

		// Hyperclocked
		ItemCADComponent.addStatToStack(ModItems.cadCoreHyperClocked, EnumCADStat.COMPLEXITY, 36);
		ItemCADComponent.addStatToStack(ModItems.cadCoreHyperClocked, EnumCADStat.PROJECTION, 6);

		// Radiative
		ItemCADComponent.addStatToStack(ModItems.cadCoreRadiative, EnumCADStat.COMPLEXITY, 30);
		ItemCADComponent.addStatToStack(ModItems.cadCoreRadiative, EnumCADStat.PROJECTION, 7);
	}

	public static void registerSocketStats() {
		//Basic
		ItemCADComponent.addStatToStack(ModItems.cadSocketBasic, EnumCADStat.BANDWIDTH, 5);
		ItemCADComponent.addStatToStack(ModItems.cadSocketBasic, EnumCADStat.SOCKETS, 4);

		// Signaling
		ItemCADComponent.addStatToStack(ModItems.cadSocketSignaling, EnumCADStat.BANDWIDTH, 7);
		ItemCADComponent.addStatToStack(ModItems.cadSocketSignaling, EnumCADStat.SOCKETS, 6);

		// Large
		ItemCADComponent.addStatToStack(ModItems.cadSocketLarge, EnumCADStat.BANDWIDTH, 6);
		ItemCADComponent.addStatToStack(ModItems.cadSocketLarge, EnumCADStat.SOCKETS, 8);

		// Transmissive
		ItemCADComponent.addStatToStack(ModItems.cadSocketTransmissive, EnumCADStat.BANDWIDTH, 9);
		ItemCADComponent.addStatToStack(ModItems.cadSocketTransmissive, EnumCADStat.SOCKETS, 10);

		// Huge
		ItemCADComponent.addStatToStack(ModItems.cadSocketHuge, EnumCADStat.BANDWIDTH, 8);
		ItemCADComponent.addStatToStack(ModItems.cadSocketHuge, EnumCADStat.SOCKETS, 12);
	}

	public static void registerBatteryStats() {
		// Basic
		ItemCADComponent.addStatToStack(ModItems.cadBatteryBasic, EnumCADStat.OVERFLOW, 100);

		// Extended
		ItemCADComponent.addStatToStack(ModItems.cadBatteryExtended, EnumCADStat.OVERFLOW, 200);

		// Ultradense
		ItemCADComponent.addStatToStack(ModItems.cadBatteryUltradense, EnumCADStat.OVERFLOW, 400);
	}
}
