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
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.ConfigHandler;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PieceTrickBreakBlock extends PieceTrick {

	private static final List<List<ItemStack>> HARVEST_TOOLS_BY_LEVEL = List.of(
			stacks(Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_SHOVEL),
			stacks(Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_HOE, Items.STONE_SHOVEL),
			stacks(Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SHOVEL),
			stacks(Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_HOE, Items.DIAMOND_SHOVEL),
			stacks(Items.NETHERITE_PICKAXE, Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_SHOVEL)
	);
	public static ThreadLocal<Boolean> doingHarvestCheck = ThreadLocal.withInitial(() -> false);
	SpellParam<Vector3> position;

	public PieceTrickBreakBlock(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(20));
		setStatLabel(EnumSpellStat.COST, new StatLabel(50));
	}

	public static void removeBlockWithDrops(SpellContext context, Player player, Level world, ItemStack stack, BlockPos pos,
			Predicate<BlockState> filter) {
		if(stack.isEmpty()) {
			stack = PsiAPI.getPlayerCAD(player);
		}

		if(!world.hasChunkAt(pos)) {
			return;
		}

		BlockState blockstate = world.getBlockState(pos);
		boolean unminable = blockstate.getDestroySpeed(world, pos) == -1;

		if(!world.isClientSide && !unminable && filter.test(blockstate) && !blockstate.isAir()) {
			ItemStack save = player.getMainHandItem();
			boolean wasChecking = doingHarvestCheck.get();
			doingHarvestCheck.set(true);
			player.setItemInHand(InteractionHand.MAIN_HAND, stack);
			((ServerPlayer) player).connection.send(
					new ClientboundLevelEventPacket(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(blockstate), false));
			((ServerPlayer) player).gameMode.destroyBlock(pos);
			doingHarvestCheck.set(wasChecking);
			player.setItemInHand(InteractionHand.MAIN_HAND, save);
		}
	}

	/**
	 * Based on {//@link BreakEvent#BreakEvent(World, BlockPos, BlockState, PlayerEntity)}.
	 * Allows a tool that isn't your mainhand tool to harvest the blocks.
	 */
	public static BreakEvent createBreakEvent(BlockState state, Player player, Level world, BlockPos pos, ItemStack tool) {
		return new BreakEvent(world, pos, state, player);
	}

	/**
	 * Item stack aware harvest check
	 * Also sets global state {@link PieceTrickBreakBlock#doingHarvestCheck} to true during the check
	 * //@see IForgeBlockState#canHarvestBlock(IBlockReader, BlockPos, PlayerEntity)
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

	private static List<ItemStack> stacks(Item... items) {
		return Stream.of(items).map(ItemStack::new).collect(Collectors.toList());
	}

	public static boolean canHarvest(int harvestLevel, BlockState state) {
		return !getTool(harvestLevel, state).isEmpty();
	}

	private static ItemStack getTool(int harvestLevel, BlockState state) {
		if(!state.requiresCorrectToolForDrops()) {
			return HARVEST_TOOLS_BY_LEVEL.getFirst().getFirst();
		}

		int idx = Math.min(harvestLevel, HARVEST_TOOLS_BY_LEVEL.size() - 1);
		for(var tool : HARVEST_TOOLS_BY_LEVEL.get(idx)) {
			if(tool.isCorrectToolForDrops(state)) {
				return tool;
			}
		}

		return ItemStack.EMPTY;
	}

	//TODO Fix mining level on blocks that can be broken by hand.
	public static int getHarvestLevel(BlockState state) {
		if(Items.AIR.isCorrectToolForDrops(Items.AIR.getDefaultInstance(), state)) {
			return 0;
		}
		for(int i = 0; i < HARVEST_TOOLS_BY_LEVEL.size(); i++) {
			for(var tool : HARVEST_TOOLS_BY_LEVEL.get(i)) {
				if(tool.isCorrectToolForDrops(state)) {
					return i + 1;
				}
			}
		}
		return HARVEST_TOOLS_BY_LEVEL.size() + 1;
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

		if(positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		if(!context.isInRadius(positionVal)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		BlockPos pos = positionVal.toBlockPos();
		removeBlockWithDrops(context, context.caster, context.focalPoint.getCommandSenderWorld(), tool, pos, (v) -> tool.isCorrectToolForDrops(v) || canHarvest(ConfigHandler.COMMON.cadHarvestLevel.get(), v));

		return null;
	}
}
