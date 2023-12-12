/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.client.gui.GuiProgrammer;

public class GuiButtonHelp extends Button {

	final GuiProgrammer gui;

	public GuiButtonHelp(int x, int y, GuiProgrammer gui) {
		super(x, y, 12, 12, Component.empty(), button -> {}, DEFAULT_NARRATION);
		this.gui = gui;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float pTicks) {
		if(!gui.takingScreenshot) {
			boolean overHelp = mouseX > getX() && mouseY > getY() && mouseX < getX() + 12 && mouseY < getY() + 12;
			graphics.blit(GuiProgrammer.texture, getX(), getY(), gui.xSize + (overHelp ? 12 : 0), gui.ySize + 9, 12, 12);
			if(overHelp && !Screen.hasAltDown()) {
				gui.tooltip.add(Component.translatable("psimisc.programmer_help"));
				String ctrl = I18n.get(Minecraft.ON_OSX ? "psimisc.ctrl_mac" : "psimisc.ctrl_windows");
				TooltipHelper.tooltipIfShift(gui.tooltip, () -> {
					int i = 0;
					while(I18n.exists("psi.programmer_reference" + i)) {
						gui.tooltip.add(Component.translatable("psi.programmer_reference" + i++, ctrl));
					}
				});
			}
		}
	}
}
