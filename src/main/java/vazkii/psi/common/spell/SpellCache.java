/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [22/01/2016, 16:06:41 (GMT)]
 */
package vazkii.psi.common.spell;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.ISpellCache;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.core.handler.ConfigHandler;

public final class SpellCache implements ISpellCache {

	public static final SpellCache instance = new SpellCache();

	public static final Map<UUID, CompiledSpell> map = new LinkedHashMap<UUID, CompiledSpell>() {
		
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > ConfigHandler.spellCacheSize;
        }
        
    };
	
	@Override
	public CompiledSpell getCompiledSpell(Spell spell) {
		if(map.containsKey(spell.uuid))
			return map.get(spell.uuid);
		
		SpellCompiler compiler = new SpellCompiler(spell);
		if(!compiler.isErrored()) {
			map.put(spell.uuid, compiler.getCompiledSpell());
			return compiler.getCompiledSpell();
		}
		
		return null;
	}
	
}
