/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [09/01/2016, 01:04:14 (GMT)]
 */
package vazkii.psi.api.cad;

import net.minecraft.util.StatCollector;

public enum EnumCADStat {

	EFFICIENCY(EnumCADComponent.ASSEMBLY),
	POTENCY(EnumCADComponent.ASSEMBLY),
	COMPLEXITY(EnumCADComponent.CORE),
	PROJECTION(EnumCADComponent.CORE),
	BANDWIDTH(EnumCADComponent.SOCKET),
	SOCKETS(EnumCADComponent.SOCKET),
	OVERFLOW(EnumCADComponent.BATTERY);
	
	private EnumCADStat(EnumCADComponent source) {
		this.source = source;
	}
	
	private EnumCADComponent source;
	
	public EnumCADComponent getSourceType() {
		return source;
	}
	
	public String getName() {
		return "psi.cadstat." + name().toLowerCase();
	}
	
}
