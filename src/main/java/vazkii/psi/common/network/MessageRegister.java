/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.message.*;

public class MessageRegister {
	private static final String VERSION = "3";
	public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(LibMisc.MOD_ID, "main"),
			() -> VERSION,
			VERSION::equals,
			VERSION::equals);

	public static void init() {
		int id = 0;
		HANDLER.messageBuilder(MessageLoopcastSync.class, id++)
				.encoder(MessageLoopcastSync::encode)
				.decoder(MessageLoopcastSync::new)
				.consumer(MessageLoopcastSync::receive).add();
		HANDLER.messageBuilder(MessageDataSync.class, id++)
				.encoder(MessageDataSync::encode)
				.decoder(MessageDataSync::new)
				.consumer(MessageDataSync::receive).add();
		HANDLER.messageBuilder(MessageEidosSync.class, id++)
				.encoder(MessageEidosSync::encode)
				.decoder(MessageEidosSync::new)
				.consumer(MessageEidosSync::receive).add();
		HANDLER.messageBuilder(MessageCADDataSync.class, id++)
				.encoder(MessageCADDataSync::encode)
				.decoder(MessageCADDataSync::new)
				.consumer(MessageCADDataSync::receive).add();
		HANDLER.messageBuilder(MessageDeductPsi.class, id++)
				.encoder(MessageDeductPsi::encode)
				.decoder(MessageDeductPsi::new)
				.consumer(MessageDeductPsi::receive).add();
		HANDLER.messageBuilder(MessageChangeSocketableSlot.class, id++)
				.encoder(MessageChangeSocketableSlot::encode)
				.decoder(MessageChangeSocketableSlot::new)
				.consumer(MessageChangeSocketableSlot::receive).add();
		HANDLER.messageBuilder(MessageSpellModified.class, id++)
				.encoder(MessageSpellModified::encode)
				.decoder(MessageSpellModified::new)
				.consumer(MessageSpellModified::receive).add();
		HANDLER.messageBuilder(MessageChangeControllerSlot.class, id++)
				.encoder(MessageChangeControllerSlot::encode)
				.decoder(MessageChangeControllerSlot::new)
				.consumer(MessageChangeControllerSlot::receive).add();
		HANDLER.messageBuilder(MessageTriggerJumpSpell.class, id++)
				.encoder((msg, buf) -> {})
				.decoder($ -> new MessageTriggerJumpSpell())
				.consumer(MessageTriggerJumpSpell::receive).add();
		HANDLER.messageBuilder(MessageVisualEffect.class, id++)
				.encoder(MessageVisualEffect::encode)
				.decoder(MessageVisualEffect::new)
				.consumer(MessageVisualEffect::receive).add();
		HANDLER.messageBuilder(MessageAdditiveMotion.class, id++)
				.encoder(MessageAdditiveMotion::encode)
				.decoder(MessageAdditiveMotion::new)
				.consumer(MessageAdditiveMotion::receive).add();
		HANDLER.messageBuilder(MessageBlink.class, id++)
				.encoder(MessageBlink::encode)
				.decoder(MessageBlink::new)
				.consumer(MessageBlink::receive).add();
		HANDLER.messageBuilder(MessageSpamlessChat.class, id++)
				.encoder(MessageSpamlessChat::encode)
				.decoder(MessageSpamlessChat::new)
				.consumer(MessageSpamlessChat::receive).add();
		HANDLER.messageBuilder(MessageParticleTrail.class, id++)
				.encoder(MessageParticleTrail::encode)
				.decoder(MessageParticleTrail::new)
				.consumer(MessageParticleTrail::receive).add();
		HANDLER.messageBuilder(MessageSpellError.class, id++)
				.encoder(MessageSpellError::encode)
				.decoder(MessageSpellError::new)
				.consumer(MessageSpellError::receive).add();
	}

	public static void writeVec3d(PacketBuffer buf, Vector3d vec3d) {
		buf.writeDouble(vec3d.x);
		buf.writeDouble(vec3d.y);
		buf.writeDouble(vec3d.z);
	}

	public static Vector3d readVec3d(PacketBuffer buf) {
		return new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	public static void sendToPlayer(Object msg, PlayerEntity player) {
		ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
		HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), msg);
	}

}
