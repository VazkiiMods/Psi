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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.common.Psi;
import vazkii.psi.common.entity.EntitySpellCircle;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

public class RenderSpellCircle extends EntityRenderer<EntitySpellCircle> {

	private static final RenderType[] LAYERS = new RenderType[3];
	static {
		for (int i = 0; i < LAYERS.length; i++) {
			ResourceLocation texture = new ResourceLocation(String.format(LibResources.MISC_SPELL_CIRCLE, i));
			RenderType.State glState = RenderType.State.builder().texture(new RenderState.TextureState(texture, false, false))
					.cull(new RenderState.CullState(false))
					.lightmap(new RenderState.LightmapState(true))
					.build(true);
			LAYERS[i] = RenderType.of(LibMisc.MOD_ID + ":spell_circle_" + i, DefaultVertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 64, false, false, glState);
		}
	}

	private static final float BRIGHTNESS_FACTOR = 0.7F;

	public RenderSpellCircle(EntityRendererManager renderManager) {
		super(renderManager);
	}


	//TODO Willie take a look at this!
	@Override
	public void render(EntitySpellCircle entity, float entityYaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		ms.push();
		int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
		ItemStack colorizer = entity.getDataManager().get(EntitySpellCircle.COLORIZER_DATA);
		if (!colorizer.isEmpty() && colorizer.getItem() instanceof ICADColorizer)
			colorVal = Psi.proxy.getColorForColorizer(colorizer);
		float alive = entity.getTimeAlive() + partialTicks;
		float s1 = Math.min(1F, alive / EntitySpellCircle.CAST_DELAY);
		if (alive > EntitySpellCircle.LIVE_TIME - EntitySpellCircle.CAST_DELAY)
			s1 = 1F - Math.min(1F, Math.max(0, alive - (EntitySpellCircle.LIVE_TIME - EntitySpellCircle.CAST_DELAY)) / EntitySpellCircle.CAST_DELAY);
		renderSpellCircle(alive, s1, 1, entity.getX(), entity.getY(), entity.getZ(), 0, 1, 0, colorVal, ms, buffers);
		ms.pop();
	}

	public static void renderSpellCircle(float alive, float scale, float horizontalScale, double x, double y, double z, float xDir, float yDir, float zDir, int color, MatrixStack ms, IRenderTypeBuffer buffers) {

		ms.push();
		double ratio = 0.0625 * horizontalScale;
		ms.translate(x, y, z);

		float mag = xDir * xDir + yDir * yDir + zDir * zDir;
		zDir /= mag;

		if (zDir == -1)
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
		else if (zDir != 1) {
			ms.multiply(new Vector3f(-yDir / mag, xDir / mag, 0).getDegreesQuaternion((float) (Math.acos(zDir) * 180 / Math.PI)));
		}
		ms.translate(0, 0, 0.1);
		ms.scale((float) ratio * scale, (float) ratio * scale, (float) ratio);

		int r = PsiRenderHelper.r(color);
		int g = PsiRenderHelper.g(color);
		int b = PsiRenderHelper.b(color);

		for (int i = 0; i < LAYERS.length; i++) {
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

			ms.push();
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(i == 0 ? -alive : alive));

			IVertexBuilder buffer = buffers.getBuffer(LAYERS[i]);
			Matrix4f mat = ms.peek().getModel();
			int fullbright = 0xF000F0;
			buffer.vertex(mat, -32, 32, 0).color(rValue, gValue, bValue, 255).texture(0, 1).light(fullbright).endVertex();
			buffer.vertex(mat, 32, 32, 0).color(rValue, gValue, bValue, 255).texture(1, 1).light(fullbright).endVertex();
			buffer.vertex(mat, 32, -32, 0).color(rValue, gValue, bValue, 255).texture(1, 0).light(fullbright).endVertex();
			buffer.vertex(mat, -32, -32, 0).color(rValue, gValue, bValue, 255).texture(0, 0).light(fullbright).endVertex();
			ms.pop();

			ms.translate(0, 0, -0.5);
		}

		ms.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(EntitySpellCircle entitySpellCircle) {
		return null;
	}
}
