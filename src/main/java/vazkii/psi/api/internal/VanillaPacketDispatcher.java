/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 9, 2015, 9:38:44 PM (GMT)]
 */
package vazkii.psi.api.internal;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public final class VanillaPacketDispatcher {

	public static void dispatchTEToNearbyPlayers(TileEntity tile) {
		World world = tile.getWorld();
        List players = world.getPlayers();
		for(Object player : players)
			if(player instanceof ServerPlayerEntity) {
				ServerPlayerEntity mp = (ServerPlayerEntity) player;
				if (MathHelper.pointDistancePlane(mp.getX(), mp.getZ(), tile.getPos().getX() + 0.5, tile.getPos().getZ() + 0.5) < 64)
					dispatchTEToPlayer(tile, mp);
			}
	}

	public static void dispatchTEToNearbyPlayers(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null)
			dispatchTEToNearbyPlayers(tile);
	}

	public static void dispatchTEToPlayer(TileEntity tile, ServerPlayerEntity p) {
		SUpdateTileEntityPacket packet = tile.getUpdatePacket();
		if (packet != null)
		p.connection.sendPacket(packet);
	}

}
