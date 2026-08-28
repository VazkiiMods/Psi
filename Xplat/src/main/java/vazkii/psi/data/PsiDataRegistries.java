/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

import vazkii.psi.common.lib.LibResources;

public final class PsiDataRegistries {

	private PsiDataRegistries() {}

	public static RegistrySetBuilder builder() {
		return new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, PsiDataRegistries::bootstrapDamageTypes);
	}

	public static void bootstrapDamageTypes(BootstrapContext<DamageType> context) {
		context.register(LibResources.PSI_OVERLOAD,
				new DamageType("psi_overload", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0F));
	}
}
