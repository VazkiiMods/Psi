/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [24/01/2016, 17:14:15 (GMT)]
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.client.core.handler.HUDHandler;

public class PieceTrickPlaceBlock extends PieceTrick {

	SpellParam position;

	public PieceTrickPlaceBlock(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		meta.addStat(EnumSpellStat.POTENCY, 8);
		meta.addStat(EnumSpellStat.COST, 8);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.<Vector3>getParamValue(context, position);

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		if(!context.isInRadius(positionVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);
		placeBlock(context.caster, context.caster.worldObj, pos, false);

		return null;
	}

	public static void placeBlock(EntityPlayer player, World world, BlockPos pos, boolean particles) {
		if(!world.isBlockLoaded(pos))
			return;

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if(block == null || block.isAir(world, pos) || block.isReplaceable(world, pos)) {
			int slot = player.inventory.currentItem;
			if(slot == 9)
				return;

			ItemStack stack = player.inventory.getStackInSlot(slot + 1);
			if(stack != null && stack.getItem() instanceof ItemBlock) {
				ItemStack rem = removeFromInventory(player, block, stack);
				Block blockToPlace = Block.getBlockFromItem(rem.getItem());
				if(!world.isRemote)
					world.setBlockState(pos, blockToPlace.getStateFromMeta(rem.getItemDamage()));

				if(player.capabilities.isCreativeMode)
					HUDHandler.setRemaining(rem, -1);
				else HUDHandler.setRemaining(player, rem, null);
			}

			if(particles && !world.isRemote)
				world.playAuxSFX(2001, pos, Block.getStateId(world.getBlockState(pos)));
		}
	}

	public static ItemStack removeFromInventory(EntityPlayer player, Block block, ItemStack stack) {
		if(player.capabilities.isCreativeMode)
			return stack.copy();

		InventoryPlayer inv = player.inventory;
		for(int i = inv.getSizeInventory() - 1; i >= 0; i--) {
			ItemStack invStack = inv.getStackInSlot(i);
			if(invStack != null && invStack.isItemEqual(stack)) {
				ItemStack retStack = invStack.copy();
				invStack.stackSize--;
				if(invStack.stackSize == 0)
					inv.setInventorySlotContents(i, null);
				return retStack;
			}
		}

		return null;
	}

}
