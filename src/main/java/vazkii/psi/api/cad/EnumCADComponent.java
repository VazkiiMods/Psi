/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/01/2016, 01:05:44 (GMT)]
 */
package vazkii.psi.api.cad;

/**
 * An Enum defining all types of CAD components.
 */
public enum EnumCADComponent {

	/** If you define an item using this component, it must implement ICADAssembly */
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
