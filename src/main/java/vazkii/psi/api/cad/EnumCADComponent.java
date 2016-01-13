/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [09/01/2016, 01:05:44 (GMT)]
 */
package vazkii.psi.api.cad;

import net.minecraft.util.StatCollector;

/**
 * An Enum defining all types of CAD components. 
 */
public enum EnumCADComponent {

	ASSEMBLY,
	CORE,
	SOCKET,
	BATTERY,
	/** If you define an item using this component, it must implement ICADColorizer */
	DYE;
	
	public String getName() {
		return "psi.component." + name().toLowerCase();
	}
	
}
