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
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.BlockFacing;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibGuiIDs;

public class BlockProgrammer extends BlockFacing {

    public static final PropertyBool ENABLED = PropertyBool.create("enabled");
	
	public BlockProgrammer() {
		super(LibBlockNames.PROGRAMMER, Material.iron);
		setHardness(5.0F);
		setResistance(10.0F);
		setStepSound(soundTypeMetal);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileProgrammer programmer = (TileProgrammer) worldIn.getTileEntity(pos);
		Runnable dispatch = () -> {
			if(playerIn instanceof EntityPlayerMP)
				VanillaPacketDispatcher.dispatchTEToPlayer(programmer, (EntityPlayerMP) playerIn);
		};
		
		ItemStack stack = playerIn.getCurrentEquippedItem();
		if(programmer.isEnabled() && stack != null && stack.getItem() instanceof ISpellContainer && programmer.spell != null && !programmer.spell.name.trim().isEmpty() && programmer.canCompile()) {
			ISpellContainer container = (ISpellContainer) stack.getItem();
			if(container.canSetSpell(stack)) {
				container.setSpell(stack, programmer.spell);
				dispatch.run();
				return true;
			}
		}

		dispatch.run();
		playerIn.openGui(Psi.instance, LibGuiIDs.PROGRAMMER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public IBlockState makeDefaultState() {
		return super.makeDefaultState().withProperty(ENABLED, false);
	}
	
	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return super.getActualState(state, worldIn, pos).withProperty(ENABLED, ((TileProgrammer) worldIn.getTileEntity(pos)).isEnabled());
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
		return new TileProgrammer(); 
	}
	
}
