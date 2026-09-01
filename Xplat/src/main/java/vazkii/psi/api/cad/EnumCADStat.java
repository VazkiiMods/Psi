/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

/**
 * An Enum defining all CAD stats and what Components provide them.
 */
public enum EnumCADStat implements StringRepresentable {

	EFFICIENCY(EnumCADComponent.ASSEMBLY),
	POTENCY(EnumCADComponent.ASSEMBLY),
	COMPLEXITY(EnumCADComponent.CORE),
	PROJECTION(EnumCADComponent.CORE),
	BANDWIDTH(EnumCADComponent.SOCKET),
	SOCKETS(EnumCADComponent.SOCKET),
	SAVED_VECTORS(EnumCADComponent.SOCKET),
	OVERFLOW(EnumCADComponent.BATTERY);

	public static final Codec<EnumCADStat> CODEC = StringRepresentable.fromEnum(EnumCADStat::values);

	private final EnumCADComponent source;

	EnumCADStat(EnumCADComponent source) {
		this.source = source;
	}

	public EnumCADComponent getSourceType() {
		return source;
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public String getName() {
		return "psi.cadstat." + getSerializedName();
	}

}
