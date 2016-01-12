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

import java.util.function.Consumer;

import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData.Deduction;
import vazkii.psi.common.lib.LibResources;

public final class HUDHandler {

	private static final ResourceLocation psiBar = new ResourceLocation(LibResources.GUI_PSI_BAR);
	private static final ResourceLocation psiBarMask = new ResourceLocation(LibResources.GUI_PSI_BAR_MASK);
	private static final ResourceLocation psiBarShatter = new ResourceLocation(LibResources.GUI_PSI_BAR_SHATTER);

	private static final int secondaryTextureUnit = 7;
	
	private static boolean registeredMask = false;

	@SubscribeEvent
	public void onDraw(RenderGameOverlayEvent.Post event) {
		if(event.type == ElementType.ALL) {
			drawPsiBar(event.resolution, event.partialTicks);
		}
	}
	
	public void drawPsiBar(ScaledResolution res, float pticks) {
		Minecraft mc = Minecraft.getMinecraft();
		boolean right = ConfigHandler.psiBarOnRight;
		
		int pad = 4;
		int width = 32;
		int height = 140;
		
		int x = -pad;
		if(right)
			x = res.getScaledWidth() + pad - width;
		
		int y = res.getScaledHeight() / 2 - height / 2;
		
		if(!registeredMask) {
			mc.renderEngine.bindTexture(psiBarMask);
			mc.renderEngine.bindTexture(psiBarShatter);
			registeredMask = true;
		}
		mc.renderEngine.bindTexture(psiBar);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, 64, 256);
		
		x += 8;
		y += 26;

		width = 16;
		height = 106;
		
		float r = 0.6F;
		float g = 0.65F;
		float b = 1F;
		
		int origHeight = height;
		int origY = y;
		int v = 0;
		PlayerData data = PlayerDataHandler.get(mc.thePlayer);
		int max = data.getTotalPsi();
		
		int texture = 0;
		boolean shaders = ShaderHandler.useShaders();

		if(shaders) {
			OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit);
			texture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		}
		
		GlStateManager.disableAlpha();
		for(Deduction d : data.deductions) {
			float a = d.getPercentile(pticks);
			GlStateManager.color(r, g, b, a);
			height = (int) Math.ceil((origHeight * (double) d.deduct / (double) max));
			int effHeight = (int) (origHeight * (double) d.current / (double) max);
			v = (origHeight - effHeight);
			y = origY + v;
			
			ShaderHandler.useShader(ShaderHandler.psiBar, generateCallback(a, d.shatter));
			Gui.drawModalRectWithCustomSizedTexture(x, y, 32, v, width, height, 64, 256);
		}
		GlStateManager.enableAlpha();
		
		float textY = origY;
		if(max > 0) {
			height = (int) ((double) origHeight * (double) data.availablePsi / (double) max);
			v = (origHeight - height);
			y = origY + v;
			
			if(data.availablePsi != data.lastAvailablePsi) {
				float textHeight = (float) ((double) origHeight * (data.availablePsi * pticks + data.lastAvailablePsi * (1.0 - pticks)) / (double) max);
				textY = origY + (origHeight - textHeight);
			} else textY = y;
		} else height = 0;
		
		GlStateManager.color(r, g, b);
		ShaderHandler.useShader(ShaderHandler.psiBar, generateCallback(1F, false));
		Gui.drawModalRectWithCustomSizedTexture(x, y, 32, v, width, height, 64, 256);
		ShaderHandler.releaseShader();
		
		if(shaders) {
			OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
			OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB);
		}
		
		GlStateManager.color(1F, 1F, 1F);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, textY, 0F);
		width = 44;
		height = 3;
		
		String s = "" + data.availablePsi;

		int offBar = 22;
		int offStr = 10 + mc.fontRendererObj.getStringWidth(s);
		if(!right) {
			offBar = 6;
			offStr = -26;
		}
		
		Gui.drawModalRectWithCustomSizedTexture(x - offBar, -2, 0, 140, width, height, 64, 256);
		mc.fontRendererObj.drawStringWithShadow(s, x - offStr, -11, 0xFFFFFF);
		GlStateManager.popMatrix();
	}
	
	private static Consumer<Integer> generateCallback(final float percentile, final boolean shatter) {
		Minecraft mc = Minecraft.getMinecraft();
		return (Integer shader) -> {
			int percentileUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "percentile");
			int imageUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "image");
			int maskUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "mask");

			OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(psiBar).getGlTextureId());
			ARBShaderObjects.glUniform1iARB(imageUniform, 0);

			OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit);

			GlStateManager.enableTexture2D();
			GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(shatter ? psiBarShatter : psiBarMask).getGlTextureId());
			ARBShaderObjects.glUniform1iARB(maskUniform, secondaryTextureUnit);

			ARBShaderObjects.glUniform1fARB(percentileUniform, percentile);
		};
	}
	
}
