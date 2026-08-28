/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import vazkii.psi.api.internal.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CADDataValue {
	public static final Codec<CADDataValue> CODEC = RecordCodecBuilder.create(
			builder -> builder.group(
					Codec.INT.fieldOf("Time").forGetter(data -> data.time),
					Codec.INT.fieldOf("Battery").forGetter(data -> data.battery),
					Codec.list(Vector3.CODEC).fieldOf("Memory").forGetter(data -> data.vectors)
			).apply(builder, CADDataValue::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, CADDataValue> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, data -> data.time,
			ByteBufCodecs.INT, data -> data.battery,
			Vector3.STREAM_CODEC.apply(ByteBufCodecs.list()), data -> data.vectors,
			CADDataValue::new);

	public int time;
	public int battery;
	public List<Vector3> vectors;

	public CADDataValue(int time, int battery, List<Vector3> vectors) {
		this.time = time;
		this.battery = battery;
		this.vectors = new ArrayList<>(vectors);
	}

	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof CADDataValue data
				&& data.time == time && data.battery == battery && data.vectors.equals(vectors);
	}

	@Override
	public int hashCode() {
		return Objects.hash(time, battery, vectors);
	}
}
