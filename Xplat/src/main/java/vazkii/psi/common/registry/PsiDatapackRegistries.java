/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.registry;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.CADComponentDefinition;
import vazkii.psi.api.cad.CADComponentLookup;
import vazkii.psi.api.spell.SpellPieceGroup;
import vazkii.psi.api.spell.SpellPieceSettings;

public final class PsiDatapackRegistries {

	private PsiDatapackRegistries() {}

	public static void register() {
		PsiRegistries.registerSyncedDatapackRegistry(CADComponentLookup.REGISTRY, CADComponentDefinition.CODEC);
		PsiRegistries.registerSyncedDatapackRegistry(PsiAPI.SPELL_PIECE_GROUP_REGISTRY_KEY, SpellPieceGroup.CODEC);
		PsiRegistries.registerSyncedDatapackRegistry(PsiAPI.SPELL_PIECE_SETTINGS_REGISTRY_KEY, SpellPieceSettings.CODEC);
	}

}
