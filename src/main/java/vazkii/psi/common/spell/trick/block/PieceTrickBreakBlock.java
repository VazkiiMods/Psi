/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeBlockState;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fluids.IFluidBlock;

import vazkii.psi.api.PsiAPI;
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

public class PieceTrickBreakBlock extends PieceTrick {

	public static ThreadLocal<Boolean> doingHarvestCheck = ThreadLocal.withInitial(() -> false);

	SpellParam<Vector3> position;

	public PieceTrickBreakBlock(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		meta.addStat(EnumSpellStat.POTENCY, 20);
		meta.addStat(EnumSpellStat.COST, 50);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		ItemStack tool = context.getHarvestTool();
		Vector3 positionVal = this.getParamValue(context, position);

		if (positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		if (!context.isInRadius(positionVal)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		BlockPos pos = positionVal.toBlockPos();
		removeBlockWithDrops(context, context.caster, context.focalPoint.getCommandSenderWorld(), tool, pos, true);

		return null;
	}

	public static void removeBlockWithDrops(SpellContext context, Player player, Level world, ItemStack tool, BlockPos pos, boolean particles) {
		if (!world.hasChunkAt(pos) || (context.positionBroken != null && pos.equals(new BlockPos(context.positionBroken.getLocation().x, context.positionBroken.getLocation().y, context.positionBroken.getLocation().z))) || !world.mayInteract(player, pos)) {
			return;
		}

		if (tool.isEmpty()) {
			tool = PsiAPI.getPlayerCAD(player);
		}

		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (!block.isAir(state, world, pos) && !(block instanceof IFluidBlock) && state.getDestroySpeed(world, pos) != -1) {
			if (!canHarvestBlock(state, player, world, pos, tool)) {
				return;
			}

			BreakEvent event = createBreakEvent(state, player, world, pos, tool);
			MinecraftForge.EVENT_BUS.post(event);
			if (!event.isCanceled()) {
				if (!player.abilities.instabuild) {
					BlockEntity tile = world.getBlockEntity(pos);

					if (block.removedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
						block.destroy(world, pos, state);
						block.playerDestroy(world, player, pos, state, tile, tool);
						if (world instanceof ServerLevel) {
							block.popExperience((ServerLevel) world, pos, event.getExpToDrop());
						}
					}
				} else {
					world.removeBlock(pos, false);
				}
			}

			if (particles) {
				world.levelEvent(2001, pos, Block.getId(state));
			}
		}
	}

	/**
	 * Based on {@link BreakEvent#BreakEvent(World, BlockPos, BlockState, PlayerEntity)}.
	 * Allows a tool that isn't your mainhand tool to harvest the blocks.
	 */
	public static BreakEvent createBreakEvent(BlockState state, Player player, Level world, BlockPos pos, ItemStack tool) {
		BreakEvent event = new BreakEvent(world, pos, state, player);
		if (state == null || !canHarvestBlock(state, player, world, pos, tool)) // Handle empty block or player unable to break block scenario
		{
			event.setExpToDrop(0);
		} else {
			int bonusLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
			int silklevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool);
			event.setExpToDrop(state.getExpDrop(world, pos, bonusLevel, silklevel));
		}
		return event;
	}

	/**
	 * Item stack aware harvest check
	 * Also sets global state {@link PieceTrickBreakBlock#doingHarvestCheck} to true during the check
	 * 
	 * @see IForgeBlockState#canHarvestBlock(IBlockReader, BlockPos, PlayerEntity)
	 */
	public static boolean canHarvestBlock(BlockState state, Player player, Level world, BlockPos pos, ItemStack stack) {
		// So the CAD can only be used as a tool when a harvest check is ongoing
		boolean wasChecking = doingHarvestCheck.get();
		doingHarvestCheck.set(true);

		// Swap the main hand with the stack temporarily to do the harvest check
		ItemStack oldHeldStack = player.getMainHandItem();
		//player.setHeldItem(EnumHand.MAIN_HAND, oldHeldStack);
		// Need to do this instead of the above to prevent the re-equip sound
		player.getInventory().items.set(player.getInventory().selected, stack);

		// Harvest check
		boolean canHarvest = state.canHarvestBlock(world, pos, player);

		// Swap back the main hand
		player.getInventory().items.set(player.getInventory().selected, oldHeldStack);

		// Reset the harvest check to its previous value
		doingHarvestCheck.set(wasChecking);
		return canHarvest;
	}
}
