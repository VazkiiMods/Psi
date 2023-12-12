/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.button;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import vazkii.psi.client.gui.GuiProgrammer;

public class GuiButtonIO extends Button {

	public final boolean out;
	final GuiProgrammer gui;

	public GuiButtonIO(int x, int y, boolean out, GuiProgrammer gui, OnPress pressable) {
		super(x, y, 12, 12, Component.empty(), pressable, DEFAULT_NARRATION);
		this.out = out;
		this.gui = gui;
	}

	@Override
	public void render(GuiGraphics graphics, int par2, int par3, float pticks) {
		if(active && !gui.takingScreenshot) {
			boolean hover = par2 >= getX() && par3 >= getY() && par2 < getX() + width && par3 < getY() + height;

			graphics.setColor(1F, 1F, 1F, 1F);
			graphics.blit(GuiProgrammer.texture, getX(), getY(), hover ? 186 : 174, out ? 169 : 181, width, height);

			if(hover) {
				String key = out ? "psimisc.export_to_clipboard" : "psimisc.import_from_clipboard";
				ChatFormatting color = out ? ChatFormatting.RED : ChatFormatting.BLUE;
				Component tip = Component.translatable(key).withStyle(color);
				gui.tooltip.add(tip);
				gui.tooltip.add(Component.translatable("psimisc.must_hold_shift").withStyle(ChatFormatting.GRAY));
			}
		}
	}

}
