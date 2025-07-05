/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.tile.TileProgrammer;

public class RenderTileProgrammer implements BlockEntityRenderer<TileProgrammer> {

	public RenderTileProgrammer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(TileProgrammer te, float partialticks, PoseStack ms, MultiBufferSource buffers, int worldLight, int overlay) {
		if(te.isEnabled()) {
			ms.pushPose();
			int light = Psi.magical ? worldLight : 0xF000F0;

			ms.translate(0, 1.62F, 0);
			ms.mulPose(Axis.ZP.rotationDegrees(180F));
			ms.mulPose(Axis.YP.rotationDegrees(-90F));

			float rot = 90F;
			BlockState state = te.getBlockState();

			Direction facing = state.getValue(FaceAttachedHorizontalDirectionalBlock.FACING);
			switch(facing) {
			case SOUTH:
				rot = -90F;
				break;
			case EAST:
				rot = 180F;
				break;
			case WEST:
				rot = 0F;
				break;
			default:
				break;
			}

			ms.translate(0.5F, 0F, 0.5F);
			ms.mulPose(Axis.YP.rotationDegrees(rot));
			ms.translate(-0.5F, 0F, -0.5F);

			float f = 1F / 300F;
			ms.scale(f, f, -f);

			if(Psi.magical) {
				ms.mulPose(Axis.XP.rotationDegrees(90F));
				ms.translate(70F, -220F, -100F + Math.sin(ClientTickHandler.total / 50) * 10);
				ms.mulPose(Axis.XP.rotationDegrees(-16F + (float) Math.cos(ClientTickHandler.total / 100) * 10F));
			} else {
				ms.translate(70F, 0F, -200F);
			}

			te.spell.draw(ms, buffers, light);

			ms.pushPose();
			ms.translate(0F, 0F, -0.01F);
			VertexConsumer buffer = buffers.getBuffer(GuiProgrammer.LAYER);
			float x = -7, y = -7;
			float width = 174;
			float height = 184;
			float u = 0, v = 0;
			float rescale = 1 / 256F;
			float a = Psi.magical ? 1F : 0.5F;
			Matrix4f mat = ms.last().pose();
			buffer.addVertex(mat, x, y + height, 0).setColor(1, 1, 1, a).setUv(u * rescale, (v + height) * rescale).setLight(light);
			buffer.addVertex(mat, x + width, y + height, 0).setColor(1, 1, 1, a).setUv((u + width) * rescale, (v + height) * rescale).setLight(light);
			buffer.addVertex(mat, x + width, y, 0).setColor(1, 1, 1, a).setUv((u + width) * rescale, v * rescale).setLight(light);
			buffer.addVertex(mat, x, y, 0).setColor(1, 1, 1, a).setUv(u * rescale, v * rescale).setLight(light);
			ms.popPose();

			int color = Psi.magical ? 0 : 0xFFFFFF;
			Minecraft mc = Minecraft.getInstance();
			mc.font.drawInBatch(I18n.get("psimisc.name"), 0, 164, color, false, ms.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
			mc.font.drawInBatch(te.spell.name, 38, 164, color, false, ms.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, 0xF000F0);

			ms.popPose();
		}
	}
}
