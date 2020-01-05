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

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.arl.block.BlockFacing;
import vazkii.arl.block.tile.TileSimpleInventory;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.IPsiBlock;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibGuiIDs;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Random;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class BlockCADAssembler extends BlockFacing implements IPsiBlock {

	final Random random;

	public BlockCADAssembler() {
		super(LibBlockNames.CAD_ASSEMBLER, Material.IRON);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);
		setCreativeTab(PsiCreativeTab.INSTANCE);

		random = new Random();
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullBlock(BlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(BlockState state) {
		return false;
	}

	@Override
	public void breakBlock(@Nonnull World par1World, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		TileSimpleInventory inv = (TileSimpleInventory) par1World.getTileEntity(pos);

		if(inv != null) {
			for(int j1 = 0; j1 < 6; ++j1) {
				ItemStack itemstack = inv.getStackInSlot(j1);

				if(!itemstack.isEmpty()) {
					float f = random.nextFloat() * 0.8F + 0.1F;
					float f1 = random.nextFloat() * 0.8F + 0.1F;
					ItemEntity entityitem;

					for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.getCount() > 0; par1World.spawnEntity(entityitem)) {
						int k1 = random.nextInt(21) + 10;

						if (k1 > itemstack.getCount())
							k1 = itemstack.getCount();

						itemstack.shrink(k1);
						entityitem = new ItemEntity(par1World, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (float)random.nextGaussian() * f3;
						entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float)random.nextGaussian() * f3;

						if(itemstack.hasTagCompound())
							entityitem.getItem().setTagCompound(ItemNBTHelper.getNBT(itemstack));
					}
				}
			}
		}

		super.breakBlock(par1World, pos, state);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile != null) {
			return tile.getCapability(ITEM_HANDLER_CAPABILITY)
					.map(ItemHandlerHelper::calcRedstoneFromInventory)
					.orElse(0);
		}

		return 0;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
		playerIn.openGui(Psi.instance, LibGuiIDs.CAD_ASSEMBLER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public Rarity getBlockRarity(ItemStack stack) {
		return Rarity.UNCOMMON;
	}

	@Override
	public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
		return new TileCADAssembler();
	}

}
