/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [09/01/2016, 23:01:02 (GMT)]
 */
package vazkii.psi.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.BlockMod;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibGuiIDs;

public class BlockCADAssembler extends BlockMod {

	public BlockCADAssembler() {
		super(LibBlockNames.CAD_ASSEMBLER, Material.iron);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		playerIn.openGui(Psi.instance, LibGuiIDs.CAD_ASSEMBLER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCADAssembler();
	}

}
