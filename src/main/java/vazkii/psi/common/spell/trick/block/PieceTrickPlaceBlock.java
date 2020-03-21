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
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.client.core.handler.HUDHandler;
import vazkii.psi.common.block.base.ModBlocks;

public class PieceTrickPlaceBlock extends PieceTrick {

	SpellParam<Vector3> position;

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
		Vector3 positionVal = this.getParamValue(context, position);

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		if(!context.isInRadius(positionVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		BlockPos pos = positionVal.toBlockPos();
		placeBlock(context.caster, context.caster.getEntityWorld(), pos, context.getTargetSlot(), false);

		return null;
	}

	public static void placeBlock(PlayerEntity player, World world, BlockPos pos, int slot, boolean particles) {
		placeBlock(player, world, pos, slot, particles, false);
	}

	public static void placeBlock(PlayerEntity player, World world, BlockPos pos, int slot, boolean particles, boolean conjure) {
		if(!world.isBlockLoaded(pos) || !world.isBlockModifiable(player, pos))
			return;
		
		BlockState state = world.getBlockState(pos);
		if(state.isAir(world, pos) || state.getMaterial().isReplaceable()) {
			if(conjure) {
				world.setBlockState(pos, ModBlocks.conjured.getDefaultState());
			} else {
				ItemStack stack = player.inventory.getStackInSlot(slot);
				if(!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                    ItemStack rem = removeFromInventory(player, stack);
                    BlockItem iblock = (BlockItem) rem.getItem();

                    ItemStack save = ItemStack.EMPTY;
                    BlockRayTraceResult hit = new BlockRayTraceResult(Vec3d.ZERO, Direction.UP, pos, false);
                    ItemUseContext ctx = new ItemUseContext(player, Hand.MAIN_HAND, hit);

                    save = player.getHeldItem(ctx.getHand());
                    player.setHeldItem(ctx.getHand(), rem);
                    ItemUseContext newCtx;
                    newCtx = new ItemUseContext(ctx.getPlayer(), ctx.getHand(), hit);
                    player.setHeldItem(newCtx.getHand(), save);

                    iblock.tryPlace(new BlockItemUseContext(newCtx));

                    if (player.abilities.isCreativeMode)
                        HUDHandler.setRemaining(rem, -1);
                    else HUDHandler.setRemaining(player, rem, null);
                }
			}

			if(particles)
				world.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)));
		}
	}

	public static ItemStack removeFromInventory(PlayerEntity player, ItemStack stack) {
		if(player.abilities.isCreativeMode)
			return stack.copy();

		PlayerInventory inv = player.inventory;
		for(int i = inv.getSizeInventory() - 1; i >= 0; i--) {
			ItemStack invStack = inv.getStackInSlot(i);
			if(!invStack.isEmpty() && invStack.isItemEqual(stack) && ItemStack.areItemStacksEqual(stack, invStack)) {
				ItemStack retStack = invStack.copy();
				invStack.shrink(1);
				if(invStack.getCount() == 0)
					inv.setInventorySlotContents(i, ItemStack.EMPTY);
				return retStack;
			}
		}

		return ItemStack.EMPTY;
	}

}
