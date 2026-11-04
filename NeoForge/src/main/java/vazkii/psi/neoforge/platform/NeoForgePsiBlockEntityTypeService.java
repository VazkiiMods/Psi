/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.psi.common.platform.PsiBlockEntityTypeService;

import java.util.function.BiFunction;

public final class NeoForgePsiBlockEntityTypeService implements PsiBlockEntityTypeService {

	@Override
	public <T extends BlockEntity> BlockEntityType<T> create(
			BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
		return BlockEntityType.Builder.of(factory::apply, blocks).build(null);
	}

}
