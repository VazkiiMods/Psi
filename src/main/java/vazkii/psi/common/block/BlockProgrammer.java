/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [12/01/2016, 17:41:48 (GMT)]
 */
package vazkii.psi.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.b3d.B3DModel.Face;
import vazkii.psi.common.block.base.BlockFacing;
import vazkii.psi.common.block.base.BlockMod;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.lib.LibBlockNames;

public class BlockProgrammer extends BlockFacing {

    public static final PropertyBool ENABLED = PropertyBool.create("enabled");
	
	public BlockProgrammer() {
		super(LibBlockNames.PROGRAMMER, Material.iron);
		setHardness(5.0F);
		setResistance(10.0F);
		setStepSound(soundTypeMetal);
	}
	
	@Override
	public IBlockState makeDefaultState() {
		return super.makeDefaultState().withProperty(ENABLED, false);
	}
	
	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return super.getActualState(state, worldIn, pos).withProperty(ENABLED, worldIn.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.redstone_block); // TODO make this reflect TE
    }

	@Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { FACING, ENABLED });
    }
	
	@Override
	public boolean isFullBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public EnumRarity getBlockRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCADAssembler(); // TODO replace with proper one
	}
	
}
