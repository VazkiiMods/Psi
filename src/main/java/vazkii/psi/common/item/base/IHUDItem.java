/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.base;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IHUDItem {

	@OnlyIn(Dist.CLIENT)
	void drawHUD(MatrixStack ms, MainWindow res, float partTicks, ItemStack stack);

}
