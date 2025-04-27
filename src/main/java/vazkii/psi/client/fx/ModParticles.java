/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.fx;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.lib.LibMisc;

// https://github.com/Vazkii/Botania/blob/1.15/src/main/java/vazkii/botania/client/fx/ModParticles.java
public class ModParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, PsiAPI.MOD_ID);
	public static DeferredHolder<ParticleType<?>, WispParticleData.Type> WISP = PARTICLE_TYPES.register("wisp", () -> new WispParticleData.Type(false));
	public static DeferredHolder<ParticleType<?>, SparkleParticleData.Type> SPARKLE = PARTICLE_TYPES.register("sparkle", () -> new SparkleParticleData.Type(false));

	@EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
	public static class FactoryHandler {

		@SubscribeEvent
		public static void registerFactories(RegisterParticleProvidersEvent evt) {
			evt.registerSpriteSet(ModParticles.WISP.get(), FXWisp.Factory::new);
			evt.registerSpriteSet(ModParticles.SPARKLE.get(), FXSparkle.Factory::new);
		}
	}
}
