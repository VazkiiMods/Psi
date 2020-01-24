package vazkii.psi.common.item;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.exosuit.PsiArmorEvent;

public class ItemStressExosuitSensor extends ItemExosuitSensor {

    public ItemStressExosuitSensor(String name, Properties properties) {
        super(name, properties);
    }

    @Override
    public int getColor(ItemStack stack) {
        return ItemExosuitSensor.lowHealthColor;
    }

    @Override
    public String getEventType(ItemStack stack) {
        return PsiArmorEvent.LOW_HP;
    }
}
