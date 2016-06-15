/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 21, 2014, 9:18:28 PM (GMT)]
 */
package vazkii.psi.common.block.tile.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileMod extends TileEntity {

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1nbtTagCompound) {
		NBTTagCompound nbt = super.writeToNBT(par1nbtTagCompound);

		writeSharedNBT(par1nbtTagCompound);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);

		readSharedNBT(par1nbtTagCompound);
	}

	public void writeSharedNBT(NBTTagCompound cmp) {
		// NO-OP
	}

	public void readSharedNBT(NBTTagCompound cmp) {
		// NO-OP
	}

	@Override
    public NBTTagCompound getUpdateTag()  {
        return writeToNBT(new NBTTagCompound());
    }
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound cmp = new NBTTagCompound();
		writeSharedNBT(cmp);
		return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), cmp);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readSharedNBT(packet.getNbtCompound());
	}

}
