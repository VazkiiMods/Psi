/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.arl.network.IMessage;
import vazkii.psi.api.exosuit.PsiArmorEvent;

public class MessageTriggerJumpSpell implements IMessage {

	@Override
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> PsiArmorEvent.post(new PsiArmorEvent(context.getSender(), PsiArmorEvent.JUMP)));
		return true;
	}

}
