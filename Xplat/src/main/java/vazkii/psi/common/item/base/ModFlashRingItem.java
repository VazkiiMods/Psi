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
import net.minecraft.world.item.Item;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.capability.PsiCapabilities;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.item.ItemFlashRing;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.platform.PsiLookups;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.List;

public final class ModFlashRingItem {
	public static final RegistryEntry<ItemFlashRing> FLASH_RING = PsiRegistries.register(
			BuiltInRegistries.ITEM, PsiAPI.location(LibItemNames.FLASH_RING),
			() -> new ItemFlashRing(new Item.Properties()));

	private ModFlashRingItem() {}

	public static void register() {
		PsiLookups.registerItem(
				PsiCapabilities.SPELL_ACCEPTOR, ISpellAcceptor.class,
				ItemFlashRing.SpellAcceptor::new, List.of(FLASH_RING));
	}
}
