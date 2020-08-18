/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import org.apache.commons.lang3.tuple.Pair;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.client.gui.GuiProgrammer;

public class StatusWidget extends Widget {

	private final GuiProgrammer parent;

	public StatusWidget(int x, int y, int width, int height, String message, GuiProgrammer programmer) {
		super(x, y, width, height, ITextComponent.func_244388_a(message));
		this.parent = programmer;
	}

	@Override
	protected boolean isValidClickButton(int p_isValidClickButton_1_) {
		return false;
	}

	@Override
	public void renderButton(MatrixStack ms, int mouseX, int mouseY, float pTicks) {
		RenderSystem.color3f(1f, 1f, 1f);
		parent.getMinecraft().getTextureManager().bindTexture(GuiProgrammer.texture);
		drawTexture(ms, parent.left - 48, parent.top + 5, parent.xSize, 0, 48, 30);
		drawTexture(ms, parent.left - 16, parent.top + 13, parent.compiler.isErrored() ? 12 : 0, parent.ySize + 28, 12, 12);

		if (mouseX > parent.left - 16 - 1 && mouseY > parent.top + 13 - 1 && mouseX < parent.left - 16 + 13 && mouseY < parent.top + 13 + 13) {
			if (parent.compiler.isErrored()) {
				parent.tooltip.add(new TranslationTextComponent("psimisc.errored").formatted(TextFormatting.RED));
				parent.tooltip.add(new TranslationTextComponent(parent.compiler.getError()).formatted(TextFormatting.GRAY));
				Pair<Integer, Integer> errorPos = parent.compiler.getErrorLocation();
				if (errorPos != null && errorPos.getRight() != -1 && errorPos.getLeft() != -1) {
					parent.tooltip.add(new StringTextComponent("[" + (errorPos.getLeft() + 1) + ", " + (errorPos.getRight() + 1) + "]").formatted(TextFormatting.GRAY));
				}
			} else {
				parent.tooltip.add(new TranslationTextComponent("psimisc.compiled").formatted(TextFormatting.GREEN));
			}
		}

		ItemStack cad = PsiAPI.getPlayerCAD(parent.getMinecraft().player);
		if (!cad.isEmpty()) {
			int cadX = parent.left - 42;
			int cadY = parent.top + 12;

			RenderSystem.enableRescaleNormal();
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(770, 771, 1, 0);
			RenderHelper.enable();
			parent.getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(cad, cadX, cadY);
			RenderHelper.disableStandardItemLighting();
			RenderSystem.disableRescaleNormal();
			RenderSystem.disableBlend();

			if (mouseX > cadX && mouseY > cadY && mouseX < cadX + 16 && mouseY < cadY + 16) {
				parent.tooltip.addAll(cad.getTooltip(parent.getMinecraft().player, parent.tooltipFlag));
			}
		}
	}
}
