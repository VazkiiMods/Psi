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
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public record WispParticleData(float size, float r, float g, float b, float maxAgeMul) implements ParticleOptions {

	public static final MapCodec<WispParticleData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
			Codec.FLOAT.fieldOf("r").forGetter(d -> d.r),
			Codec.FLOAT.fieldOf("g").forGetter(d -> d.g),
			Codec.FLOAT.fieldOf("b").forGetter(d -> d.b),
			Codec.FLOAT.fieldOf("maxAgeMul").forGetter(d -> d.maxAgeMul)
	).apply(instance, WispParticleData::new));

	public static StreamCodec<? super RegistryFriendlyByteBuf, WispParticleData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.FLOAT, WispParticleData::size,
			ByteBufCodecs.FLOAT, WispParticleData::r,
			ByteBufCodecs.FLOAT, WispParticleData::g,
			ByteBufCodecs.FLOAT, WispParticleData::b,
			ByteBufCodecs.FLOAT, WispParticleData::maxAgeMul,
			WispParticleData::new);

	@NotNull
	@Override
	public ParticleType<WispParticleData> getType() {
		return ModParticles.WISP.get();
	}

	public static class Type extends ParticleType<WispParticleData> {

		protected Type(boolean pOverrideLimitter) {
			super(pOverrideLimitter);
		}

		@Override
		public MapCodec<WispParticleData> codec() {
			return WispParticleData.CODEC;
		}

		@Override
		public StreamCodec<? super RegistryFriendlyByteBuf, WispParticleData> streamCodec() {
			return WispParticleData.STREAM_CODEC;
		}
	}
}
