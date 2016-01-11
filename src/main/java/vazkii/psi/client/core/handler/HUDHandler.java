/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [11/01/2016, 00:30:54 (GMT)]
 */
package vazkii.psi.client.core.handler;

import javax.annotation.Resource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.lib.LibResources;

public final class HUDHandler {

	private static final ResourceLocation psiBar = new ResourceLocation(LibResources.GUI_PSI_BAR);
	
	@SubscribeEvent
	public void onDraw(RenderGameOverlayEvent.Post event) {
		if(event.type == ElementType.ALL) {
			drawPsiBar(event.resolution);
		}
	}
	
	public void drawPsiBar(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		boolean right = true;
		
		int pad = 4;
		int width = 32;
		int height = 140;
		
		int x = -pad;
		if(right)
			x = res.getScaledWidth() + pad - width;
		
		int y = res.getScaledHeight() / 2 - height / 2;
		
		mc.renderEngine.bindTexture(psiBar);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, 256, 256);
		
		x += 8;
		y += 26;

		width = 16;
		height = 106;
		
		PlayerData data = PlayerDataHandler.get(mc.thePlayer);
		int max = data.getTotalPsi();
		if(max > 0) {
			int origHeight = height;
			height *= (double) data.availablePsi / (double) max;
			y += (origHeight - height);
		} else height = 0;
		
		GlStateManager.color(0F, 1F, 1F);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 32, 0, width, height, 256, 256);
		
		String s = "" + data.availablePsi;
		mc.fontRendererObj.drawString(s, x - 10 - mc.fontRendererObj.getStringWidth(s), y, 0xFFFFFF);
	}
	
}
