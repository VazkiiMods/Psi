/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

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
	OVERFLOW(EnumCADComponent.BATTERY);

	EnumCADStat(EnumCADComponent source) {
		this.source = source;
	}

	private final EnumCADComponent source;

	public EnumCADComponent getSourceType() {
		return source;
	}

	public String getName() {
		return "psi.cadstat." + name().toLowerCase();
	}

}
