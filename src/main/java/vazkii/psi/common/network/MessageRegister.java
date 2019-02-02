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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.network.message.*;

public class MessageRegister {

	public static void init() {
		NetworkHandler.register(MessageLoopcastSync.class, Side.CLIENT);
		NetworkHandler.register(MessageDataSync.class, Side.CLIENT);
		NetworkHandler.register(MessageEidosSync.class, Side.CLIENT);
		NetworkHandler.register(MessageCADDataSync.class, Side.CLIENT);
		NetworkHandler.register(MessageDeductPsi.class, Side.CLIENT);
		NetworkHandler.register(MessageChangeSocketableSlot.class, Side.SERVER);
		NetworkHandler.register(MessageSpellModified.class, Side.SERVER);
		NetworkHandler.register(MessageLearnGroup.class, Side.SERVER);
		NetworkHandler.register(MessageSkipToLevel.class, Side.SERVER);
		NetworkHandler.register(MessageLevelUp.class, Side.CLIENT);
		NetworkHandler.register(MessageChangeControllerSlot.class, Side.SERVER);
		NetworkHandler.register(MessageTriggerJumpSpell.class, Side.SERVER);
		NetworkHandler.register(MessageVisualEffect.class, Side.CLIENT);

		NetworkMessage.mapHandler(Spell.class, MessageRegister::readSpell, MessageRegister::writeSpell);
	}

	private static Spell readSpell(ByteBuf buf) {
		NBTTagCompound cmp = ByteBufUtils.readTag(buf);
		return Spell.createFromNBT(cmp);
	}

	private static void writeSpell(Spell spell, ByteBuf buf) {
		NBTTagCompound cmp = new NBTTagCompound();
		if(spell != null)
			spell.writeToNBT(cmp);

		ByteBufUtils.writeTag(buf, cmp);
	}
	
}
