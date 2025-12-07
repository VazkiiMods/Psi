/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.render.entity;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.client.model.ArmorModels;
import vazkii.psi.common.Psi;
import vazkii.psi.common.entity.EntitySpellCircle;
import vazkii.psi.common.lib.LibResources;

public class RenderSpellCircle extends EntityRenderer<EntitySpellCircle> {

	private static final RenderType[] LAYERS = new RenderType[3];
	private static final float BRIGHTNESS_FACTOR = 0.7F;

	static {
		for(int i = 0; i < LAYERS.length; i++) {
			ResourceLocation texture = ResourceLocation.parse(String.format(LibResources.MISC_SPELL_CIRCLE, i));
			RenderType.CompositeState glState = RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
					.setCullState(new RenderStateShard.CullStateShard(false))
					//.setAlphaState(new RenderStateShard.AlphaStateShard(0.004F))
					.setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
					.setLightmapState(new RenderStateShard.LightmapStateShard(true))
					.createCompositeState(true);
			LAYERS[i] = RenderType.create(PsiAPI.MOD_ID + ":spell_circle_" + i, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 64, false, false, glState);
		}
	}

	public RenderSpellCircle(EntityRendererProvider.Context ctx) {
		super(ctx);
		// Ugly hack to get context
		ArmorModels.init(ctx);
	}

	public static void renderSpellCircle(float alive, float scale, float horizontalScale, float xDir, float yDir, float zDir, int color, PoseStack ms, MultiBufferSource buffers) {
		ms.pushPose();
		double ratio = 0.0625 * horizontalScale;

		Vec3 direction = new Vec3(xDir, yDir, zDir);
		Vec3 normal = new Vec3(0f, 0f, 1f);
		Vec3 axis = normal.cross(direction);
		double dot = normal.dot(direction);

		// Rotate the model so that it's normal matches the normal
		ms.mulPose(Axis.YP.rotationDegrees(90));

		// Very small threshold to see if it's parallel or not.
		if(axis.length() < 1e-6) {
			// If it's parallel but the dot product is negative we flip it.
			if(dot < 0) {
				ms.mulPose(Axis.XP.rotationDegrees(180f));
			}
		} else {
			float angle = (float) Math.acos(dot);
			ms.mulPose(new Quaternionf().fromAxisAngleRad((float) axis.x, (float) axis.y, (float) axis.z, angle));
		}

		ms.translate(0, 0, 0.1);
		ms.scale((float) ratio * scale, (float) ratio * scale, (float) ratio);

		int r = PsiRenderHelper.r(color);
		int g = PsiRenderHelper.g(color);
		int b = PsiRenderHelper.b(color);

		for(int i = 0; i < LAYERS.length; i++) {
			int rValue = r;
			int gValue = g;
			int bValue = b;

			if(i == 1) {
				rValue = gValue = bValue = 0xFF;
			} else if(i == 2) {
				int minBrightness = (int) (1 / (1 - BRIGHTNESS_FACTOR));
				if(rValue == 0 && gValue == 0 && bValue == 0) {
					rValue = gValue = bValue = minBrightness;
				}
				if(rValue > 0 && rValue < minBrightness) {
					rValue = minBrightness;
				}
				if(gValue > 0 && gValue < minBrightness) {
					gValue = minBrightness;
				}
				if(bValue > 0 && bValue < minBrightness) {
					bValue = minBrightness;
				}

				rValue = (int) Math.min(rValue / BRIGHTNESS_FACTOR, 0xFF);
				gValue = (int) Math.min(gValue / BRIGHTNESS_FACTOR, 0xFF);
				bValue = (int) Math.min(bValue / BRIGHTNESS_FACTOR, 0xFF);
			}

			ms.pushPose();
			ms.mulPose(Axis.ZP.rotationDegrees(i == 0 ? -alive : alive));

			VertexConsumer buffer = buffers.getBuffer(LAYERS[i]);
			Matrix4f mat = ms.last().pose();
			int fullbright = 0xF000F0;
			buffer.addVertex(mat, -32, 32, 0).setColor(rValue, gValue, bValue, 255).setUv(0, 1).setLight(fullbright);
			buffer.addVertex(mat, 32, 32, 0).setColor(rValue, gValue, bValue, 255).setUv(1, 1).setLight(fullbright);
			buffer.addVertex(mat, 32, -32, 0).setColor(rValue, gValue, bValue, 255).setUv(1, 0).setLight(fullbright);
			buffer.addVertex(mat, -32, -32, 0).setColor(rValue, gValue, bValue, 255).setUv(0, 0).setLight(fullbright);
			ms.popPose();

			ms.translate(0, 0, -0.5);
		}

		ms.popPose();
	}

	@Override
	public void render(EntitySpellCircle entity, float entityYaw, float partialTicks, PoseStack ms, @NotNull MultiBufferSource buffers, int light) {
		ms.pushPose();
		ItemStack colorizer = entity.getEntityData().get(EntitySpellCircle.COLORIZER_DATA);
		int color = Psi.proxy.getColorForColorizer(colorizer);
		float alive = entity.getTimeAlive() + partialTicks;
		float scale = Math.min(entity.getEntityData().get(EntitySpellCircle.SCALE), alive / EntitySpellCircle.CAST_DELAY);
		int lifetime = entity.getEntityData().get(EntitySpellCircle.LIFETIME);
		if(alive > lifetime - EntitySpellCircle.CAST_DELAY) {
			scale = 1F - Math.min(1F, Math.max(0, alive - (lifetime - EntitySpellCircle.CAST_DELAY)) / EntitySpellCircle.CAST_DELAY);
		}

		renderSpellCircle(alive, scale, 1, entity.getEntityData().get(EntitySpellCircle.DIRECTION_X), entity.getEntityData().get(EntitySpellCircle.DIRECTION_Y), entity.getEntityData().get(EntitySpellCircle.DIRECTION_Z), color, ms, buffers);
		ms.popPose();
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull EntitySpellCircle entitySpellCircle) {
		return PsiAPI.location("spell_circle");
	}
}
