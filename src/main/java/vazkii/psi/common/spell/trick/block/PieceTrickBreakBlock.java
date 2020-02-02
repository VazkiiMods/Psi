/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [24/01/2016, 15:35:27 (GMT)]
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fluids.IFluidBlock;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.ConfigHandler;

public class PieceTrickBreakBlock extends PieceTrick {

	SpellParam position;

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
		meta.addStat(EnumSpellStat.COST, 25);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		if(!context.isInRadius(positionVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		BlockPos pos = positionVal.toBlockPos();
		removeBlockWithDrops(context, context.caster, context.caster.getEntityWorld(), context.tool, pos, true);

		return null;
	}

	public static void removeBlockWithDrops(SpellContext context, PlayerEntity player, World world, ItemStack tool, BlockPos pos, boolean particles) {
        if (!world.isBlockLoaded(pos) || (context.positionBroken != null && pos.equals(new BlockPos(context.positionBroken.getHitVec().x, context.positionBroken.getHitVec().y, context.positionBroken.getHitVec().z))) || !world.isBlockModifiable(player, pos))
            return;

        if (tool.isEmpty())
            tool = PsiAPI.getPlayerCAD(player);

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!block.isAir(state, world, pos) && !(block instanceof IFluidBlock) && state.getPlayerRelativeBlockHardness(player, world, pos) > 0) {
            if (!canHarvestBlock(block, player, world, pos, tool))
                return;

            BreakEvent event = createBreakEvent(state, player, world, pos, tool);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                if (!player.abilities.isCreativeMode) {
                    TileEntity tile = world.getTileEntity(pos);

                    if (block.removedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
						block.onPlayerDestroy(world, pos, state);
                        block.harvestBlock(world, player, pos, state, tile, tool);
                    }
                } else world.removeBlock(pos, false);
            }

            if (particles)
                world.playEvent(2001, pos, Block.getStateId(state));
        }
    }

    // Based on BreakEvent::new, allows a tool that isn't your mainhand tool to harvest the blocks
    public static BreakEvent createBreakEvent(BlockState state, PlayerEntity player, World world, BlockPos pos, ItemStack tool) {
        BreakEvent event = new BreakEvent(world, pos, state, player);
        if (state == null || !ForgeHooks.canHarvestBlock(state, player, world, pos)) // Handle empty block or player unable to break block scenario
        {
            event.setExpToDrop(0);
        } else {
            int bonusLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
            int silklevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool);
            event.setExpToDrop(state.getExpDrop(world, pos, bonusLevel, silklevel));
        }
        return event;
    }

	// todo 1.14 get rid of all this and just return cad harvest level from cad item code (possibly with a threadlocal hack to indicate this trick is in progress)
	public static boolean canHarvestBlock(Block block, PlayerEntity player, World world, BlockPos pos, ItemStack tool) {
        //General positive checks
        BlockState state = world.getBlockState(pos);
        int reqLevel = block.getHarvestLevel(state);
        Item toolItem = tool.getItem();
        if (tool.canHarvestBlock(state) || state.getMaterial().isToolNotRequired() || ConfigHandler.COMMON.cadHarvestLevel.get() >= reqLevel)
            return ForgeEventFactory.doPlayerHarvestCheck(player, state, true);

        //General negative checks
        ToolType reqTool = block.getHarvestTool(state);
        if (toolItem == Items.AIR || reqTool == null) return false;

        //Targeted tool check
        if (toolItem.getHarvestLevel(tool, reqTool, player, state) >= reqLevel)
            return ForgeEventFactory.doPlayerHarvestCheck(player, state, true);

        return false;
    }
}
