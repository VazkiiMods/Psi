/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import java.util.Locale;

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
		return "psi.component." + name().toLowerCase(Locale.ROOT);
	}

}
