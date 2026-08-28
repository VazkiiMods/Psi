/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network;

import vazkii.psi.common.network.message.MessageAdditiveMotion;
import vazkii.psi.common.network.message.MessageBlink;
import vazkii.psi.common.network.message.MessageChangeControllerSlot;
import vazkii.psi.common.network.message.MessageChangeSocketableSlot;
import vazkii.psi.common.network.message.MessageDataSync;
import vazkii.psi.common.network.message.MessageDeductPsi;
import vazkii.psi.common.network.message.MessageEidosSync;
import vazkii.psi.common.network.message.MessageFlashRingSync;
import vazkii.psi.common.network.message.MessageLoopcastSync;
import vazkii.psi.common.network.message.MessageParticleTrail;
import vazkii.psi.common.network.message.MessagePsiOverflow;
import vazkii.psi.common.network.message.MessageSpamlessChat;
import vazkii.psi.common.network.message.MessageSpellError;
import vazkii.psi.common.network.message.MessageSpellModified;
import vazkii.psi.common.network.message.MessageTriggerJumpSpell;
import vazkii.psi.common.network.message.MessageVisualEffect;

public final class PsiPackets {

	private PsiPackets() {}

	public static void register() {
		PsiNetwork.initialize();
		PsiNetwork.registerClientbound(MessageAdditiveMotion.TYPE, MessageAdditiveMotion.CODEC, MessageAdditiveMotion::handle);
		PsiNetwork.registerClientbound(MessageBlink.TYPE, MessageBlink.CODEC, MessageBlink::handle);
		PsiNetwork.registerClientbound(MessageDataSync.TYPE, MessageDataSync.CODEC, MessageDataSync::handle);
		PsiNetwork.registerClientbound(MessageDeductPsi.TYPE, MessageDeductPsi.CODEC, MessageDeductPsi::handle);
		PsiNetwork.registerClientbound(MessageEidosSync.TYPE, MessageEidosSync.CODEC, MessageEidosSync::handle);
		PsiNetwork.registerClientbound(MessageLoopcastSync.TYPE, MessageLoopcastSync.CODEC, MessageLoopcastSync::handle);
		PsiNetwork.registerClientbound(MessageParticleTrail.TYPE, MessageParticleTrail.CODEC, MessageParticleTrail::handle);
		PsiNetwork.registerClientbound(MessagePsiOverflow.TYPE, MessagePsiOverflow.CODEC, MessagePsiOverflow::handle);
		PsiNetwork.registerClientbound(MessageSpamlessChat.TYPE, MessageSpamlessChat.CODEC, MessageSpamlessChat::handle);
		PsiNetwork.registerClientbound(MessageSpellError.TYPE, MessageSpellError.CODEC, MessageSpellError::handle);
		PsiNetwork.registerClientbound(MessageVisualEffect.TYPE, MessageVisualEffect.CODEC, MessageVisualEffect::handle);
		PsiNetwork.registerServerbound(MessageChangeControllerSlot.TYPE, MessageChangeControllerSlot.CODEC, MessageChangeControllerSlot::handle);
		PsiNetwork.registerServerbound(MessageChangeSocketableSlot.TYPE, MessageChangeSocketableSlot.CODEC, MessageChangeSocketableSlot::handle);
		PsiNetwork.registerServerbound(MessageFlashRingSync.TYPE, MessageFlashRingSync.CODEC, MessageFlashRingSync::handle);
		PsiNetwork.registerServerbound(MessageSpellModified.TYPE, MessageSpellModified.CODEC, MessageSpellModified::handle);
		PsiNetwork.registerServerbound(MessageTriggerJumpSpell.TYPE, MessageTriggerJumpSpell.CODEC, MessageTriggerJumpSpell::handle);
	}

}
