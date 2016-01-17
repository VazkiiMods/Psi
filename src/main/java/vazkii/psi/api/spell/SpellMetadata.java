/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [17/01/2016, 15:12:28 (GMT)]
 */
package vazkii.psi.api.spell;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;

public final class SpellMetadata {

	public Map<EnumSpellStat, Integer> stats = new EnumMap(EnumSpellStat.class);
	
	public SpellMetadata() {
		for(EnumSpellStat stat : EnumSpellStat.class.getEnumConstants())
			stats.put(stat, 0);
	}
	
	public void addStat(EnumSpellStat stat, int val) {
		int curr = stats.get(stat);
		setStat(stat, val + curr);
	}
	
	public void setStat(EnumSpellStat stat, int val) {
		stats.put(stat, val);
	}
	
	public boolean evaluateAgainst(ItemStack stack) {
		if(stack == null || !(stack.getItem() instanceof ICAD))
			return false;
		
		ICAD cad = (ICAD) stack.getItem();
		for(EnumSpellStat stat : stats.keySet()) {
			EnumCADStat cadStat = stat.getTarget();
			if(cadStat == null)
				continue;
			
			int statVal = stats.get(stat);
			int cadVal = cad.getStatValue(stack, cadStat);
			if(cadVal < statVal)
				return false;
		}
		
		return true;
	}
	
}
