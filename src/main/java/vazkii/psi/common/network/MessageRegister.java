/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [11/01/2016, 21:58:25 (GMT)]
 */
package vazkii.psi.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.api.distmarker.Dist;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.network.message.*;

public class MessageRegister {

	@SuppressWarnings("unchecked")
	public static void init() {
		NetworkHandler.register(MessageLoopcastSync.class, Dist.CLIENT);
		NetworkHandler.register(MessageDataSync.class, Dist.CLIENT);
		NetworkHandler.register(MessageEidosSync.class, Dist.CLIENT);
		NetworkHandler.register(MessageCADDataSync.class, Dist.CLIENT);
		NetworkHandler.register(MessageDeductPsi.class, Dist.CLIENT);
		NetworkHandler.register(MessageChangeSocketableSlot.class, Dist.SERVER);
		NetworkHandler.register(MessageSpellModified.class, Dist.SERVER);
		NetworkHandler.register(MessageLearnGroup.class, Dist.SERVER);
		NetworkHandler.register(MessageSkipToLevel.class, Dist.SERVER);
		NetworkHandler.register(MessageLevelUp.class, Dist.CLIENT);
		NetworkHandler.register(MessageChangeControllerSlot.class, Dist.SERVER);
		NetworkHandler.register(MessageTriggerJumpSpell.class, Dist.SERVER);
		NetworkHandler.register(MessageVisualEffect.class, Dist.CLIENT);
		NetworkHandler.register(MessageAdditiveMotion.class, Dist.CLIENT);
		NetworkHandler.register(MessageBlink.class, Dist.CLIENT);

		NetworkMessage.mapHandler(Spell.class, MessageRegister::readSpell, MessageRegister::writeSpell);
	}

	private static Spell readSpell(ByteBuf buf) {
		CompoundNBT cmp = ByteBufUtils.readTag(buf);
		return Spell.createFromNBT(cmp);
	}

	private static void writeSpell(Spell spell, ByteBuf buf) {
		CompoundNBT cmp = new CompoundNBT();
		if(spell != null)
			spell.writeToNBT(cmp);

		ByteBufUtils.writeTag(buf, cmp);
	}
	
}
