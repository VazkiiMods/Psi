/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.CADComponentDefinition;
import vazkii.psi.api.cad.CADComponentLookup;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.common.item.base.ModCADComponents;
import vazkii.psi.common.item.component.ItemCADAssembly;
import vazkii.psi.common.item.component.ItemCADColorizer;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class PsiDataRegistries {

	private PsiDataRegistries() {}

	private static final Set<RegistryEntry<? extends ItemCADColorizer>> DYNAMIC_COLORIZERS = Set.of(
			ModCADComponents.cadColorizerRainbow, ModCADComponents.cadColorizerPsi);

	public static RegistrySetBuilder configure(RegistrySetBuilder builder) {
		return builder
				.add(Registries.DAMAGE_TYPE, PsiDataRegistries::bootstrapDamageTypes)
				.add(PsiAPI.SPELL_PIECE_GROUP_REGISTRY_KEY, PsiSpellPieceGroups::bootstrap)
				.add(CADComponentLookup.REGISTRY, PsiDataRegistries::bootstrapCADComponents);
	}

	public static void bootstrapCADComponents(BootstrapContext<CADComponentDefinition> context) {
		assembly(context, ModCADComponents.cadAssemblyIron, LibItemNames.CAD_IRON, 70, 100);
		assembly(context, ModCADComponents.cadAssemblyGold, LibItemNames.CAD_GOLD, 75, 175);
		assembly(context, ModCADComponents.cadAssemblyPsimetal, LibItemNames.CAD_PSIMETAL, 85, 250);
		assembly(context, ModCADComponents.cadAssemblyEbony, LibItemNames.CAD_EBONY_PSIMETAL, 90, 350);
		assembly(context, ModCADComponents.cadAssemblyIvory, LibItemNames.CAD_IVORY_PSIMETAL, 95, 320);
		assembly(context, ModCADComponents.cadAssemblyCreative, LibItemNames.CAD_CREATIVE, -1, -1);

		core(context, ModCADComponents.cadCoreBasic, 14, 1);
		core(context, ModCADComponents.cadCoreOverclocked, 24, 3);
		core(context, ModCADComponents.cadCoreConductive, 20, 4);
		core(context, ModCADComponents.cadCoreHyperClocked, 36, 6);
		core(context, ModCADComponents.cadCoreRadiative, 30, 7);

		socket(context, ModCADComponents.cadSocketBasic, 5, 4, 7);
		socket(context, ModCADComponents.cadSocketSignaling, 7, 6, 14);
		socket(context, ModCADComponents.cadSocketLarge, 6, 8, 14);
		socket(context, ModCADComponents.cadSocketTransmissive, 9, 10, 18);
		socket(context, ModCADComponents.cadSocketHuge, 8, 12, 21);

		battery(context, ModCADComponents.cadBatteryBasic, 100);
		battery(context, ModCADComponents.cadBatteryExtended, 200);
		battery(context, ModCADComponents.cadBatteryUltradense, 400);

		for(RegistryEntry<? extends ItemCADColorizer> colorizer : ModCADComponents.colorizers) {
			if(DYNAMIC_COLORIZERS.contains(colorizer)) {
				continue;
			}
			int color = colorizer.get().getColor(new ItemStack(colorizer.get()));
			register(context, colorizer, CADComponentDefinition.dye(color));
		}
	}

	private static void assembly(BootstrapContext<CADComponentDefinition> context, RegistryEntry<ItemCADAssembly> entry,
			String model, int efficiency, int potency) {
		Map<EnumCADStat, Integer> stats = Map.of(EnumCADStat.EFFICIENCY, efficiency, EnumCADStat.POTENCY, potency);
		register(context, entry, CADComponentDefinition.assembly(stats, Optional.of(PsiAPI.location("item/" + model))));
	}

	private static void core(BootstrapContext<CADComponentDefinition> context, RegistryEntry<?> entry,
			int complexity, int projection) {
		register(context, entry, CADComponentDefinition.core(
				Map.of(EnumCADStat.COMPLEXITY, complexity, EnumCADStat.PROJECTION, projection)));
	}

	private static void socket(BootstrapContext<CADComponentDefinition> context, RegistryEntry<?> entry,
			int bandwidth, int sockets, int savedVectors) {
		register(context, entry, CADComponentDefinition.socket(Map.of(EnumCADStat.BANDWIDTH, bandwidth,
				EnumCADStat.SOCKETS, sockets, EnumCADStat.SAVED_VECTORS, savedVectors)));
	}

	private static void battery(BootstrapContext<CADComponentDefinition> context, RegistryEntry<?> entry, int overflow) {
		register(context, entry, CADComponentDefinition.battery(Map.of(EnumCADStat.OVERFLOW, overflow)));
	}

	private static void register(BootstrapContext<CADComponentDefinition> context, RegistryEntry<?> entry,
			CADComponentDefinition definition) {
		context.register(ResourceKey.create(CADComponentLookup.REGISTRY, entry.id()), definition);
	}

	public static void bootstrapDamageTypes(BootstrapContext<DamageType> context) {
		context.register(LibResources.PSI_OVERLOAD,
				new DamageType("psi_overload", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0F));
	}
}
