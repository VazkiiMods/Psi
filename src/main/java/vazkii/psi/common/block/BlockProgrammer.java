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

import java.util.UUID;

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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.BlockFacing;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
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

		if(!playerIn.capabilities.isCreativeMode) {
			PlayerData data = PlayerDataHandler.get(playerIn);
			if(data.spellGroupsUnlocked.isEmpty()) {
				if(!worldIn.isRemote)
					playerIn.addChatComponentMessage(new ChatComponentTranslation("psimisc.cantUseProgrammer").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				return true;
			}
		}
		
		boolean enabled = programmer.isEnabled();
		if(enabled && !programmer.playerLock.isEmpty()) {
			if(!programmer.playerLock.equals(playerIn.getName())) {
				if(!worldIn.isRemote)
					playerIn.addChatComponentMessage(new ChatComponentTranslation("psimisc.notYourProgrammer").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				return true;
			}
		} else programmer.playerLock = playerIn.getName();
		
		ItemStack stack = playerIn.getCurrentEquippedItem();
		if(enabled && stack != null && stack.getItem() instanceof ISpellContainer && programmer.spell != null && !programmer.spell.name.trim().isEmpty()) {
			if(programmer.canCompile()) {
				ISpellContainer container = (ISpellContainer) stack.getItem();
				if(!worldIn.isRemote)
					worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "psi:bulletCreate", 0.5F, 1F);
				
				programmer.spell.uuid = UUID.randomUUID();
				container.setSpell(stack, programmer.spell);
				if(playerIn instanceof EntityPlayerMP)
					VanillaPacketDispatcher.dispatchTEToPlayer(programmer, (EntityPlayerMP) playerIn);				
				return true;
			} else {
				if(!worldIn.isRemote)
					worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "psi:compileError", 0.5F, 1F);
				return false;
			}
		}

		if(playerIn instanceof EntityPlayerMP)
			VanillaPacketDispatcher.dispatchTEToPlayer(programmer, (EntityPlayerMP) playerIn);
		playerIn.openGui(Psi.instance, LibGuiIDs.PROGRAMMER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public IBlockState makeDefaultState() {
		return super.makeDefaultState().withProperty(ENABLED, false);
	}
	
	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity tile = worldIn.getTileEntity(pos);
        return super.getActualState(state, worldIn, pos).withProperty(ENABLED, tile != null && tile instanceof TileProgrammer && ((TileProgrammer) tile).isEnabled());
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
