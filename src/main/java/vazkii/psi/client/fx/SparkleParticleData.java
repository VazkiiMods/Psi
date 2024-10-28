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
import net.minecraft.network.codec.StreamCodec;

import javax.annotation.Nonnull;

public record SparkleParticleData(float size, float r, float g, float b, int m, double mx, double my,
                                  double mz) implements ParticleOptions {
    public static MapCodec<SparkleParticleData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
            Codec.FLOAT.fieldOf("r").forGetter(d -> d.r),
            Codec.FLOAT.fieldOf("g").forGetter(d -> d.g),
            Codec.FLOAT.fieldOf("b").forGetter(d -> d.b),
            Codec.INT.fieldOf("m").forGetter(d -> d.m),
            Codec.DOUBLE.fieldOf("mx").forGetter(d -> d.mx),
            Codec.DOUBLE.fieldOf("my").forGetter(d -> d.my),
            Codec.DOUBLE.fieldOf("mz").forGetter(d -> d.mz)
    ).apply(instance, SparkleParticleData::new));

    public static StreamCodec<? super RegistryFriendlyByteBuf, SparkleParticleData> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, SparkleParticleData>() {
        public SparkleParticleData decode(RegistryFriendlyByteBuf pBuffer) {
            return new SparkleParticleData(
                    pBuffer.readFloat(),
                    pBuffer.readFloat(),
                    pBuffer.readFloat(),
                    pBuffer.readFloat(),
                    pBuffer.readInt(),
                    pBuffer.readDouble(),
                    pBuffer.readDouble(),
                    pBuffer.readDouble()
            );
        }

        public void encode(RegistryFriendlyByteBuf pBuffer, SparkleParticleData data) {
            pBuffer.writeFloat(data.size());
            pBuffer.writeFloat(data.r());
            pBuffer.writeFloat(data.g());
            pBuffer.writeFloat(data.b());
            pBuffer.writeInt(data.m());
            pBuffer.writeDouble(data.mx());
            pBuffer.writeDouble(data.my());
            pBuffer.writeDouble(data.mz());
        }
    };

    @Nonnull
    @Override
    public ParticleType<SparkleParticleData> getType() {
        return ModParticles.SPARKLE.get();
    }

    public static class Type extends ParticleType<SparkleParticleData> {

        protected Type(boolean pOverrideLimitter) {
            super(pOverrideLimitter);
        }

        @Override
        public MapCodec<SparkleParticleData> codec() {
            return SparkleParticleData.CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, SparkleParticleData> streamCodec() {
            return SparkleParticleData.STREAM_CODEC;
        }
    }
}
