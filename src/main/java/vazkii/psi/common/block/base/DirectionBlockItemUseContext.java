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

	private Direction horizontalFacing;

	public DirectionBlockItemUseContext(ItemUseContext itemUseContext, Direction horizontalFacing) {
		super(itemUseContext);
		this.horizontalFacing = horizontalFacing;
	}

	@Override
	public Direction getPlacementHorizontalFacing() {
		return horizontalFacing.getAxis() == Direction.Axis.Y ? Direction.NORTH : horizontalFacing;
	}

	@Override
	public Direction getNearestLookingDirection() {
		return rayTraceResult.getFace();
	}

	@Override
	public Direction[] getNearestLookingDirections() {
		switch(this.rayTraceResult.getFace()) {
		case DOWN:
		default:
			return new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP};
		case UP:
			return new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
		case NORTH:
			return new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.SOUTH};
		case SOUTH:
			return new Direction[]{Direction.DOWN, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.NORTH};
		case WEST:
			return new Direction[]{Direction.DOWN, Direction.WEST, Direction.SOUTH, Direction.UP, Direction.NORTH, Direction.EAST};
		case EAST:
			return new Direction[]{Direction.DOWN, Direction.EAST, Direction.SOUTH, Direction.UP, Direction.NORTH, Direction.WEST};
		}
	}
}
