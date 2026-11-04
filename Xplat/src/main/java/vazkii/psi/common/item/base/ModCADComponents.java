/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.base;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.item.component.ItemCADAssembly;
import vazkii.psi.common.item.component.ItemCADBattery;
import vazkii.psi.common.item.component.ItemCADColorizer;
import vazkii.psi.common.item.component.ItemCADColorizerEmpty;
import vazkii.psi.common.item.component.ItemCADColorizerPsi;
import vazkii.psi.common.item.component.ItemCADColorizerRainbow;
import vazkii.psi.common.item.component.ItemCADCore;
import vazkii.psi.common.item.component.ItemCADSocket;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.List;
import java.util.function.Supplier;

public final class ModCADComponents {

	public static final RegistryEntry<ItemCADAssembly> cadAssemblyIron = assembly(LibItemNames.CAD_ASSEMBLY_IRON, LibItemNames.CAD_IRON);
	public static final RegistryEntry<ItemCADAssembly> cadAssemblyGold = assembly(LibItemNames.CAD_ASSEMBLY_GOLD, LibItemNames.CAD_GOLD);
	public static final RegistryEntry<ItemCADAssembly> cadAssemblyPsimetal = assembly(LibItemNames.CAD_ASSEMBLY_PSIMETAL, LibItemNames.CAD_PSIMETAL);
	public static final RegistryEntry<ItemCADAssembly> cadAssemblyIvory = assembly(LibItemNames.CAD_ASSEMBLY_IVORY_PSIMETAL, LibItemNames.CAD_IVORY_PSIMETAL);
	public static final RegistryEntry<ItemCADAssembly> cadAssemblyEbony = assembly(LibItemNames.CAD_ASSEMBLY_EBONY_PSIMETAL, LibItemNames.CAD_EBONY_PSIMETAL);
	public static final RegistryEntry<ItemCADAssembly> cadAssemblyCreative = assembly(LibItemNames.CAD_ASSEMBLY_CREATIVE, LibItemNames.CAD_CREATIVE);

	public static final RegistryEntry<ItemCADCore> cadCoreBasic = item(LibItemNames.CAD_CORE_BASIC, () -> new ItemCADCore(properties()));
	public static final RegistryEntry<ItemCADCore> cadCoreOverclocked = item(LibItemNames.CAD_CORE_OVERCLOCKED, () -> new ItemCADCore(properties()));
	public static final RegistryEntry<ItemCADCore> cadCoreConductive = item(LibItemNames.CAD_CORE_CONDUCTIVE, () -> new ItemCADCore(properties()));
	public static final RegistryEntry<ItemCADCore> cadCoreHyperClocked = item(LibItemNames.CAD_CORE_HYPERCLOCKED, () -> new ItemCADCore(properties()));
	public static final RegistryEntry<ItemCADCore> cadCoreRadiative = item(LibItemNames.CAD_CORE_RADIATIVE, () -> new ItemCADCore(properties()));

	public static final RegistryEntry<ItemCADSocket> cadSocketBasic = item(LibItemNames.CAD_SOCKET_BASIC, () -> new ItemCADSocket(properties()));
	public static final RegistryEntry<ItemCADSocket> cadSocketSignaling = item(LibItemNames.CAD_SOCKET_SIGNALING, () -> new ItemCADSocket(properties()));
	public static final RegistryEntry<ItemCADSocket> cadSocketLarge = item(LibItemNames.CAD_SOCKET_LARGE, () -> new ItemCADSocket(properties()));
	public static final RegistryEntry<ItemCADSocket> cadSocketTransmissive = item(LibItemNames.CAD_SOCKET_TRANSMISSIVE, () -> new ItemCADSocket(properties()));
	public static final RegistryEntry<ItemCADSocket> cadSocketHuge = item(LibItemNames.CAD_SOCKET_HUGE, () -> new ItemCADSocket(properties()));

	public static final RegistryEntry<ItemCADBattery> cadBatteryBasic = item(LibItemNames.CAD_BATTERY_BASIC, () -> new ItemCADBattery(properties()));
	public static final RegistryEntry<ItemCADBattery> cadBatteryExtended = item(LibItemNames.CAD_BATTERY_EXTENDED, () -> new ItemCADBattery(properties()));
	public static final RegistryEntry<ItemCADBattery> cadBatteryUltradense = item(LibItemNames.CAD_BATTERY_ULTRADENSE, () -> new ItemCADBattery(properties()));

	public static final RegistryEntry<ItemCADColorizer> cadColorizerWhite = colorizer(LibItemNames.CAD_COLORIZER_WHITE, DyeColor.WHITE);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerOrange = colorizer(LibItemNames.CAD_COLORIZER_ORANGE, DyeColor.ORANGE);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerMagenta = colorizer(LibItemNames.CAD_COLORIZER_MAGENTA, DyeColor.MAGENTA);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerLightBlue = colorizer(LibItemNames.CAD_COLORIZER_LIGHT_BLUE, DyeColor.LIGHT_BLUE);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerYellow = colorizer(LibItemNames.CAD_COLORIZER_YELLOW, DyeColor.YELLOW);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerLime = colorizer(LibItemNames.CAD_COLORIZER_LIME, DyeColor.LIME);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerPink = colorizer(LibItemNames.CAD_COLORIZER_PINK, DyeColor.PINK);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerGray = colorizer(LibItemNames.CAD_COLORIZER_GRAY, DyeColor.GRAY);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerLightGray = colorizer(LibItemNames.CAD_COLORIZER_LIGHT_GRAY, DyeColor.LIGHT_GRAY);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerCyan = colorizer(LibItemNames.CAD_COLORIZER_CYAN, DyeColor.CYAN);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerPurple = colorizer(LibItemNames.CAD_COLORIZER_PURPLE, DyeColor.PURPLE);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerBlue = colorizer(LibItemNames.CAD_COLORIZER_BLUE, DyeColor.BLUE);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerBrown = colorizer(LibItemNames.CAD_COLORIZER_BROWN, DyeColor.BROWN);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerGreen = colorizer(LibItemNames.CAD_COLORIZER_GREEN, DyeColor.GREEN);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerRed = colorizer(LibItemNames.CAD_COLORIZER_RED, DyeColor.RED);
	public static final RegistryEntry<ItemCADColorizer> cadColorizerBlack = colorizer(LibItemNames.CAD_COLORIZER_BLACK, DyeColor.BLACK);
	public static final RegistryEntry<ItemCADColorizerRainbow> cadColorizerRainbow = item(LibItemNames.CAD_COLORIZER_RAINBOW, () -> new ItemCADColorizerRainbow(properties()));
	public static final RegistryEntry<ItemCADColorizerPsi> cadColorizerPsi = item(LibItemNames.CAD_COLORIZER_PSI, () -> new ItemCADColorizerPsi(properties()));
	public static final RegistryEntry<ItemCADColorizerEmpty> cadColorizerEmpty = item(LibItemNames.CAD_COLORIZER_EMPTY, () -> new ItemCADColorizerEmpty(properties()));

	public static final List<RegistryEntry<ItemCADAssembly>> assemblies = List.of(cadAssemblyIron, cadAssemblyGold,
			cadAssemblyPsimetal, cadAssemblyIvory, cadAssemblyEbony, cadAssemblyCreative);
	public static final List<RegistryEntry<ItemCADCore>> cores = List.of(cadCoreBasic, cadCoreOverclocked,
			cadCoreConductive, cadCoreHyperClocked, cadCoreRadiative);
	public static final List<RegistryEntry<ItemCADSocket>> sockets = List.of(cadSocketBasic, cadSocketSignaling,
			cadSocketLarge, cadSocketTransmissive, cadSocketHuge);
	public static final List<RegistryEntry<ItemCADBattery>> batteries = List.of(cadBatteryBasic, cadBatteryExtended,
			cadBatteryUltradense);
	public static final List<RegistryEntry<? extends ItemCADColorizer>> colorizers = List.of(cadColorizerWhite,
			cadColorizerOrange, cadColorizerMagenta, cadColorizerLightBlue, cadColorizerYellow, cadColorizerLime,
			cadColorizerPink, cadColorizerGray, cadColorizerLightGray, cadColorizerCyan, cadColorizerPurple,
			cadColorizerBlue, cadColorizerBrown, cadColorizerGreen, cadColorizerRed, cadColorizerBlack,
			cadColorizerRainbow, cadColorizerPsi, cadColorizerEmpty);

	private ModCADComponents() {}

	private static RegistryEntry<ItemCADAssembly> assembly(String name, String model) {
		return item(name, () -> new ItemCADAssembly(properties(), model));
	}

	private static RegistryEntry<ItemCADColorizer> colorizer(String name, DyeColor color) {
		return item(name, () -> new ItemCADColorizer(properties(), color));
	}

	private static <T extends Item> RegistryEntry<T> item(String name, Supplier<T> factory) {
		return PsiRegistries.register(BuiltInRegistries.ITEM, PsiAPI.location(name), factory);
	}

	private static Item.Properties properties() {
		return new Item.Properties();
	}

	public static void register() {}
}
