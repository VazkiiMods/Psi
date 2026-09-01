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

public record CADDataValue(int time, int battery, List<Vector3> vectors) {

	public static final Codec<CADDataValue> CODEC = RecordCodecBuilder.create(
			builder -> builder.group(
					Codec.INT.fieldOf("Time").forGetter(CADDataValue::time),
					Codec.INT.fieldOf("Battery").forGetter(CADDataValue::battery),
					Codec.list(Vector3.CODEC).fieldOf("Memory").forGetter(CADDataValue::vectors)
			).apply(builder, CADDataValue::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, CADDataValue> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, CADDataValue::time,
			ByteBufCodecs.INT, CADDataValue::battery,
			Vector3.STREAM_CODEC.apply(ByteBufCodecs.list()), CADDataValue::vectors,
			CADDataValue::new);
	public static final CADDataValue EMPTY = new CADDataValue(0, 0, List.of());

	public CADDataValue {
		vectors = List.copyOf(vectors);
	}

	public CADDataValue withTime(int time) {
		return new CADDataValue(time, battery, vectors);
	}

	public CADDataValue withBattery(int battery) {
		return new CADDataValue(time, battery, vectors);
	}

	public CADDataValue withSavedVector(int memorySlot, Vector3 value) {
		List<Vector3> newVectors = new ArrayList<>(vectors);
		while(newVectors.size() <= memorySlot) {
			newVectors.add(Vector3.zero.copy());
		}
		newVectors.set(memorySlot, value.copy());
		return new CADDataValue(time, battery, newVectors);
	}
}
