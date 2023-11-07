/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class VanillaPacketDispatcher {

	public static void dispatchTEToNearbyPlayers(BlockEntity tile) {
		Level world = tile.getLevel();
		for(Player player : world.players()) {
			if(player instanceof ServerPlayer) {
				ServerPlayer mp = (ServerPlayer) player;
				if(MathHelper.pointDistancePlane(mp.getX(), mp.getZ(), tile.getBlockPos().getX() + 0.5, tile.getBlockPos().getZ() + 0.5) < 64) {
					dispatchTEToPlayer(tile, mp);
				}
			}
		}
	}

	public static void dispatchTEToNearbyPlayers(Level world, BlockPos pos) {
		BlockEntity tile = world.getBlockEntity(pos);
		if(tile != null) {
			dispatchTEToNearbyPlayers(tile);
		}
	}

	public static void dispatchTEToPlayer(BlockEntity tile, ServerPlayer p) {
		Packet<ClientGamePacketListener> packet = tile.getUpdatePacket();
		if(packet != null) {
			p.connection.send(packet);
		}
	}

}
