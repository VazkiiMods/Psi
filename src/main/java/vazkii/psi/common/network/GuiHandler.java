/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [10/01/2016, 14:53:05 (GMT)]
 */
package vazkii.psi.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import vazkii.psi.client.gui.GuiCADAssembler;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.lib.LibGuiIDs;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case LibGuiIDs.CAD_ASSEMBLER:
			return new ContainerCADAssembler(player.inventory, (TileCADAssembler) world.getTileEntity(new BlockPos(x, y, z)));
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case LibGuiIDs.CAD_ASSEMBLER:
			return new GuiCADAssembler(player.inventory, (TileCADAssembler) world.getTileEntity(new BlockPos(x, y, z)));
		case LibGuiIDs.PROGRAMMER:
			return new GuiProgrammer((TileProgrammer) world.getTileEntity(new BlockPos(x, y, z)));
		}

		return null;
	}


}
