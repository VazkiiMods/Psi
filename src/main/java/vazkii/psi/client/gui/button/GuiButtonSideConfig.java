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
import com.mojang.blaze3d.vertex.*;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

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
		super(x, y, 8, 8, Component.empty(), Button::onPress);
		this.gui = gui;
		this.gridX = gridX;
		this.gridY = gridY;
		this.paramIndex = paramIndex;
		this.paramName = paramName;
		this.side = side;
	}

	public GuiButtonSideConfig(GuiProgrammer gui, int gridX, int gridY, int paramIndex, String paramName, SpellParam.Side side, int x, int y, OnPress pressable) {
		super(x, y, 8, 8, Component.empty(), pressable);
		this.gui = gui;
		this.gridX = gridX;
		this.gridY = gridY;
		this.paramIndex = paramIndex;
		this.paramName = paramName;
		this.side = side;
	}

	@Override
	public void renderButton(PoseStack ms, int par2, int par3, float pTicks) {
		if (active && visible && !gui.takingScreenshot) {
			int minX = x;
			int minY = y;
			int maxX = minX + 8;
			int maxY = minY + 8;

			RenderSystem.setShaderTexture(0, GuiProgrammer.texture);
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
				RenderSystem.setShaderColor(PsiRenderHelper.r(param.color) / 255F,
						PsiRenderHelper.g(param.color) / 255F,
						PsiRenderHelper.b(param.color) / 255F, 1F);
			} else {
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			}

			float wh = 8F;
			float minU = side.u / 256F;
			float minV = side.v / 256F;
			float maxU = (side.u + wh) / 256F;
			float maxV = (side.v + wh) / 256F;
			//RenderSystem.enableAlphaTest(); //TODO Alpha Test?
			BufferBuilder wr = Tesselator.getInstance().getBuilder();
			wr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX); //TODO Check if QUADS is correct
			wr.vertex(minX, maxY, 0).uv(minU, maxV).endVertex();
			wr.vertex(maxX, maxY, 0).uv(maxU, maxV).endVertex();
			wr.vertex(maxX, minY, 0).uv(maxU, minV).endVertex();
			wr.vertex(minX, minY, 0).uv(minU, minV).endVertex();
			Tesselator.getInstance().end();
			//RenderSystem.disableAlphaTest();
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
