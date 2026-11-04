/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.fx;

import net.minecraft.core.registries.BuiltInRegistries;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

public final class ModParticles {
	public static final RegistryEntry<WispParticleData.Type> WISP = PsiRegistries.register(
			BuiltInRegistries.PARTICLE_TYPE, PsiAPI.location("wisp"), WispParticleData.Type::new);
	public static final RegistryEntry<SparkleParticleData.Type> SPARKLE = PsiRegistries.register(
			BuiltInRegistries.PARTICLE_TYPE, PsiAPI.location("sparkle"), SparkleParticleData.Type::new);

	private ModParticles() {}

	public static void register() {}
}
