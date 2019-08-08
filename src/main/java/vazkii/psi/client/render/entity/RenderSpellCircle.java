/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [30/01/2016, 16:42:31 (GMT)]
 */
package vazkii.psi.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.common.Psi;
import vazkii.psi.common.entity.EntitySpellCircle;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;

public class RenderSpellCircle extends EntityRenderer<EntitySpellCircle> {

	private static final ResourceLocation[] layers = new ResourceLocation[] {
			new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, 0)),
			new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, 1)),
			new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, 2)),
	};

	private static final float BRIGHTNESS_FACTOR = 0.7F;

	public RenderSpellCircle(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(@Nonnull EntitySpellCircle entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
		ItemStack colorizer = entity.getDataManager().get(EntitySpellCircle.COLORIZER_DATA);
		if(!colorizer.isEmpty() && colorizer.getItem() instanceof ICADColorizer)
			colorVal = Psi.proxy.getColorForColorizer(colorizer);
		float alive = entity.getTimeAlive() + partialTicks;
		float s1 = Math.min(1F, alive / EntitySpellCircle.CAST_DELAY);
		if(alive > EntitySpellCircle.LIVE_TIME - EntitySpellCircle.CAST_DELAY)
			s1 = 1F - Math.min(1F, Math.max(0, alive - (EntitySpellCircle.LIVE_TIME - EntitySpellCircle.CAST_DELAY)) / EntitySpellCircle.CAST_DELAY);

		renderSpellCircle(alive, s1, x, y, z, colorVal);
	}


	public static void renderSpellCircle(float alive, float scale, double x, double y, double z, int color) {
		renderSpellCircle(alive, scale, 1, x, y, z, 0, 1, 0, color);
	}

	public static void renderSpellCircle(float alive, float scale, float horizontalScale, double x, double y, double z, float xDir, float yDir, float zDir, int color) {

		GlStateManager.pushMatrix();
		double ratio = 0.0625 * horizontalScale;
		GlStateManager.translate(x, y, z);

		float mag = xDir * xDir + yDir * yDir + zDir * zDir;
		zDir /= mag;

		if (zDir == -1)
			GlStateManager.rotate(180, 1, 0, 0);
		else if (zDir != 1) {
			GlStateManager.rotate((float) (Math.acos(zDir) * 180 / Math.PI),
					-yDir / mag, xDir / mag, 0);
		}
		GlStateManager.translate(0, 0, 0.1);
		GlStateManager.scale(ratio * scale, ratio * scale, ratio);

		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		float lastBrightnessX = OpenGlHelper.lastBrightnessX;
		float lastBrightnessY = OpenGlHelper.lastBrightnessY;

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);


		int r = PsiRenderHelper.r(color);
		int g = PsiRenderHelper.g(color);
		int b = PsiRenderHelper.b(color);

		for (int i = 0; i < layers.length; i++) {
			int rValue = r;
			int gValue = g;
			int bValue = b;

			if (i == 1)
				rValue = gValue = bValue = 0xFF;
			else if (i == 2) {
				int minBrightness = (int) (1 / (1 - BRIGHTNESS_FACTOR));
				if (rValue == 0 && gValue == 0 && bValue == 0)
					rValue = gValue = bValue = minBrightness;
				if (rValue > 0 && rValue < minBrightness) rValue = minBrightness;
				if (gValue > 0 && gValue < minBrightness) gValue = minBrightness;
				if (bValue > 0 && bValue < minBrightness) bValue = minBrightness;

				rValue = (int) Math.min(rValue / BRIGHTNESS_FACTOR, 0xFF);
				gValue = (int) Math.min(gValue / BRIGHTNESS_FACTOR, 0xFF);
				bValue = (int) Math.min(bValue / BRIGHTNESS_FACTOR, 0xFF);
			}

			GlStateManager.pushMatrix();
			GlStateManager.rotate(i == 0 ? -alive : alive, 0, 0, 1);

			GlStateManager.color(rValue / 255f, gValue / 255f, bValue / 255f);

			Minecraft.getMinecraft().renderEngine.bindTexture(layers[i]);
			AbstractGui.drawModalRectWithCustomSizedTexture(-32, -32, 0, 0, 64, 64, 64, 64);
			GlStateManager.popMatrix();

			GlStateManager.translate(0, 0, -0.5);
		}

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntitySpellCircle entity) {
		return null;
	}

}
