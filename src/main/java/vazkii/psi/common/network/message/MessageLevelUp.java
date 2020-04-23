/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.common.Psi;

import java.util.function.Supplier;

public class MessageLevelUp {

	private final ResourceLocation level;

	public MessageLevelUp(ResourceLocation level) {
		this.level = level;
	}

	public MessageLevelUp(PacketBuffer buf) {
		this.level = buf.readResourceLocation();
	}

	public void encode(PacketBuffer buf) {
		buf.writeResourceLocation(level);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> Psi.proxy.onLevelUp(level));
		return true;
	}

}
