/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [17/01/2016, 00:45:57 (GMT)]
 */
package vazkii.psi.client.render.tile;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.translation.I18n;
import vazkii.arl.block.BlockFacing;
import vazkii.arl.util.RenderHelper;
import vazkii.psi.client.core.handler.ShaderHandler;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.TileProgrammer;

public class RenderTileProgrammer extends TileEntitySpecialRenderer<TileProgrammer> {

	@Override
	public void func_192841_a(TileProgrammer te, double x, double y, double z, float partialTicks, int destroyStage, float something) {
		if(te.isEnabled()) {
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			ShaderHandler.useShader(ShaderHandler.rawColor);
			GlStateManager.translate(x, y + 1.62F, z);
			GlStateManager.rotate(180F, 0F, 0F, 1F);
			GlStateManager.rotate(-90F, 0F, 1F, 0F);

			float rot = 90F;
			IBlockState state = te.getWorld().getBlockState(te.getPos());
			if(state.getBlock() != ModBlocks.programmer)
				return;

			IBlockState actualState = state.getBlock().getActualState(state, te.getWorld(), te.getPos());
			EnumFacing facing = actualState.getValue(BlockFacing.FACING);
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
			default: break;
			}

			GlStateManager.translate(0.5F, 0F, 0.5F);
			GlStateManager.rotate(rot, 0F, 1F, 0F);
			GlStateManager.translate(-0.5F, 0F, -0.5F);

			float f = 1F / 300F;
			GlStateManager.scale(f, f, -f);
			GlStateManager.translate(70F, 0F, -200F);

			te.spell.draw();

			Minecraft mc = Minecraft.getMinecraft();
			mc.renderEngine.bindTexture(GuiProgrammer.texture);

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1F, 1F, 1F, 0.5F);
			GlStateManager.translate(0F, 0F, -0.01F);
			RenderHelper.drawTexturedModalRect(-7, -7, 0, 0, 0, 174, 184, 1F / 256F, 1F / 256F);

			GlStateManager.translate(0F, 0F, 0.01F);
			mc.fontRendererObj.drawString(I18n.translateToLocal("psimisc.name"), 0, 164, 0xFFFFFF);
			mc.fontRendererObj.drawString(te.spell.name, 38, 164, 0xFFFFFF);

			ShaderHandler.releaseShader();
			GlStateManager.enableLighting();
			GlStateManager.enableCull();
			GlStateManager.popMatrix();
		}
	}

}
