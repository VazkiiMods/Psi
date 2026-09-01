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
 * An Enum defining all types of CAD components.
 */
public enum EnumCADComponent implements StringRepresentable {

	/**
	 * If you define an item using this component, it must implement ICADAssembly
	 */
	ASSEMBLY,
	CORE,
	SOCKET,
	BATTERY,
	/**
	 * If you define an item using this component, it must implement ICADColorizer
	 */
	DYE;

	public static final Codec<EnumCADComponent> CODEC = StringRepresentable.fromEnum(EnumCADComponent::values);

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public String getName() {
		return "psi.component." + getSerializedName();
	}

}
