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
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Direction;
import org.lwjgl.opengl.GL11;
import vazkii.arl.util.ClientTicker;
import vazkii.arl.util.RenderHelper;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
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
                ms.translate(70F, -220F, -100F + Math.sin(ClientTicker.total / 50) * 10);
                ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-16F + (float) Math.cos(ClientTicker.total / 100) * 10F));
            } else ms.translate(70F, 0F, -200F);

            te.spell.draw(ms, buffers, light);

            Minecraft mc = Minecraft.getInstance();
            mc.textureManager.bindTexture(GuiProgrammer.texture);

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderSystem.color4f(1F, 1F, 1F, (Psi.magical ? 1F : 0.5F));
            ms.translate(0F, 0F, -0.01F);

            RenderHelper.drawTexturedModalRect(-7, -7, 0, 0, 0, 174, 184, 1F / 256F, 1F / 256F);

            ms.translate(0F, 0F, 0.01F);

            int color = Psi.magical ? 0 : 0xFFFFFF;
            mc.fontRenderer.draw(I18n.format("psimisc.name"), 0, 164, color, false, ms.peek().getModel(), buffers, false, 0, 0xF000F0);
            mc.fontRenderer.draw(te.spell.name, 38, 164, color, false, ms.peek().getModel(), buffers, false, 0, 0xF000F0);

            ms.pop();
        }
	}
}
