/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.constant;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;

import org.lwjgl.glfw.GLFW;

import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.Psi;

public class PieceConstantNumber extends SpellPiece {

	private static final String TAG_CONSTANT_VALUE = "constantValue";

	public String valueStr;

	public PieceConstantNumber(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		super.initParams();

		valueStr = "0";
	}

	@Override
	public void drawAdditional(PoseStack pPoseStack, MultiBufferSource buffers, int light) {
		if(valueStr == null || valueStr.isEmpty() || valueStr.length() > 5) {
			valueStr = "0";
		}

		Minecraft mc = Minecraft.getInstance();
		int color = Psi.magical ? 0 : 0xFFFFFF;
		float efflen = mc.font.width(valueStr);
		float scale = 1;

		while(efflen > 16) {
			scale++;
			efflen = mc.font.width(valueStr) / scale;
		}

		pPoseStack.pushPose();
		pPoseStack.scale(1F / scale, 1F / scale, 1F);
		pPoseStack.translate((9 - efflen / 2) * scale, 4 * scale, 0);
		//graphics.drawString(mc.font, valueStr, 0, 0, color, false); // TODO(Kamefrede): 1.20 check if this is ruight
		mc.font.drawInBatch(valueStr, 0, 0, color, false, pPoseStack.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, 15728880);
		pPoseStack.popPose();
	}

	@Override
	public boolean interceptKeystrokes() {
		return true;
	}

	@Override
	public boolean onCharTyped(char character, int keyCode, boolean doit) {
		if("FDfd".indexOf(character) >= 0) {
			return false;
		}

		String oldStr = valueStr;
		String newStr = valueStr;
		if((newStr.equals("0") || newStr.equals("-0")) && "+-.".indexOf(character) < 0) {
			newStr = newStr.replace("0", "");
		}

		if(character == '+') {
			newStr = newStr.replace("-", "");
		} else if(character == '-') {
			if(!newStr.startsWith("-")) {
				newStr = "-" + newStr;
			}
		} else {
			newStr += character;
		}

		if(newStr.isEmpty()) {
			newStr = "0";
		}
		newStr = newStr.trim();

		if(newStr.length() > 5) {
			return false;
		}

		String newValueStr;
		try {
			Double.parseDouble(newStr);
			newValueStr = newStr;
		} catch (NumberFormatException e) {
			return false;
		}

		if(doit) {
			valueStr = newValueStr;
		}

		return !newValueStr.equals(oldStr);
	}

	@Override
	public boolean onKeyPressed(int keyCode, int scanCode, boolean doit) {
		String oldStr = valueStr;
		String newStr = valueStr;
		if(keyCode == GLFW.GLFW_KEY_BACKSPACE) {
			if(newStr.length() == 2 && newStr.startsWith("-")) {
				newStr = "-0";
			} else if(newStr.equals("-")) {
				newStr = "0";
			} else if(!newStr.isEmpty()) {
				newStr = newStr.substring(0, newStr.length() - 1);
			}
		}

		if(newStr.isEmpty()) {
			newStr = "0";
		}
		newStr = newStr.trim();

		if(newStr.length() > 5) {
			return false;
		}

		String newValueStr;
		try {
			Double.parseDouble(newStr);
			newValueStr = newStr;
		} catch (NumberFormatException e) {
			return false;
		}

		if(doit) {
			valueStr = newValueStr;
		}

		return !newValueStr.equals(oldStr);
	}

	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.CONSTANT;
	}

	@Override
	public void writeToNBT(CompoundTag cmp) {
		super.writeToNBT(cmp);
		cmp.putString(TAG_CONSTANT_VALUE, valueStr);
	}

	@Override
	public void readFromNBT(CompoundTag cmp) {
		super.readFromNBT(cmp);
		valueStr = cmp.getString(TAG_CONSTANT_VALUE);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

	@Override
	public Object evaluate() {
		if(valueStr == null || valueStr.isEmpty() || valueStr.length() > 5) {
			valueStr = "0";
		}

		try {
			return Double.parseDouble(valueStr);
		} catch (NumberFormatException e) {
			return 0D;
		}
	}

	@Override
	public Object execute(SpellContext context) {
		return evaluate();
	}

}
