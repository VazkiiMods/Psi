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
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import org.apache.commons.lang3.tuple.Pair;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.client.gui.GuiProgrammer;

public class StatusWidget extends Widget {

	private final GuiProgrammer parent;

	public StatusWidget(int x, int y, int width, int height, String message, GuiProgrammer programmer) {
		super(x, y, width, height, ITextComponent.nullToEmpty(message));
		this.parent = programmer;
	}

	@Override
	protected boolean isValidClickButton(int p_isValidClickButton_1_) {
		return false;
	}

	@Override
	public void renderButton(MatrixStack ms, int mouseX, int mouseY, float pTicks) {
		RenderSystem.color3f(1f, 1f, 1f);
		parent.getMinecraft().getTextureManager().bind(GuiProgrammer.texture);
		blit(ms, parent.left - 48, parent.top + 5, parent.xSize, 0, 48, 30);
		blit(ms, parent.left - 16, parent.top + 13, parent.compileResult.right().isPresent() ? 12 : 0, parent.ySize + 28, 12, 12);

		if (mouseX > parent.left - 16 - 1 && mouseY > parent.top + 13 - 1 && mouseX < parent.left - 16 + 13 && mouseY < parent.top + 13 + 13) {
			if (parent.compileResult.right().isPresent()) {
				// no such thing as ifPresentOrElse in J8, sadly
				SpellCompilationException ex = parent.compileResult.right().get();
				parent.tooltip.add(new TranslationTextComponent("psimisc.errored").withStyle(TextFormatting.RED));
				parent.tooltip.add(new TranslationTextComponent(ex.getMessage()).withStyle(TextFormatting.GRAY));
				Pair<Integer, Integer> errorPos = ex.location;
				if (errorPos != null && errorPos.getRight() != -1 && errorPos.getLeft() != -1) {
					parent.tooltip.add(new StringTextComponent("[" + GuiProgrammer.convertIntToLetter((errorPos.getLeft() + 1)) + ", " + (errorPos.getRight() + 1) + "]").withStyle(TextFormatting.GRAY));
				}
			} else {
				parent.tooltip.add(new TranslationTextComponent("psimisc.compiled").withStyle(TextFormatting.GREEN));
			}
		}

		ItemStack cad = PsiAPI.getPlayerCAD(parent.getMinecraft().player);
		if (!cad.isEmpty()) {
			int cadX = parent.left - 42;
			int cadY = parent.top + 12;

			PsiRenderHelper.transferMsToGl(ms, () -> parent.getMinecraft().getItemRenderer().renderAndDecorateItem(cad, cadX, cadY));

			if (mouseX > cadX && mouseY > cadY && mouseX < cadX + 16 && mouseY < cadY + 16) {
				parent.tooltip.addAll(cad.getTooltipLines(parent.getMinecraft().player, parent.tooltipFlag));
			}
		}
	}
}
