/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import java.util.Locale;

/**
 * An Enum defining all CAD stats and what Components provide them.
 */
public enum EnumCADStat {

	EFFICIENCY(EnumCADComponent.ASSEMBLY),
	POTENCY(EnumCADComponent.ASSEMBLY),
	COMPLEXITY(EnumCADComponent.CORE),
	PROJECTION(EnumCADComponent.CORE),
	BANDWIDTH(EnumCADComponent.SOCKET),
	SOCKETS(EnumCADComponent.SOCKET),
	SAVED_VECTORS(EnumCADComponent.SOCKET),
	OVERFLOW(EnumCADComponent.BATTERY);

	private final EnumCADComponent source;

	EnumCADStat(EnumCADComponent source) {
		this.source = source;
	}

	public EnumCADComponent getSourceType() {
		return source;
	}

	public String getName() {
		return "psi.cadstat." + name().toLowerCase(Locale.ROOT);
	}

}
