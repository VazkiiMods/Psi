/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/01/2016, 23:01:02 (GMT)]
 */
package vazkii.psi.common.block;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.arl.block.BlockFacing;
import vazkii.arl.block.tile.TileSimpleInventory;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.IPsiBlock;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibGuiIDs;

public class BlockCADAssembler extends BlockFacing implements IPsiBlock {

	Random random;

	public BlockCADAssembler() {
		super(LibBlockNames.CAD_ASSEMBLER, Material.IRON);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);

		random = new Random();
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
	public void breakBlock(World par1World, BlockPos pos, IBlockState state) {
		TileSimpleInventory inv = (TileSimpleInventory) par1World.getTileEntity(pos);

		if(inv != null) {
			for(int j1 = 1; j1 < 7; ++j1) {
				ItemStack itemstack = inv.getStackInSlot(j1);

				if(itemstack != null) {
					float f = random.nextFloat() * 0.8F + 0.1F;
					float f1 = random.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;

					for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem)) {
						int k1 = random.nextInt(21) + 10;

						if (k1 > itemstack.stackSize)
							k1 = itemstack.stackSize;

						itemstack.stackSize -= k1;
						entityitem = new EntityItem(par1World, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (float)random.nextGaussian() * f3;
						entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float)random.nextGaussian() * f3;

						if(itemstack.hasTagCompound())
							entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}
				}
			}
		}

		super.breakBlock(par1World, pos, state);
	}

	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		playerIn.openGui(Psi.instance, LibGuiIDs.CAD_ASSEMBLER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public EnumRarity getBlockRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCADAssembler();
	}

}
