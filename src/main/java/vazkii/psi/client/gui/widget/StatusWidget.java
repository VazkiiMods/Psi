/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.widget;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.client.gui.GuiProgrammer;

public class StatusWidget extends AbstractWidget {

	private final GuiProgrammer parent;

	public StatusWidget(int x, int y, int width, int height, String message, GuiProgrammer programmer) {
		super(x, y, width, height, Component.nullToEmpty(message));
		this.parent = programmer;
	}

	@Override
	protected boolean isValidClickButton(int p_isValidClickButton_1_) {
		return false;
	}

	@Override
	public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float pTicks) {
		graphics.setColor(1f, 1f, 1f, 1F);
		graphics.blit(GuiProgrammer.texture, parent.left - 48, parent.top + 5, parent.xSize, 0, 48, 30);
		graphics.blit(GuiProgrammer.texture, parent.left - 16, parent.top + 13, parent.compileResult.right().isPresent() ? 12 : 0, parent.ySize + 28, 12, 12);

		if(mouseX > parent.left - 16 - 1 && mouseY > parent.top + 13 - 1 && mouseX < parent.left - 16 + 13 && mouseY < parent.top + 13 + 13) {
			if(parent.compileResult.right().isPresent()) {
				// no such thing as ifPresentOrElse in J8, sadly
				SpellCompilationException ex = parent.compileResult.right().get();
				parent.tooltip.add(Component.translatable("psimisc.errored").withStyle(ChatFormatting.RED));
				parent.tooltip.add(Component.translatable(ex.getMessage()).withStyle(ChatFormatting.GRAY));
				Pair<Integer, Integer> errorPos = ex.location;
				if(errorPos != null && errorPos.getRight() != -1 && errorPos.getLeft() != -1) {
					parent.tooltip.add(Component.literal("[" + GuiProgrammer.convertIntToLetter((errorPos.getLeft() + 1)) + ", " + (errorPos.getRight() + 1) + "]").withStyle(ChatFormatting.GRAY));
				}
			} else {
				parent.tooltip.add(Component.translatable("psimisc.compiled").withStyle(ChatFormatting.GREEN));
			}
		}

		ItemStack cad = PsiAPI.getPlayerCAD(parent.getMinecraft().player);
		if(!cad.isEmpty()) {
			int cadX = parent.left - 42;
			int cadY = parent.top + 12;

			graphics.renderFakeItem(cad, cadX, cadY);

			if(mouseX > cadX && mouseY > cadY && mouseX < cadX + 16 && mouseY < cadY + 16) {
				parent.tooltip.addAll(cad.getTooltipLines(Item.TooltipContext.of(parent.getMinecraft().level), parent.getMinecraft().player, parent.tooltipFlag));
			}
		}
	}

	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
		this.defaultButtonNarrationText(pNarrationElementOutput);
	}
}
