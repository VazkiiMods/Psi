/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell;

import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.ISpellCache;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.core.handler.ConfigHandler;

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
			return size() > ConfigHandler.COMMON.spellCacheSize.get();
		}

	};

	@Override
	public CompiledSpell getCompiledSpell(Spell spell) {
		if(map.containsKey(spell.uuid)) {
			return map.get(spell.uuid);
		}

		Optional<CompiledSpell> result = new SpellCompiler().compile(spell).left();
		return result.map(compSpell -> {
			map.put(spell.uuid, compSpell);
			return compSpell;
		}).orElse(null);
	}

}
