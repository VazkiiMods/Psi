/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.gui.GuiProgrammer;

public class GuiButtonSideConfig extends Button {

	public final String paramName;
	final GuiProgrammer gui;
	final int gridX;
	final int gridY;
	final int paramIndex;
	final SpellParam.Side side;

	public GuiButtonSideConfig(GuiProgrammer gui, int gridX, int gridY, int paramIndex, String paramName, SpellParam.Side side, int x, int y, OnPress pressable) {
		super(x, y, 8, 8, Component.empty(), pressable, DEFAULT_NARRATION);
		this.gui = gui;
		this.gridX = gridX;
		this.gridY = gridY;
		this.paramIndex = paramIndex;
		this.paramName = paramName;
		this.side = side;
	}

	public static void performAction(GuiProgrammer gui, int gridX, int gridY, String paramName, SpellParam.Side side) {
		SpellPiece piece = gui.spell.grid.gridData[gridX][gridY];
		if(piece == null) {
			return;
		}

		SpellParam<?> param = piece.params.get(paramName);
		if(param == null) {
			return;
		}

		piece.paramSides.put(param, side);
	}

	@Override
	public void renderWidget(@NotNull GuiGraphics graphics, int par2, int par3, float pTicks) {
		if(active && visible && !gui.takingScreenshot) {
			SpellPiece piece = gui.spell.grid.gridData[gridX][gridY];
			if(piece == null) {
				return;
			}

			SpellParam<?> param = piece.params.get(paramName);
			if(param == null) {
				return;
			}

			SpellParam.Side currSide = piece.paramSides.get(param);
			if(currSide == side) {
				graphics.setColor(PsiRenderHelper.r(param.color) / 255F,
						PsiRenderHelper.g(param.color) / 255F,
						PsiRenderHelper.b(param.color) / 255F, 1F);
			} else {
				graphics.setColor(1f, 1f, 1f, 1F);
			}

			graphics.blit(GuiProgrammer.texture, getX(), getY(), 8, 8, side.u, side.v, 8, 8, 256, 256);
			graphics.setColor(1f, 1f, 1f, 1F);
		}
	}

	public boolean matches(int index, SpellParam.Side side) {
		return paramIndex == index && this.side == side;
	}
}
