/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.component;

import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.client.core.handler.ColorHandler;
import vazkii.psi.common.core.handler.ContributorSpellCircleHandler;

import java.awt.*;
import java.util.Locale;

public class ItemCADColorizerPsi extends ItemCADColorizer {

	public ItemCADColorizerPsi(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getColor(ItemStack stack) {
		if(!getContributorName(stack).isEmpty() && ContributorSpellCircleHandler.isContributor(getContributorName(stack).toLowerCase(Locale.ROOT))) {
			return ColorHandler.slideColor(ContributorSpellCircleHandler.getColors(getContributorName(stack).toLowerCase(Locale.ROOT)), 0.0125f);
		}
		float time = ClientTickHandler.total;
		float w = (float) (Math.sin(time * 0.4) * 0.5 + 0.5) * 0.1F;
		float r = (float) (Math.sin(time * 0.1) * 0.5 + 0.5) * 0.5F + 0.25F + w;
		float g = 0.5F + w;
		float b = 1F;

		return new Color((int) (r * 255), (int) (g * 255), (int) (b * 255)).getRGB();
	}
}
