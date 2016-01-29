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

import java.awt.Color;
import java.util.function.Consumer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.client.gui.GuiLeveling;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData.Deduction;
import vazkii.psi.common.lib.LibObfuscation;
import vazkii.psi.common.lib.LibResources;

public final class HUDHandler {

	private static final ResourceLocation psiBar = new ResourceLocation(LibResources.GUI_PSI_BAR);
	private static final ResourceLocation psiBarMask = new ResourceLocation(LibResources.GUI_PSI_BAR_MASK);
	private static final ResourceLocation psiBarShatter = new ResourceLocation(LibResources.GUI_PSI_BAR_SHATTER);

	private static final int secondaryTextureUnit = 7;
	
	private static boolean registeredMask = false;
	
	public static boolean showLevelUp = false;
	public static int levelDisplayTime = 0;
	public static int levelValue = 0;
	
	@SubscribeEvent
	public void onDraw(RenderGameOverlayEvent.Post event) {
		if(event.type == ElementType.ALL) {
			drawPsiBar(event.resolution, event.partialTicks);
			renderSocketableEquippedName(event.resolution, event.partialTicks);
			renderLevelUpIndicator(event.resolution, event.partialTicks);
		}
	}
	
	public void drawPsiBar(ScaledResolution res, float pticks) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack cadStack = PsiAPI.getPlayerCAD(mc.thePlayer);
		
		if(cadStack == null)
			return;
		
		ICAD cad = (ICAD) cadStack.getItem();
		PlayerData data = PlayerDataHandler.get(mc.thePlayer);
		if(data.level == 0)
			return;
		
		boolean right = ConfigHandler.psiBarOnRight;
		
		int pad = 3;
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
		int max = data.getTotalPsi();
		
		int texture = 0;
		boolean shaders = ShaderHandler.useShaders();

		if(shaders) {
			OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit);
			texture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		}
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
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
		
		String s1 = "" + data.availablePsi;
		String s2 = "" + cad.getStoredPsi(cadStack);

		int offBar = 22;
		int offStr1 = 7 + mc.fontRendererObj.getStringWidth(s1);
		int offStr2 = 7 + mc.fontRendererObj.getStringWidth(s2);
		
		if(!right) {
			offBar = 6;
			offStr1 = -23;
			offStr2 = -23;
		}
		
		Color color = new Color(cad.getSpellColor(cadStack));
		GlStateManager.color((float) color.getRed() / 255F, (float) color.getGreen() / 255F, (float) color.getBlue() / 255F);
		
		Gui.drawModalRectWithCustomSizedTexture(x - offBar, -2, 0, 140, width, height, 64, 256);
		mc.fontRendererObj.drawStringWithShadow(s1, x - offStr1, -11, 0xFFFFFF);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, Math.max(textY + 3, origY + 100), 0F);
		mc.fontRendererObj.drawStringWithShadow(s2, x - offStr2, 0, 0xFFFFFF);
		GlStateManager.popMatrix();
	}
	
	private void renderSocketableEquippedName(ScaledResolution res, float pticks) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
		String name = ISocketable.getSocketedItemName(stack, "");
		if(name == null || name.trim().isEmpty())
			return;
		
		int ticks = ReflectionHelper.getPrivateValue(GuiIngame.class, mc.ingameGUI, LibObfuscation.REMAINING_HIGHLIGHT_TICKS);
		ticks -= 10;
		
		if(ticks > 0) {
			int alpha = Math.min(255, (int) ((ticks + pticks) * 256.0F / 10.0F));
			int color = ICADColorizer.DEFAULT_SPELL_COLOR + (alpha << 24);

			int x = res.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(name) / 2;
			int y = res.getScaledHeight() - 71;
			if(mc.thePlayer.capabilities.isCreativeMode)
				y += 14;

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			mc.fontRendererObj.drawStringWithShadow(name, x, y, color);
			GlStateManager.disableBlend();
		}
	}
	
	public static void levelUp(int level) {
		levelValue = level; 
		levelDisplayTime = 0;
		showLevelUp = true;
	}
	
	private void renderLevelUpIndicator(ScaledResolution res, float pticks) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.currentScreen instanceof GuiLeveling)
			showLevelUp = false;
		
		if(!showLevelUp)
			return;
		
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		int time = 100;
		int fadeTime = (time / 10);
		int fadeoutTime = fadeTime * 2;

		String levelUp = StatCollector.translateToLocal("psimisc.levelup");
		int len = levelUp.length();
		int effLen = Math.min(len, len * (levelDisplayTime) / fadeTime);
		levelUp = levelUp.substring(0, effLen);
		
		int swidth = mc.fontRendererObj.getStringWidth(levelUp);
		int x = res.getScaledWidth() / 4 - swidth / 2; 
		int y = 25;
		float a = 1F - Math.max(0F, Math.min(1F, (float) (levelDisplayTime - time) / fadeoutTime));
		int alphaOverlay = ((int) (a * 0xFF) << 24); 

		GlStateManager.pushMatrix();
		GlStateManager.scale(2F, 2F, 2F);
		mc.fontRendererObj.drawStringWithShadow(levelUp, x, y, 0x0013C5FF + alphaOverlay);
		
		String currLevel = "" + levelValue;
		swidth = mc.fontRendererObj.getStringWidth(currLevel);
		x = res.getScaledWidth() / 4;
		y += 10;
		
		if(levelDisplayTime > fadeTime) {
			if(levelDisplayTime - fadeTime == 1)
				mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("psi:levelUp"), 0.5F));
				
			float a1 = Math.min(1F, (float) (levelDisplayTime - fadeTime) / fadeTime) * a;
			int color1 = 0x00FFFFFF + ((int) (a1 * 0xFF) << 24); 
			mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.GOLD + currLevel, x, y, color1);
		}
		GlStateManager.popMatrix();
		
		if(levelDisplayTime > fadeTime * 2) {
			String s = StatCollector.translateToLocal("psimisc.levelUpInfo1");
			swidth = mc.fontRendererObj.getStringWidth(s);
			len = s.length();
			effLen = Math.min(len, len * (levelDisplayTime - fadeTime * 2) / fadeTime);
			s = s.substring(0, effLen);
			x = res.getScaledWidth() / 2 - swidth / 2; 
			y += 65;
			
			mc.fontRendererObj.drawStringWithShadow(s, x, y, 0x00FFFFFF + alphaOverlay);
		}
		
		if(levelDisplayTime > fadeTime * 3) {
			String s = StatCollector.translateToLocal("psimisc.levelUpInfo2");
			s = String.format(s, EnumChatFormatting.GREEN + Keyboard.getKeyName(KeybindHandler.keybind.getKeyCode()) + EnumChatFormatting.RESET);
			swidth = mc.fontRendererObj.getStringWidth(s);
			len = s.length();
			effLen = Math.min(len, len * (levelDisplayTime - fadeTime * 3) / fadeTime);
			s = s.substring(0, effLen);
			x = res.getScaledWidth() / 2 - swidth / 2; 
			y += 10;
			
			mc.fontRendererObj.drawStringWithShadow(s, x, y, 0x00FFFFFF + alphaOverlay);
		}
		
		GlStateManager.enableAlpha();
		if(levelValue > 1 && levelDisplayTime >= (time + fadeoutTime))
			showLevelUp = false;
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
