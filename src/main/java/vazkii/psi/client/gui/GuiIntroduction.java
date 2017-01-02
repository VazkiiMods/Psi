/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [25/01/2016, 15:59:27 (GMT)]
 */
package vazkii.psi.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.client.core.helper.TextHelper;
import vazkii.psi.client.gui.button.GuiButtonBoolean;
import vazkii.psi.common.core.handler.PersistencyHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.message.MessageSkipToLevel;

public class GuiIntroduction extends GuiScreen {

	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_INTRODUCTION);

	int xSize, ySize, left, top;
	boolean skip = false;
	boolean returnToLeveling;
	
	public GuiIntroduction() {
		this(false);
	}
	
	public GuiIntroduction(boolean ret) {
		returnToLeveling = ret;
	}

	@Override
	public void initGui() {
		xSize = 256;
		ySize = 184;
		left = (width - xSize) / 2;
		top = (height - ySize) / 2;

		skip = PersistencyHandler.persistentLevel > 0;

		buttonList.clear();
		if(skip) {
			buttonList.add(new GuiButtonBoolean(width / 2 - 32, top + 145, true));
			buttonList.add(new GuiButtonBoolean(width / 2 + 20, top + 145, false));
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();

		GlStateManager.color(1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);

		super.drawScreen(mouseX, mouseY, partialTicks);

		if(LibMisc.BETA_TESTING) {
			String betaTest = I18n.translateToLocal("psimisc.wip");
			mc.fontRendererObj.drawStringWithShadow(betaTest, left + xSize / 2 - mc.fontRendererObj.getStringWidth(betaTest) / 2, top - 12, 0xFFFFFF);
		}

		TextHelper.renderText(width / 2 - 120, height / 2 - 30, 245, skip ? "psi.levelskip" : "psi.introduction", false, true, PersistencyHandler.persistentLevel);
		if(skip) {
			String loadPrompt = I18n.translateToLocal("psimisc.loadPrompt");
			mc.fontRendererObj.drawStringWithShadow(loadPrompt, left + xSize / 2 - mc.fontRendererObj.getStringWidth(loadPrompt) / 2, top + 133, 0xFFFFFF);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button instanceof GuiButtonBoolean) {
			GuiButtonBoolean bool = (GuiButtonBoolean) button;
			if(bool.yes) {
				MessageSkipToLevel message = new MessageSkipToLevel(PersistencyHandler.persistentLevel);
				NetworkHandler.INSTANCE.sendToServer(message);

				PlayerDataHandler.get(mc.player).skipToLevel(PersistencyHandler.persistentLevel);
				mc.displayGuiScreen(new GuiLeveling());
			} else {
				if(returnToLeveling)
					mc.displayGuiScreen(new GuiLeveling(true));
				
				skip = false;
				buttonList.clear();
			}
		}
	}

}
