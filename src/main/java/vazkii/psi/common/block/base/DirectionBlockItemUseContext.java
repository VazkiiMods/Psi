/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.base;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class DirectionBlockItemUseContext extends BlockPlaceContext {

	private final Direction horizontalFacing;

	public DirectionBlockItemUseContext(UseOnContext itemUseContext, Direction horizontalFacing) {
		super(itemUseContext);
		this.horizontalFacing = horizontalFacing;
	}

	@Override
	public @NotNull Direction getHorizontalDirection() {
		return horizontalFacing.getAxis() == Direction.Axis.Y ? Direction.NORTH : horizontalFacing;
	}

	@Override
	public @NotNull Direction getNearestLookingDirection() {
		return getHitResult().getDirection();
	}

	@Override
	public Direction @NotNull [] getNearestLookingDirections() {
        return switch (getHitResult().getDirection()) {
            default ->
                    new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP};
            case UP ->
                    new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
            case NORTH ->
                    new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.SOUTH};
            case SOUTH ->
                    new Direction[]{Direction.DOWN, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.NORTH};
            case WEST ->
                    new Direction[]{Direction.DOWN, Direction.WEST, Direction.SOUTH, Direction.UP, Direction.NORTH, Direction.EAST};
            case EAST ->
                    new Direction[]{Direction.DOWN, Direction.EAST, Direction.SOUTH, Direction.UP, Direction.NORTH, Direction.WEST};
        };
	}
}
