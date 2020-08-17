/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.base;

import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;

public class DirectionBlockItemUseContext extends BlockItemUseContext {

	public DirectionBlockItemUseContext(ItemUseContext itemUseContext) {
		super(itemUseContext);
	}

	@Override
	public Direction getPlacementHorizontalFacing() {
		return rayTraceResult.getFace();
	}

	@Override
	public Direction getNearestLookingDirection() {
		return rayTraceResult.getFace();
	}

	@Override
	public Direction[] getNearestLookingDirections() {
		return new Direction[] { rayTraceResult.getFace() };
	}
}
