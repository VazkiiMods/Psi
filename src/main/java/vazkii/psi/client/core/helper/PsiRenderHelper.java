/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [28/08/2016, 01:15:29 (GMT)]
 */
package vazkii.psi.client.core.helper;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import vazkii.psi.client.core.handler.ClientTickHandler;

public class PsiRenderHelper {

	public static void renderStar(int color, float xScale, float yScale, float zScale, long seed) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buff = tessellator.getBuffer();

		int ticks = ClientTickHandler.ticksInGame % 200;
		if (ticks >= 100)
			ticks = 200 - ticks - 1;

		float f1 = ticks / 200F;
		float f2 = 0F;
		if (f1 > 0.7F)
			f2 = (f1 - 0.7F) / 0.2F;
		Random random = new Random(seed);

		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 1);
		GlStateManager.disableAlpha();
		GlStateManager.enableCull();
		GlStateManager.depthMask(false);
		GlStateManager.scale(xScale, yScale, zScale);

		for (int i = 0; i < (f1 + f1 * f1) / 2F * 90F + 30F; i++) {
			GlStateManager.rotate(random.nextFloat() * 360F, 1F, 0F, 0F);
			GlStateManager.rotate(random.nextFloat() * 360F, 0F, 1F, 0F);
			GlStateManager.rotate(random.nextFloat() * 360F, 0F, 0F, 1F);
			GlStateManager.rotate(random.nextFloat() * 360F, 1F, 0F, 0F);
			GlStateManager.rotate(random.nextFloat() * 360F, 0F, 1F, 0F);
			GlStateManager.rotate(random.nextFloat() * 360F + f1 * 90F, 0F, 0F, 1F);
			buff.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
			float f3 = random.nextFloat() * 20F + 5F + f2 * 10F;
			float f4 = random.nextFloat() * 2F + 1F + f2 * 2F;
			int r = (color & 0xFF0000) >> 16;
			int g = (color & 0xFF00) >> 8;
			int b = color & 0xFF;
			buff.pos(0, 0, 0).color(r, g, b, 255F * (1F - f2)).endVertex();
			buff.pos(-0.866D * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex();
			buff.pos(0.866D * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex();
			buff.pos(0, f3, 1F * f4).color(0, 0, 0, 0).endVertex();
			buff.pos(-0.866D * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex();
			tessellator.draw();
		}

		GlStateManager.depthMask(true);
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		GlStateManager.popMatrix();
	}

	public static void renderProgressPie(int x, int y, float progress, ItemStack stack) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);

		GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GlStateManager.colorMask(false, false, false, false);
		GlStateManager.depthMask(false);
		GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xFF);
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);
		GL11.glStencilMask(0xFF);
		mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);

		int r = 10;
		int centerX = x + 8;
		int centerY = y + 8;
		int degs = (int) (360 * progress);
		float a = 0.5F + 0.2F * ((float) Math.cos((double) (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) / 10) * 0.5F + 0.5F);

		GlStateManager.disableLighting();
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.depthMask(true);
		GL11.glStencilMask(0x00);
		GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GlStateManager.color(0F, 0.5F, 0.5F, a);
		GL11.glVertex2i(centerX, centerY);
		GlStateManager.color(0F, 1F, 0.5F, a);
		for(int i = degs; i > 0; i--) {
			double rad = (i - 90) / 180F * Math.PI;
			GL11.glVertex2d(centerX + Math.cos(rad) * r, centerY + Math.sin(rad) * r);
		}
		GL11.glVertex2i(centerX, centerY);
		GL11.glEnd();
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}
	
}
