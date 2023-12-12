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

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleType;

// https://github.com/Vazkii/Botania/blob/1.15/src/main/java/vazkii/botania/client/fx/SparkleParticleType.java
public class SparkleParticleType extends ParticleType<SparkleParticleData> {

	public SparkleParticleType() {
		super(false, SparkleParticleData.DESERIALIZER);
	}

	@Override
	public Codec<SparkleParticleData> codec() {
		return SparkleParticleData.CODEC;
	}

	public static class Factory implements ParticleProvider<SparkleParticleData> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprite) {
			this.sprite = sprite;
		}

		@Override
		public TextureSheetParticle createParticle(SparkleParticleData data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
			return new FXSparkle(world, x, y, z, data.size, data.r, data.g, data.b, data.m, mx, my, mz, sprite);
		}
	}
}
