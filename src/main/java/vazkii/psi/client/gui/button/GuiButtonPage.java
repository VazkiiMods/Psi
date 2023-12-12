/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import vazkii.psi.client.gui.GuiProgrammer;

public class GuiButtonPage extends Button {

	public final boolean right;
	final GuiProgrammer gui;

	public GuiButtonPage(int x, int y, boolean right, GuiProgrammer gui, Button.OnPress pressable) {
		super(x, y, 18, 10, Component.empty(), pressable, DEFAULT_NARRATION);
		this.gui = gui;
		this.right = right;
	}

	@Override
	public void render(GuiGraphics graphics, int par2, int par3, float pTicks) {
		if(active) {
			boolean hover = par2 >= getX() && par3 >= getY() && par2 < getX() + width && par3 < getY() + height;

			graphics.setColor(1F, 1F, 1F, 1F);
			graphics.blit(GuiProgrammer.texture, getX(), getY(), hover ? 216 : 198, right ? 145 : 155, width, height);

			if(hover) {
				gui.tooltip.add(Component.translatable(right ? "psimisc.next_page" : "psimisc.prev_page"));
			}
		}
	}

	public boolean isRight() {
		return right;
	}
}
