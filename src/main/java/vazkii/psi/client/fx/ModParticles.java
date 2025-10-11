/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.fx;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import vazkii.psi.api.PsiAPI;

// https://github.com/Vazkii/Botania/blob/1.15/src/main/java/vazkii/botania/client/fx/ModParticles.java
public class ModParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, PsiAPI.MOD_ID);
	public static final DeferredHolder<ParticleType<?>, WispParticleData.Type> WISP = PARTICLE_TYPES.register("wisp", WispParticleData.Type::new);
	public static final DeferredHolder<ParticleType<?>, SparkleParticleData.Type> SPARKLE = PARTICLE_TYPES.register("sparkle", SparkleParticleData.Type::new);
}
