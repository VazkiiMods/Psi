package vazkii.psi.common.item;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.exosuit.PsiArmorEvent;

public class ItemLightExosuitSensor extends ItemExosuitSensor {

	public ItemLightExosuitSensor(Properties properties) {
		super(properties);
	}

	@Override
	public String getEventType(ItemStack stack) {
		return PsiArmorEvent.LOW_LIGHT;
	}

	@Override
	public int getColor(ItemStack stack) {
		return ItemExosuitSensor.lightColor;
    }
}
