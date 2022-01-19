/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.fx;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

import javax.annotation.Nonnull;

import java.util.Locale;

// https://github.com/Vazkii/Botania/blob/1.15/src/main/java/vazkii/botania/client/fx/WispParticleData.java
import net.minecraft.particles.IParticleData.IDeserializer;

public class WispParticleData implements IParticleData {
	public static final Codec<WispParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
			Codec.FLOAT.fieldOf("r").forGetter(d -> d.r),
			Codec.FLOAT.fieldOf("g").forGetter(d -> d.g),
			Codec.FLOAT.fieldOf("b").forGetter(d -> d.b),
			Codec.FLOAT.fieldOf("maxAgeMul").forGetter(d -> d.maxAgeMul)
	)
			.apply(instance, WispParticleData::new));
	public final float size;
	public final float r, g, b;
	public final float maxAgeMul;

	public static WispParticleData wisp(float size, float r, float g, float b) {
		return wisp(size, r, g, b, 1);
	}

	public static WispParticleData wisp(float size, float r, float g, float b, float maxAgeMul) {
		return new WispParticleData(size, r, g, b, maxAgeMul);
	}

	public WispParticleData(float size, float r, float g, float b, float maxAgeMul) {
		this.size = size;
		this.r = r;
		this.g = g;
		this.b = b;
		this.maxAgeMul = maxAgeMul;
	}

	@Nonnull
	@Override
	public ParticleType<WispParticleData> getType() {
		return ModParticles.WISP;
	}

	@Override
	public void writeToNetwork(PacketBuffer buf) {
		buf.writeFloat(size);
		buf.writeFloat(r);
		buf.writeFloat(g);
		buf.writeFloat(b);
		buf.writeFloat(maxAgeMul);
	}

	@Nonnull
	@Override
	public String writeToString() {
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f",
				this.getType().getRegistryName(), this.size, this.r, this.g, this.b, this.maxAgeMul);
	}

	public static final IDeserializer<WispParticleData> DESERIALIZER = new IDeserializer<WispParticleData>() {
		@Nonnull
		@Override
		public WispParticleData fromCommand(@Nonnull ParticleType<WispParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			float size = reader.readFloat();
			reader.expect(' ');
			float r = reader.readFloat();
			reader.expect(' ');
			float g = reader.readFloat();
			reader.expect(' ');
			float b = reader.readFloat();
			reader.expect(' ');
			float mam = reader.readFloat();
			return new WispParticleData(size, r, g, b, mam);
		}

		@Override
		public WispParticleData fromNetwork(@Nonnull ParticleType<WispParticleData> type, PacketBuffer buf) {
			return new WispParticleData(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
		}
	};
}
