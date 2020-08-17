/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.gui.GuiProgrammer;

public class GuiButtonSideConfig extends Button {

	final GuiProgrammer gui;
	final int gridX;
	final int gridY;
	public final String paramName;
	final int paramIndex;
	final SpellParam.Side side;

	public GuiButtonSideConfig(GuiProgrammer gui, int gridX, int gridY, int paramIndex, String paramName, SpellParam.Side side, int x, int y) {
		super(x, y, 8, 8, "", Button::onPress);
		this.gui = gui;
		this.gridX = gridX;
		this.gridY = gridY;
		this.paramIndex = paramIndex;
		this.paramName = paramName;
		this.side = side;
	}

	public GuiButtonSideConfig(GuiProgrammer gui, int gridX, int gridY, int paramIndex, String paramName, SpellParam.Side side, int x, int y, IPressable pressable) {
		super(x, y, 8, 8, "", pressable);
		this.gui = gui;
		this.gridX = gridX;
		this.gridY = gridY;
		this.paramIndex = paramIndex;
		this.paramName = paramName;
		this.side = side;
	}

	@Override
	public void renderButton(int par2, int par3, float pTicks) {
		if (active && visible && !gui.takingScreenshot) {
			int minX = x;
			int minY = y;
			int maxX = minX + 8;
			int maxY = minY + 8;

			Minecraft.getInstance().textureManager.bindTexture(GuiProgrammer.texture);
			SpellPiece piece = gui.spell.grid.gridData[gridX][gridY];
			if (piece == null) {
				return;
			}

			SpellParam<?> param = piece.params.get(paramName);
			if (param == null) {
				return;
			}

			SpellParam.Side currSide = piece.paramSides.get(param);
			if (currSide == side) {
				RenderSystem.color4f(PsiRenderHelper.r(param.color) / 255F,
						PsiRenderHelper.g(param.color) / 255F,
						PsiRenderHelper.b(param.color) / 255F, 1F);
			} else {
				RenderSystem.color3f(1F, 1F, 1F);
			}

			float wh = 8F;
			float minU = side.u / 256F;
			float minV = side.v / 256F;
			float maxU = (side.u + wh) / 256F;
			float maxV = (side.v + wh) / 256F;
			RenderSystem.enableAlphaTest();
			BufferBuilder wr = Tessellator.getInstance().getBuffer();
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);
			wr.vertex(minX, maxY, 0).texture(minU, maxV).endVertex();
			wr.vertex(maxX, maxY, 0).texture(maxU, maxV).endVertex();
			wr.vertex(maxX, minY, 0).texture(maxU, minV).endVertex();
			wr.vertex(minX, minY, 0).texture(minU, minV).endVertex();
			Tessellator.getInstance().draw();
			RenderSystem.disableAlphaTest();
		}
	}

	public boolean matches(int index, SpellParam.Side side) {
		return paramIndex == index && this.side == side;
	}

	public static void performAction(GuiProgrammer gui, int gridX, int gridY, String paramName, SpellParam.Side side) {
		SpellPiece piece = gui.spell.grid.gridData[gridX][gridY];
		if (piece == null) {
			return;
		}

		SpellParam<?> param = piece.params.get(paramName);
		if (param == null) {
			return;
		}

		piece.paramSides.put(param, side);
	}
}
