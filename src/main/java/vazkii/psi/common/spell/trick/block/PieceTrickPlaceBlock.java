/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.level.BlockEvent;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.StatLabel;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.client.core.handler.HUDHandler;
import vazkii.psi.common.block.base.DirectionBlockItemUseContext;
import vazkii.psi.common.block.base.ModBlocks;

public class PieceTrickPlaceBlock extends PieceTrick {

	SpellParam<Vector3> position;
	SpellParam<Vector3> direction;

	public PieceTrickPlaceBlock(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(8));
		setStatLabel(EnumSpellStat.COST, new StatLabel(8));
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(direction = new ParamVector(SpellParam.GENERIC_NAME_DIRECTION, SpellParam.GREEN, true, false));
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
		Vector3 directionVal = this.getParamValue(context, direction);

		Direction facing = Direction.NORTH;
		Direction horizontalFacing = Direction.NORTH;
		if(directionVal != null) {
			facing = Direction.getNearest(directionVal.x, directionVal.y, directionVal.z);
			horizontalFacing = Direction.getNearest(directionVal.x, 0.0, directionVal.z);
		}

		if(positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		if(!context.isInRadius(positionVal)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		BlockPos pos = positionVal.toBlockPos();
		placeBlock(context.caster, context.focalPoint.getCommandSenderWorld(), pos, context.getTargetSlot(), false, facing, horizontalFacing);

		return null;
	}

	public static void placeBlock(Player player, Level world, BlockPos pos, int slot, boolean particles, Direction direction, Direction horizontalDirection) {
		placeBlock(player, world, pos, slot, particles, false, direction, horizontalDirection);
	}

	public static void placeBlock(Player player, Level world, BlockPos pos, int slot, boolean particles, boolean conjure, Direction direction, Direction horizontalDirection) {
		if(!world.hasChunk(pos.getX(), pos.getY()) || !world.mayInteract(player, pos)) {
			return;
		}

		BlockState state = world.getBlockState(pos);
		BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(BlockSnapshot.create(world.dimension(), world, pos), world.getBlockState(pos.relative(Direction.UP)), player);
		MinecraftForge.EVENT_BUS.post(placeEvent);
		if(state.isAir() || state.getMaterial().isReplaceable() && !placeEvent.isCanceled()) {

			if(conjure) {

				world.setBlockAndUpdate(pos, ModBlocks.conjured.defaultBlockState());
			} else {
				ItemStack stack = player.getInventory().getItem(slot);
				if(!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
					ItemStack rem = removeFromInventory(player, stack, true);
					BlockItem iblock = (BlockItem) rem.getItem();

					ItemStack save;
					BlockHitResult hit = new BlockHitResult(Vec3.ZERO, direction, pos, false);
					UseOnContext ctx = new UseOnContext(player, InteractionHand.MAIN_HAND, hit);

					save = player.getItemInHand(ctx.getHand());
					player.setItemInHand(ctx.getHand(), rem);
					UseOnContext newCtx;
					newCtx = new UseOnContext(ctx.getPlayer(), ctx.getHand(), hit);
					player.setItemInHand(newCtx.getHand(), save);

					InteractionResult result = iblock.place(new DirectionBlockItemUseContext(newCtx, horizontalDirection));

					if(result != InteractionResult.FAIL) {
						removeFromInventory(player, stack, false);
						if(world.isClientSide()) {
							if(player.isCreative()) {
								HUDHandler.setRemaining(rem, -1);
							} else {
								HUDHandler.setRemaining(player, rem, null);
							}
						}
					}
				}
			}

			if(particles) {
				world.levelEvent(2001, pos, Block.getId(world.getBlockState(pos)));
			}
		}
	}

	public static ItemStack removeFromInventory(Player player, ItemStack stack, boolean copy) {
		if(player.isCreative()) {
			return stack.copy();
		}

		Inventory inv = player.getInventory();
		for(int i = inv.getContainerSize() - 1; i >= 0; i--) {
			ItemStack invStack = inv.getItem(i);
			if(!invStack.isEmpty() && invStack.sameItem(stack) && ItemStack.matches(stack, invStack)) {
				ItemStack retStack = invStack.copy();
				if(!copy) {
					invStack.shrink(1);
					if(invStack.getCount() == 0) {
						inv.setItem(i, ItemStack.EMPTY);
					}
				}
				return retStack;
			}
		}

		return ItemStack.EMPTY;
	}

}
