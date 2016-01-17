/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [17/01/2016, 00:45:57 (GMT)]
 */
package vazkii.psi.client.render.tile;

import org.lwjgl.opengl.GL11;

import com.sun.prism.ps.Shader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.StatCollector;
import sun.security.jgss.GSSCaller;
import vazkii.psi.client.core.handler.ShaderHandler;
import vazkii.psi.client.core.helper.RenderHelper;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.block.tile.TileProgrammer;

public class RenderTileProgrammer extends TileEntitySpecialRenderer<TileProgrammer> {

	@Override
	public void renderTileEntityAt(TileProgrammer te, double x, double y, double z, float partialTicks, int destroyStage) {
		if(te.isEnabled()) {
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			ShaderHandler.useShader(ShaderHandler.rawColor);
			GlStateManager.translate(x, y + 1.62F, z);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glRotatef(-90F, 0F, 1F, 0F);

			float f = 1F / 300F;
			GlStateManager.scale(f, f, -f);
			GL11.glTranslatef(70F, 0F, -200F);

			te.spell.draw();

			Minecraft mc = Minecraft.getMinecraft(); 
			mc.renderEngine.bindTexture(GuiProgrammer.texture);
			
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1F, 1F, 1F, 0.5F);
			GL11.glTranslatef(0F, 0F, -0.01F);
			RenderHelper.drawTexturedModalRect(-7, -7, 0, 0, 0, 174, 184, 1F / 256F, 1F / 256F);

			GL11.glTranslatef(0F, 0F, 0.01F);
			mc.fontRendererObj.drawString(StatCollector.translateToLocal("psimisc.name"), 0, 164, 0xFFFFFF);
			mc.fontRendererObj.drawString(te.spell.name, 38, 164, 0xFFFFFF);
			
			ShaderHandler.releaseShader();
			GlStateManager.enableLighting();
			GlStateManager.enableCull();
			GlStateManager.popMatrix();
		}
	}

}
