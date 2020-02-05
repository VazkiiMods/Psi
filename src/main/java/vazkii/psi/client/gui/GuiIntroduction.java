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

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.client.core.helper.TextHelper;
import vazkii.psi.client.gui.button.GuiButtonBoolean;
import vazkii.psi.common.core.handler.PersistencyHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.MessageRegister;
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
        super(new StringTextComponent(""));
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
            addButton(new GuiButtonBoolean(width / 2 - 32, top + 145, true, button -> {
                MessageSkipToLevel message = new MessageSkipToLevel(PersistencyHandler.persistentLevel);
                MessageRegister.HANDLER.sendToServer(message);

                PlayerDataHandler.get(minecraft.player).skipToLevel(PersistencyHandler.persistentLevel);
                minecraft.displayGuiScreen(new GuiLeveling());
            }));
            addButton(new GuiButtonBoolean(width / 2 + 20, top + 145, false, button -> {
                if (returnToLeveling)
                    minecraft.displayGuiScreen(new GuiLeveling(true));

                skip = false;
                children.removeAll(buttons);
                buttons.clear();
            }));
        }
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();

        RenderSystem.color3f(1F, 1F, 1F);
        minecraft.getTextureManager().bindTexture(texture);
        blit(left, top, 0, 0, xSize, ySize);

        super.render(mouseX, mouseY, partialTicks);

        if (LibMisc.BETA_TESTING) {
            String betaTest = I18n.format("psimisc.wip");
            minecraft.fontRenderer.drawStringWithShadow(betaTest, left + xSize / 2f - minecraft.fontRenderer.getStringWidth(betaTest) / 2f, top - 12, 0xFFFFFF);
        }

        TextHelper.renderText(width / 2 - 120, height / 2 - 30, 245, skip ? "psi.levelskip" : "psi.introduction", false, true, PersistencyHandler.persistentLevel);
		if(skip) {
            String loadPrompt = I18n.format("psimisc.loadPrompt");
            minecraft.fontRenderer.drawStringWithShadow(loadPrompt, left + xSize / 2f - minecraft.fontRenderer.getStringWidth(loadPrompt) / 2f, top + 133, 0xFFFFFF);
        }
	}


}
