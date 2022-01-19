/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.fx;

import com.mojang.serialization.Codec;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleType;

// https://github.com/Vazkii/Botania/blob/1.15/src/main/java/vazkii/botania/client/fx/WispParticleType.java
public class WispParticleType extends ParticleType<WispParticleData> {
	public WispParticleType() {
		super(false, WispParticleData.DESERIALIZER);
	}

	@Override
	public Codec<WispParticleData> codec() {
		return WispParticleData.CODEC;
	}

	public static class Factory implements ParticleProvider<WispParticleData> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprite) {
			this.sprite = sprite;
		}

		@Override
		public Particle createParticle(WispParticleData data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
			FXWisp ret = new FXWisp(world, x, y, z, mx, my, mz, data.size, data.r, data.g, data.b, data.maxAgeMul);
			ret.pickSprite(sprite);
			return ret;
		}
	}
}
