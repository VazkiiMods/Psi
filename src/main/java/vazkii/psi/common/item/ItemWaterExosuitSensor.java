package vazkii.psi.common.item;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.exosuit.PsiArmorEvent;

public class ItemWaterExosuitSensor extends ItemExosuitSensor {

	public ItemWaterExosuitSensor(Properties properties) {
		super(properties);
	}

	@Override
	public int getColor(ItemStack stack) {
		return ItemExosuitSensor.underwaterColor;
	}

	@Override
	public String getEventType(ItemStack stack) {
		return PsiArmorEvent.UNDERWATER;
    }
}
