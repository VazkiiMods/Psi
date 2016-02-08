/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [17/01/2016, 15:14:30 (GMT)]
 */
package vazkii.psi.api.spell;

import vazkii.psi.api.cad.EnumCADStat;

/**
 * An Enum defining all spell stats and the CAD stats to compare against.
 */
public enum EnumSpellStat {

	COMPLEXITY(EnumCADStat.COMPLEXITY),
	POTENCY(EnumCADStat.POTENCY),
	COST(null),
	PROJECTION(EnumCADStat.PROJECTION),
	BANDWIDTH(EnumCADStat.BANDWIDTH);
	
	private EnumSpellStat(EnumCADStat target) {
		this.target = target;
	}
	
	private EnumCADStat target;

	public EnumCADStat getTarget() {
		return target;
	}
	
	public String getName() {
		return "psi.spellstat." + name().toLowerCase();
	}
	
	public String getDesc() {
		return getName() + ".desc";
	}
	
}
