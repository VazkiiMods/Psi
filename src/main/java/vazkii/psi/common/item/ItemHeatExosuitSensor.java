package vazkii.psi.common.item;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.exosuit.PsiArmorEvent;

public class ItemHeatExosuitSensor extends ItemExosuitSensor {

    public ItemHeatExosuitSensor(String name, Properties properties) {
        super(name, properties);
    }

    @Override
    public int getColor(ItemStack stack) {
        return ItemExosuitSensor.fireColor;
    }

    @Override
    public String getEventType(ItemStack stack) {
        return PsiArmorEvent.ON_FIRE;
    }
}
