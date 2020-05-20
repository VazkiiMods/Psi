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
		return new Direction[]{rayTraceResult.getFace()};
	}
}
