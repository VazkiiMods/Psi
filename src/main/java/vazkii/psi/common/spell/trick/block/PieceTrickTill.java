/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
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
import vazkii.psi.common.core.helpers.SpellHelpers;

public class PieceTrickTill extends PieceTrick {
	SpellParam<Vector3> position;

	public PieceTrickTill(Spell spell) {
		super(spell);
	}

	public static ActionResultType tillBlock(PlayerEntity player, World world, BlockPos pos) {
		if (!world.isBlockLoaded(pos) || !world.isBlockModifiable(player, pos)) {
			return ActionResultType.PASS;
		}
		BlockRayTraceResult hit = new BlockRayTraceResult(Vec3d.ZERO, Direction.UP, pos, false);
		ItemStack save = player.getHeldItem(Hand.MAIN_HAND);
		player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.BONE_MEAL));
		ItemUseContext fakeContext = new ItemUseContext(player, Hand.MAIN_HAND, hit);
		player.setHeldItem(Hand.MAIN_HAND, save);
		return Items.IRON_HOE.onItemUse(fakeContext);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		meta.addStat(EnumSpellStat.COST, 10);
		meta.addStat(EnumSpellStat.POTENCY, 10);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		BlockPos pos = SpellHelpers.getBlockPos(this, context, position, true, false);

		return tillBlock(context.caster, context.caster.world, pos);
	}

}
