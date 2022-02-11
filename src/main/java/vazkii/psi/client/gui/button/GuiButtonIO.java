/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.button;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;

import vazkii.psi.client.gui.GuiProgrammer;

import net.minecraft.client.gui.components.Button.OnPress;

public class GuiButtonIO extends Button {

	public final boolean out;
	final GuiProgrammer gui;

	public GuiButtonIO(int x, int y, boolean out, GuiProgrammer gui) {
		super(x, y, 12, 12, TextComponent.EMPTY, button -> {});
		this.out = out;
		this.gui = gui;
	}

	public GuiButtonIO(int x, int y, boolean out, GuiProgrammer gui, OnPress pressable) {
		super(x, y, 12, 12, TextComponent.EMPTY, pressable);
		this.out = out;
		this.gui = gui;
	}

	@Override
	public void renderButton(PoseStack ms, int par2, int par3, float pticks) {
		if (active && !gui.takingScreenshot) {
			boolean hover = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;

			Minecraft.getInstance().textureManager.bindForSetup(GuiProgrammer.texture);
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			blit(ms, x, y, hover ? 186 : 174, out ? 169 : 181, width, height);

			if (hover) {
				String key = out ? "psimisc.export_to_clipboard" : "psimisc.import_from_clipboard";
				ChatFormatting color = out ? ChatFormatting.RED : ChatFormatting.BLUE;
				Component tip = new TranslatableComponent(key).withStyle(color);
				gui.tooltip.add(tip);
				gui.tooltip.add(new TranslatableComponent("psimisc.must_hold_shift").withStyle(ChatFormatting.GRAY));
			}
		}
	}

}
