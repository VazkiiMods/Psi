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

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.util.ResourceLocation;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.client.core.helper.TextHelper;
import vazkii.psi.client.gui.button.GuiButtonBoolean;
import vazkii.psi.common.core.handler.PersistencyHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.message.MessageSkipToLevel;

public class GuiIntroduction extends Screen {

	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_INTRODUCTION);

	int xSize, ySize, left, top;
	boolean skip = false;
	final boolean returnToLeveling;
	
	public GuiIntroduction() {
		this(false);
	}
	
	public GuiIntroduction(boolean ret) {
		returnToLeveling = ret;
	}

	@Override
	public void init() {
		xSize = 256;
		ySize = 184;
		left = (width - xSize) / 2;
		top = (height - ySize) / 2;

		skip = PersistencyHandler.persistentLevel > 0;

		if(skip) {
			addButton(new GuiButtonBoolean(width / 2 - 32, top + 145, true));
			addButton(new GuiButtonBoolean(width / 2 + 20, top + 145, false));
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();

		GlStateManager.color3f(1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);
		blit(left, top, 0, 0, xSize, ySize);

		super.render(mouseX, mouseY, partialTicks);

		if(LibMisc.BETA_TESTING) {
			String betaTest = TooltipHelper.local("psimisc.wip");
			minecraft.fontRenderer.drawStringWithShadow(betaTest, left + xSize / 2f - mc.fontRenderer.getStringWidth(betaTest) / 2f, top - 12, 0xFFFFFF);
		}

		TextHelper.renderText(width / 2 - 120, height / 2 - 30, 245, skip ? "psi.levelskip" : "psi.introduction", false, true, PersistencyHandler.persistentLevel);
		if(skip) {
			String loadPrompt = TooltipHelper.local("psimisc.loadPrompt");
			mc.fontRenderer.drawStringWithShadow(loadPrompt, left + xSize / 2f - mc.fontRenderer.getStringWidth(loadPrompt) / 2f, top + 133, 0xFFFFFF);
		}
	}

	@Override
	protected void actionPerformed(Button button) {
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
