/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.component;

import net.minecraft.world.item.ItemStack;

import vazkii.psi.common.client.PsiClientRuntime;

import java.awt.*;

public class ItemCADColorizerRainbow extends ItemCADColorizer {
	public ItemCADColorizerRainbow(Properties properties) {
		super(properties);
	}

	@Override
	public int getColor(ItemStack stack) {
		float time = PsiClientRuntime.clientTicks();
		return Color.HSBtoRGB(time * 0.005F, 1F, 1F);
	}
}
