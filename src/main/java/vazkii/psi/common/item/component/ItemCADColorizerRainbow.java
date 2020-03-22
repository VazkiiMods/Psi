package vazkii.psi.common.item.component;

import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.client.core.handler.ClientTickHandler;

import java.awt.*;

public class ItemCADColorizerRainbow extends ItemCADColorizer {
    public ItemCADColorizerRainbow(String name, Properties properties) {
        super(name, properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getColor(ItemStack stack) {
        float time = ClientTickHandler.total;
        float w = (float) (Math.sin(time * 0.4) * 0.5 + 0.5) * 0.1F;
        float r = (float) (Math.sin(time * 0.1) * 0.5 + 0.5) * 0.5F + 0.25F + w;
        float g = 0.5F + w;
        float b = 1F;

        return new Color((int) (r * 255), (int) (g * 255), (int) (b * 255)).getRGB();
    }
}
