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

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADData;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.capability.PsiCapabilities;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.core.handler.capability.CADData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.platform.PsiLookups;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.List;

public final class ModCADItem {
	public static final RegistryEntry<ItemCAD> CAD = PsiRegistries.register(
			BuiltInRegistries.ITEM, PsiAPI.location(LibItemNames.CAD),
			() -> new ItemCAD(new net.minecraft.world.item.Item.Properties()));

	private ModCADItem() {}

	public static void register() {
		PsiLookups.registerItem(PsiCapabilities.CAD_DATA, ICADData.class, CADData::new, List.of(CAD));
		PsiLookups.registerItem(PsiCapabilities.SOCKETABLE, ISocketable.class, CADData::new, List.of(CAD));
		PsiLookups.registerItem(PsiCapabilities.PSI_BAR_DISPLAY, IPsiBarDisplay.class, CADData::new, List.of(CAD));
		PsiLookups.registerItem(PsiCapabilities.SPELL_ACCEPTOR, ISpellAcceptor.class, CADData::new, List.of(CAD));
	}
}
