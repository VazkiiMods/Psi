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
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import net.minecraftforge.registries.RegisterEvent;
import vazkii.psi.common.lib.LibMisc;

// https://github.com/Vazkii/Botania/blob/1.15/src/main/java/vazkii/botania/client/fx/ModParticles.java
@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {
	@ObjectHolder(registryName = "particle_type", value = LibMisc.MOD_ID + ":wisp")
	public static ParticleType<WispParticleData> WISP;

	@ObjectHolder(registryName = "particle_type", value = LibMisc.MOD_ID + ":sparkle")
	public static ParticleType<SparkleParticleData> SPARKLE;

	@SubscribeEvent
	public static void register(RegisterEvent evt) {
		evt.register(ForgeRegistries.Keys.PARTICLE_TYPES, helper -> {
			helper.register(new ResourceLocation(LibMisc.MOD_ID, "wisp"), new WispParticleType());
			helper.register(new ResourceLocation(LibMisc.MOD_ID, "sparkle"), new SparkleParticleType());
		});
	}

	@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class FactoryHandler {

		@SubscribeEvent
		public static void registerFactories(RegisterParticleProvidersEvent evt) {
			evt.register(ModParticles.WISP, WispParticleType.Factory::new);
			evt.register(ModParticles.SPARKLE, SparkleParticleType.Factory::new);
		}
	}
}
