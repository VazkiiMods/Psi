/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

public final class PsiPacketCodecs {

	public static final StreamCodec<RegistryFriendlyByteBuf, Vec3> VEC3 = new StreamCodec<>() {
		@Override
		public @NotNull Vec3 decode(RegistryFriendlyByteBuf buffer) {
			return buffer.readVec3();
		}

		@Override
		public void encode(RegistryFriendlyByteBuf buffer, @NotNull Vec3 value) {
			buffer.writeVec3(value);
		}
	};

	private PsiPacketCodecs() {}

}
