/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.tile.TileProgrammer;

public class RenderTileProgrammer extends TileEntityRenderer<TileProgrammer> {

	public RenderTileProgrammer(TileEntityRendererDispatcher p_i226006_1_) {
		super(p_i226006_1_);
	}

	@Override
	public void render(TileProgrammer te, float partialticks, MatrixStack ms, IRenderTypeBuffer buffers, int worldLight, int overlay) {
		if (te.isEnabled()) {
			ms.push();
			int light = Psi.magical ? worldLight : 0xF000F0;

			ms.translate(0, 1.62F, 0);
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180F));
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90F));

			float rot = 90F;
			BlockState state = te.getBlockState();

			Direction facing = state.get(HorizontalFaceBlock.HORIZONTAL_FACING);
			switch (facing) {
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
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rot));
			ms.translate(-0.5F, 0F, -0.5F);

			float f = 1F / 300F;
			ms.scale(f, f, -f);

			if (Psi.magical) {
				ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90F));
				ms.translate(70F, -220F, -100F + Math.sin(ClientTickHandler.total / 50) * 10);
				ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-16F + (float) Math.cos(ClientTickHandler.total / 100) * 10F));
			} else {
				ms.translate(70F, 0F, -200F);
			}

			te.spell.draw(ms, buffers, light);

			ms.push();
			ms.translate(0F, 0F, -0.01F);
			IVertexBuilder buffer = buffers.getBuffer(GuiProgrammer.LAYER);
			float x = -7, y = -7;
			float width = 174;
			float height = 184;
			float u = 0, v = 0;
			float rescale = 1 / 256F;
			float a = Psi.magical ? 1F : 0.5F;
			Matrix4f mat = ms.peek().getModel();
			buffer.vertex(mat, x, y + height, 0).color(1, 1, 1, a).texture(u * rescale, (v + height) * rescale).light(light).endVertex();
			buffer.vertex(mat, x + width, y + height, 0).color(1, 1, 1, a).texture((u + width) * rescale, (v + height) * rescale).light(light).endVertex();
			buffer.vertex(mat, x + width, y, 0).color(1, 1, 1, a).texture((u + width) * rescale, v * rescale).light(light).endVertex();
			buffer.vertex(mat, x, y, 0).color(1, 1, 1, a).texture(u * rescale, v * rescale).light(light).endVertex();
			ms.pop();

			int color = Psi.magical ? 0 : 0xFFFFFF;
			Minecraft mc = Minecraft.getInstance();
			mc.fontRenderer.draw(I18n.format("psimisc.name"), 0, 164, color, false, ms.peek().getModel(), buffers, false, 0, 0xF000F0);
			mc.fontRenderer.draw(te.spell.name, 38, 164, color, false, ms.peek().getModel(), buffers, false, 0, 0xF000F0);

			ms.pop();
		}
	}
}
