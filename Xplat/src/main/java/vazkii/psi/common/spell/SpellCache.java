/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.ISpellCache;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.platform.PsiConfig;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class SpellCache implements ISpellCache {

	public static final SpellCache instance = new SpellCache();

	@SuppressWarnings("serial")
	public static final Map<UUID, CompiledSpell> map = new LinkedHashMap<>() {

		@Override
		protected boolean removeEldestEntry(Map.Entry<UUID, CompiledSpell> eldest) {
			return size() > PsiConfig.common().spellCacheSize();
		}

	};

	@Override
	public CompiledSpell getCompiledSpell(Spell spell, Player player) {
		CompiledSpell cached = map.get(spell.uuid);
		if(cached != null) {
			return allPiecesAvailable(spell, player) ? cached : null;
		}

		Optional<CompiledSpell> result = new SpellCompiler().compile(spell, player.registryAccess(), player).left();
		return result.map(compSpell -> {
			map.put(spell.uuid, compSpell);
			return compSpell;
		}).orElse(null);
	}

	private static boolean allPiecesAvailable(Spell spell, Player player) {
		RegistryAccess registries = player.registryAccess();
		for(SpellPiece[] row : spell.grid.gridData) {
			for(SpellPiece piece : row) {
				if(piece != null && !PsiAPI.isPieceAvailable(player, registries, piece.getRegistryKey())) {
					return false;
				}
			}
		}
		return true;
	}

}
