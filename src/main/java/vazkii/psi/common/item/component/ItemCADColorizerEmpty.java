package vazkii.psi.common.item.component;

import net.minecraft.item.ItemStack;

public class ItemCADColorizerEmpty extends ItemCADColorizer {

	public ItemCADColorizerEmpty(Properties properties) {
		super(properties);
	}

	@Override
	public int getColor(ItemStack stack) {
		return 0x080808;
	}
}
