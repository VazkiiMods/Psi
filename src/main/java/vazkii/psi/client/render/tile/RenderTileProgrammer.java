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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import org.lwjgl.opengl.GL11;
import vazkii.arl.util.ClientTicker;
import vazkii.arl.util.RenderHelper;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.TileProgrammer;

public class RenderTileProgrammer extends TileEntityRenderer<TileProgrammer> {


    public RenderTileProgrammer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(TileProgrammer te, float partialticks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
        if (te.isEnabled()) {
            double x = te.getPos().getX();
            double y = te.getPos().getY();
            double z = te.getPos().getZ();
            ms.push();
            RenderSystem.pushMatrix();
            RenderSystem.disableLighting();
            RenderSystem.disableCull();

           /* float brightnessX = OpenGlHelper.lastBrightnessX;
            float brightnessY = OpenGlHelper.lastBrightnessY;
            if (!Psi.magical)
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);*/


            ms.translate(x, y + 1.62F, z);
            RenderSystem.rotatef(180F, 0F, 0F, 1F);
            RenderSystem.rotatef(-90F, 0F, 1F, 0F);

            float rot = 90F;
            BlockState state = te.getWorld().getBlockState(te.getPos());
            if (state.getBlock() != ModBlocks.programmer)
                return;

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
            RenderSystem.rotatef(rot, 0F, 1F, 0F);
            ms.translate(-0.5F, 0F, -0.5F);

            float f = 1F / 300F;
            ms.scale(f, f, -f);

            if (Psi.magical) {
                RenderSystem.rotatef(90F, 1F, 0F, 0F);
                ms.translate(70F, -220F, -100F + Math.sin(ClientTicker.total / 50) * 10);
                RenderSystem.rotatef(-16F + (float) Math.cos(ClientTicker.total / 100) * 10F, 1F, 0F, 0F);
            } else ms.translate(70F, 0F, -200F);

            te.spell.draw();

            Minecraft mc = Minecraft.getInstance();
            //mc.textureManager.bindTexture(GuiProgrammer.texture);

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderSystem.color4f(1F, 1F, 1F, (Psi.magical ? 1F : 0.5F));
            ms.translate(0F, 0F, -0.01F);

            RenderHelper.drawTexturedModalRect(-7, -7, 0, 0, 0, 174, 184, 1F / 256F, 1F / 256F);

            ms.translate(0F, 0F, 0.01F);

            int color = Psi.magical ? 0 : 0xFFFFFF;
            mc.fontRenderer.drawString(TooltipHelper.local("psimisc.name").toString(), 0, 164, color);
            mc.fontRenderer.drawString(te.spell.name, 38, 164, color);

            /*if (!Psi.magical)
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightnessX, brightnessY);*/
            RenderSystem.enableLighting();
            RenderSystem.enableCull();

            RenderSystem.popMatrix();
            ms.pop();
        }
	}
}
