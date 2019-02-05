/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 21:34:23 (GMT)]
 */
package vazkii.psi.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.core.helper.PsiRenderHelper;
import vazkii.psi.client.gui.GuiProgrammer;

import javax.annotation.Nonnull;

public class GuiButtonSideConfig extends GuiButton {

	final GuiProgrammer gui;
	final int gridX;
	final int gridY;
	public final String paramName;
	final int paramIndex;
	final SpellParam.Side side;

	public GuiButtonSideConfig(GuiProgrammer gui, int gridX, int gridY, int paramIndex, String paramName, SpellParam.Side side, int x, int y) {
		super(0, x, y, 8, 8, "");
		this.gui = gui;
		this.gridX = gridX;
		this.gridY = gridY;
		this.paramIndex = paramIndex;
		this.paramName = paramName;
		this.side = side;
	}

	public boolean matches(int index, SpellParam.Side side) {
		return paramIndex == index && this.side == side;
	}

	public void onClick() {
		SpellPiece piece = gui.programmer.spell.grid.gridData[gridX][gridY];
		if(piece == null)
			return;

		SpellParam param = piece.params.get(paramName);
		if(param == null)
			return;

		piece.paramSides.put(param, side);
	}

	@Override
	public void drawButton(@Nonnull Minecraft mc, int par2, int par3, float pticks) {
		if(enabled && visible && !gui.takingScreenshot) {
			int minX = x;
			int minY = y;
			int maxX = minX + 8;
			int maxY = minY + 8;

			mc.renderEngine.bindTexture(GuiProgrammer.texture);
			SpellPiece piece = gui.programmer.spell.grid.gridData[gridX][gridY];
			if(piece == null)
				return;

			SpellParam param = piece.params.get(paramName);
			if(param == null)
				return;

			SpellParam.Side currSide = piece.paramSides.get(param);
			if(currSide == side) {
				GlStateManager.color(PsiRenderHelper.r(param.color) / 255F,
						PsiRenderHelper.g(param.color) / 255F,
						PsiRenderHelper.b(param.color) / 255F, 1F);
			} else GlStateManager.color(1F, 1F, 1F);

			float wh = 8F;
			float minU = side.u / 256F;
			float minV = side.v / 256F;
			float maxU = (side.u + wh) / 256F;
			float maxV = (side.v + wh) / 256F;

			BufferBuilder wr = Tessellator.getInstance().getBuffer();
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);
			wr.pos(minX, maxY, 0).tex(minU, maxV).endVertex();
			wr.pos(maxX, maxY, 0).tex(maxU, maxV).endVertex();
			wr.pos(maxX, minY, 0).tex(maxU, minV).endVertex();
			wr.pos(minX, minY, 0).tex(minU, minV).endVertex();
			Tessellator.getInstance().draw();
		}
	}

}
