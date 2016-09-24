/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [12/01/2016, 17:41:48 (GMT)]
 */
package vazkii.psi.common.block;

import java.util.UUID;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.arl.block.BlockFacing;
import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.ISpellSettable;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.IPsiBlock;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibGuiIDs;

public class BlockProgrammer extends BlockFacing implements IPsiBlock {

	public static final PropertyBool ENABLED = PropertyBool.create("enabled");

	public BlockProgrammer() {
		super(LibBlockNames.PROGRAMMER, Material.IRON);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);
	}

	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileProgrammer programmer = (TileProgrammer) worldIn.getTileEntity(pos);

		if(!playerIn.capabilities.isCreativeMode) {
			PlayerData data = PlayerDataHandler.get(playerIn);
			if(data.spellGroupsUnlocked.isEmpty()) {
				if(!worldIn.isRemote)
					playerIn.addChatComponentMessage(new TextComponentTranslation("psimisc.cantUseProgrammer").setStyle(new Style().setColor(TextFormatting.RED)));
				return true;
			}
		}
		
		EnumActionResult result = setSpell(worldIn, pos, playerIn, heldItem);
		if(result == EnumActionResult.SUCCESS)
			return true;

		boolean enabled = programmer.isEnabled();
		if(!enabled || programmer.playerLock.isEmpty())
			programmer.playerLock = playerIn.getName();

		if(playerIn instanceof EntityPlayerMP)
			VanillaPacketDispatcher.dispatchTEToPlayer(programmer, (EntityPlayerMP) playerIn);
		playerIn.openGui(Psi.instance, LibGuiIDs.PROGRAMMER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	public EnumActionResult setSpell(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack heldItem) {
		TileProgrammer programmer = (TileProgrammer) worldIn.getTileEntity(pos);

		boolean enabled = programmer.isEnabled();
		
		if(enabled && heldItem != null && heldItem.getItem() instanceof ISpellSettable && programmer.spell != null && playerIn.isSneaking() == ((ISpellSettable) heldItem.getItem()).requiresSneakForSpellSet(heldItem)) {
			if(programmer.canCompile()) {
				ISpellSettable settable = (ISpellSettable) heldItem.getItem();
				if(!worldIn.isRemote)
					worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, PsiSoundHandler.bulletCreate, SoundCategory.BLOCKS, 0.5F, 1F);

				programmer.spell.uuid = UUID.randomUUID();
				settable.setSpell(playerIn, heldItem, programmer.spell);
				if(playerIn instanceof EntityPlayerMP)
					VanillaPacketDispatcher.dispatchTEToPlayer(programmer, (EntityPlayerMP) playerIn);
				return EnumActionResult.SUCCESS;
			} else {
				if(!worldIn.isRemote)
					worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, PsiSoundHandler.compileError, SoundCategory.BLOCKS, 0.5F, 1F);
				return EnumActionResult.FAIL;
			}
		}
		
		return EnumActionResult.PASS;
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, ENABLED });
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
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
