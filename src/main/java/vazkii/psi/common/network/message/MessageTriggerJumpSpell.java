/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.api.exosuit.PsiArmorEvent;

import java.util.function.Supplier;

public class MessageTriggerJumpSpell {

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> PsiArmorEvent.post(new PsiArmorEvent(context.get().getSender(), PsiArmorEvent.JUMP)));
		return true;
	}

}
