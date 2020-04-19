/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.arl.network.IMessage;
import vazkii.psi.common.Psi;

public class MessageLevelUp implements IMessage {

	public ResourceLocation level;

	public MessageLevelUp() {}

	public MessageLevelUp(ResourceLocation level) {
		this.level = level;
	}

	@Override
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> Psi.proxy.onLevelUp(level));
		return true;
	}

}
