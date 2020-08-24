/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import vazkii.psi.client.gui.GuiProgrammer;

public class GuiButtonPage extends Button {

	public final boolean right;
	final GuiProgrammer gui;

	public GuiButtonPage(int x, int y, boolean right, GuiProgrammer gui) {
		super(x, y, 18, 10, StringTextComponent.EMPTY, button -> {});
		this.gui = gui;
		this.right = right;
	}

	public GuiButtonPage(int x, int y, boolean right, GuiProgrammer gui, Button.IPressable pressable) {
		super(x, y, 18, 10, StringTextComponent.EMPTY, pressable);
		this.gui = gui;
		this.right = right;
	}

	@Override
	public void renderButton(MatrixStack ms, int par2, int par3, float pTicks) {
		if (active) {
			boolean hover = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;

			Minecraft.getInstance().textureManager.bindTexture(GuiProgrammer.texture);
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			drawTexture(ms, x, y, hover ? 216 : 198, right ? 145 : 155, width, height);

			if (hover) {
				gui.tooltip.add(new TranslationTextComponent(right ? "psimisc.next_page" : "psimisc.prev_page"));
			}
		}
	}

	public boolean isRight() {
		return right;
	}
}
