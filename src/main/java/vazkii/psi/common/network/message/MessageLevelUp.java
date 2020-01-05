/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [15/02/2016, 18:45:31 (GMT)]
 */
package vazkii.psi.common.network.message;

import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.arl.network.IMessage;
import vazkii.psi.common.Psi;

public class MessageLevelUp implements IMessage {

	public int level;

	public MessageLevelUp() { }

	public MessageLevelUp(int level) {
		this.level = level;
	}

	@Override
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> Psi.proxy.onLevelUp(level));
		return true;
	}

}
