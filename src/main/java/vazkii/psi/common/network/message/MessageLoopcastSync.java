/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Feb 02, 2019, 10:05 AM (EST)]
 */
package vazkii.psi.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.util.ClientTicker;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class MessageLoopcastSync extends NetworkMessage<MessageLoopcastSync> {

	public int entityId;
	public byte loopcastState;

	public MessageLoopcastSync() { }

	public MessageLoopcastSync(int entityId, boolean isLoopcasting, Hand hand) {
		this.entityId = entityId;
		loopcastState = (byte) ((isLoopcasting ? 1 : 0) | (hand == null ? 0 : hand.ordinal() << 1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		boolean isLoopcasting = (loopcastState & 0b1) != 0;

		Hand loopcastHand = isLoopcasting ?
				((loopcastState & 0b10) != 0 ? Hand.OFF_HAND : Hand.MAIN_HAND) : null;

		ClientTicker.addAction(() -> {
			World world = Minecraft.getMinecraft().world;
			PlayerEntity mcPlayer = Minecraft.getMinecraft().player;
			if (mcPlayer == null)
				return;

			Entity player = null;
			if (world != null)
				player = world.getEntityByID(entityId);
			else if (mcPlayer.getEntityId() == entityId)
				player = mcPlayer;

			if (player instanceof PlayerEntity) {
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get((PlayerEntity) player);
				data.loopcasting = isLoopcasting;
				data.loopcastHand = loopcastHand;
			}
		});

		return null;
	}

}
