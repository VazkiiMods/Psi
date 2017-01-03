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

import java.awt.Color;

import com.google.common.base.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.client.core.handler.ShaderHandler;
import vazkii.psi.common.Psi;
import vazkii.psi.common.entity.EntitySpellCircle;
import vazkii.psi.common.lib.LibResources;

public class RenderSpellCircle extends Render<EntitySpellCircle> {

	private static final ResourceLocation[] layers = new ResourceLocation[] {
			new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, 0)),
			new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, 1)),
			new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, 2)),
	};

	public RenderSpellCircle(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntitySpellCircle entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
		ItemStack colorizer = entity.getDataManager().get(EntitySpellCircle.COLORIZER_DATA);
		if(!colorizer.isEmpty() && colorizer.getItem() instanceof ICADColorizer)
			colorVal = Psi.proxy.getColorizerColor(colorizer).getRGB();
		float alive = entity.getTimeAlive() + partialTicks;
		float s1 = Math.min(1F, alive / EntitySpellCircle.CAST_DELAY);
		if(alive > EntitySpellCircle.LIVE_TIME - EntitySpellCircle.CAST_DELAY)
			s1 = 1F - Math.min(1F, Math.max(0, alive - (EntitySpellCircle.LIVE_TIME - EntitySpellCircle.CAST_DELAY)) / EntitySpellCircle.CAST_DELAY);

		renderSpellCircle(alive, s1, x, y, z, colorVal);
	}

	public static void renderSpellCircle(float time, float s1, double x, double y, double z, int colorVal) {
		float s = 1F / 16F;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x - s1 * 2, y + 0.01, z - s1 * 2);
		GlStateManager.scale(s, s, s);
		GlStateManager.scale(s1, 1F, s1);
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		ShaderHandler.useShader(ShaderHandler.rawColor);

		for(int i = 0; i < layers.length; i++) {
			Color color = new Color(colorVal);
			if(i == 2)
				color = color.brighter();

			float r = color.getRed() / 255F;
			float g = color.getGreen() / 255F;
			float b = color.getBlue() / 255F;

			float d = 2F / s;
			GlStateManager.pushMatrix();
			GlStateManager.translate(d, d, 0F);
			float rot = time;
			if(i == 0)
				rot = -rot;
			GlStateManager.rotate(rot, 0F, 0F, 1F);
			GlStateManager.translate(-d, -d, 0F);

			if(i == 1)
				GlStateManager.color(1F, 1F, 1F);
			else GlStateManager.color(r, g, b);

			Minecraft.getMinecraft().renderEngine.bindTexture(layers[i]);
			Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 64, 64, 64, 64);
			GlStateManager.popMatrix();
			GlStateManager.translate(0F, 0F, -0.5F);
		}

		ShaderHandler.releaseShader();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySpellCircle entity) {
		return null;
	}

}
