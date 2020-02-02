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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import vazkii.arl.network.IMessage;
import vazkii.arl.network.MessageSerializer;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.message.*;

import java.lang.reflect.Field;
import java.util.List;

public class MessageRegister {
	public static final NetworkHandler HANDLER = new NetworkHandler(LibMisc.MOD_ID, 1);

	@SuppressWarnings("unchecked")
	public static void init() {
		HANDLER.register(MessageLoopcastSync.class, NetworkDirection.PLAY_TO_CLIENT);
		HANDLER.register(MessageDataSync.class, NetworkDirection.PLAY_TO_CLIENT);
		HANDLER.register(MessageEidosSync.class, NetworkDirection.PLAY_TO_CLIENT);
		HANDLER.register(MessageCADDataSync.class, NetworkDirection.PLAY_TO_CLIENT);
		HANDLER.register(MessageDeductPsi.class, NetworkDirection.PLAY_TO_CLIENT);
		HANDLER.register(MessageChangeSocketableSlot.class, NetworkDirection.PLAY_TO_SERVER);
		HANDLER.register(MessageSpellModified.class, NetworkDirection.PLAY_TO_SERVER);
		HANDLER.register(MessageLearnGroup.class, NetworkDirection.PLAY_TO_SERVER);
		HANDLER.register(MessageSkipToLevel.class, NetworkDirection.PLAY_TO_SERVER);
		HANDLER.register(MessageLevelUp.class, NetworkDirection.PLAY_TO_CLIENT);
		HANDLER.register(MessageChangeControllerSlot.class, NetworkDirection.PLAY_TO_SERVER);
		HANDLER.register(MessageTriggerJumpSpell.class, NetworkDirection.PLAY_TO_SERVER);
		HANDLER.register(MessageVisualEffect.class, NetworkDirection.PLAY_TO_CLIENT);
		HANDLER.register(MessageAdditiveMotion.class, NetworkDirection.PLAY_TO_CLIENT);
		HANDLER.register(MessageBlink.class, NetworkDirection.PLAY_TO_CLIENT);

		MessageSerializer.mapHandler(Spell.class, MessageRegister::readSpell, MessageRegister::writeSpell);
	}

	private static Spell readSpell(PacketBuffer buf, Field f) {
		CompoundNBT cmp = buf.readCompoundTag();
        return Spell.createFromNBT(cmp);
    }

    private static void writeSpell(PacketBuffer buf, Field f, Spell spell) {
        CompoundNBT cmp = new CompoundNBT();
        if (spell != null)
            spell.writeToNBT(cmp);

        buf.writeCompoundTag(cmp);
    }

    public static void sendToAllAround(IMessage message, BlockPos origin, World world, int radius) {
        List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(origin.getX() - radius, origin.getY() - radius, origin.getZ() - radius, origin.getX() + radius, origin.getY() + 32, origin.getZ() + 32),
                entity -> entity != null && entity.getDistanceSq(origin.getX(), origin.getY(), origin.getZ()) <= radius * radius);
        players.forEach(pl -> HANDLER.sendToPlayer(message, (ServerPlayerEntity) pl));

    }


}
