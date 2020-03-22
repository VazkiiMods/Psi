package vazkii.psi.common.item.component;

import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.client.core.handler.ClientTickHandler;

import java.awt.*;

public class ItemCADColorizerPsi extends ItemCADColorizer {

	public ItemCADColorizerPsi(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getColor(ItemStack stack) {
		float time = ClientTickHandler.total;
		return Color.HSBtoRGB(time * 0.005F, 1F, 1F);
	}
}
