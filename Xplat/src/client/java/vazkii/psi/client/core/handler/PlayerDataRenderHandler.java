/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.client.render.entity.RenderSpellCircle;
import vazkii.psi.common.core.handler.PlayerData;
import vazkii.psi.common.core.handler.PsiPlayerData;

public final class PlayerDataRenderHandler {

	private PlayerDataRenderHandler() {}

	public static void renderAll(float partialTicks, PoseStack poseStack) {
		Minecraft minecraft = Minecraft.getInstance();
		Entity cameraEntity = minecraft.getCameraEntity();
		if(cameraEntity != null && minecraft.level != null) {
			for(Player player : minecraft.level.players()) {
				render(PsiPlayerData.get(player), player, partialTicks, poseStack);
			}
		}
	}

	public static float modifyFov(AbstractClientPlayer player, float fov) {
		PlayerData data = PsiPlayerData.get(player);
		if(data.isAnchored) {
			if(data.eidosAnchorTime > 0) {
				fov *= Math.min(5, data.eidosAnchorTime - ClientTickHandler.partialTicks) / 5;
			} else {
				fov *= (10 - Math.min(10, data.postAnchorRecallTime + ClientTickHandler.partialTicks)) / 10;
			}
		}
		return fov;
	}

	public static void render(PlayerData data, Player player, float partialTicks, PoseStack poseStack) {
		EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
		double x = player.xOld + (player.getX() - player.xOld) * partialTicks - renderManager.camera.getPosition().x;
		double y = player.yOld + (player.getY() - player.yOld) * partialTicks - renderManager.camera.getPosition().y;
		double z = player.zOld + (player.getZ() - player.zOld) * partialTicks - renderManager.camera.getPosition().z;
		float scale = 0.75F;
		if(data.loopcasting) {
			scale *= Math.min(5F, data.loopcastTime + partialTicks) / 5F;
		} else if(data.loopcastFadeTime > 0) {
			scale *= Math.min(5F, data.loopcastFadeTime - partialTicks) / 5F;
		} else {
			return;
		}

		int color = ICADColorizer.DEFAULT_SPELL_COLOR;
		ItemStack cad = PsiAPI.getPlayerCAD(data.playerWR.get());
		if(!cad.isEmpty() && cad.getItem() instanceof ICAD icad) {
			color = icad.getSpellColor(cad);
		}

		poseStack.pushPose();
		poseStack.translate(x, y + 0.15, z);
		MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
		RenderSpellCircle.renderSpellCircle(ClientTickHandler.ticksInGame + partialTicks,
				scale, 1, 0, -1, 0, color, poseStack, buffers);
		buffers.endBatch();
		poseStack.popPose();
	}

}
