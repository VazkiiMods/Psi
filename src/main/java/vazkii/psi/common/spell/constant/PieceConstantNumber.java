/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 23:47:11 (GMT)]
 */
package vazkii.psi.common.spell.constant;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;
import vazkii.psi.api.spell.*;
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
	public void drawAdditional() {
		if(valueStr == null || valueStr.isEmpty() || valueStr.length() > 5)
			valueStr = "0";

		Minecraft mc = Minecraft.getMinecraft();
		int color = Psi.magical ? 0 : 0xFFFFFF;
		float efflen = mc.fontRenderer.getStringWidth(valueStr);
		float scale = 1;

		while(efflen > 16) {
			scale++;
			efflen = mc.fontRenderer.getStringWidth(valueStr) / scale;
		}

		GlStateManager.pushMatrix();
		GlStateManager.scale(1F / scale, 1F / scale, 1F);
		GlStateManager.translate((9 - efflen / 2) * scale, 4 * scale, 0);
		mc.fontRenderer.drawString(valueStr, 0, 0, color);
		GlStateManager.popMatrix();
	}

	@Override
	public boolean interceptKeystrokes() {
		return true;
	}

	@Override
	public boolean onKeyPressed(char c, int i, boolean doit) {
		if("FDfd".indexOf(c) >= 0)
			return false;

		String oldStr = valueStr;
		String newStr = valueStr;
		if(newStr.equals("0") || newStr.equals("-0")) {
			if(c == '-')
				newStr = "-0";
			else if(c != '.')
				newStr = newStr.replace("0", "");
		}

		if(i == Keyboard.KEY_BACK) {
			if(newStr.length() == 2 && newStr.startsWith("-"))
				newStr = "-0";
			else if(newStr.equals("-"))
				newStr = "0";
			else if(!newStr.isEmpty())
				newStr = newStr.substring(0, newStr.length() - 1);
		} else if(c != '-')
			newStr += c;

		if(newStr.isEmpty())
			newStr = "0";
		newStr = newStr.trim();

		if(newStr.length() > 5)
			return false;

		String newValueStr;
		try {
			Double.parseDouble(newStr);
			newValueStr = newStr;
		} catch(NumberFormatException e) {
			return false;
		}

		if(doit)
			valueStr = newValueStr;

		return !newValueStr.equals(oldStr);
	}

	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.CONSTANT;
	}

	@Override
	public void writeToNBT(NBTTagCompound cmp) {
		super.writeToNBT(cmp);
		cmp.setString(TAG_CONSTANT_VALUE, valueStr);
	}

	@Override
	public void readFromNBT(NBTTagCompound cmp) {
		super.readFromNBT(cmp);
		valueStr = cmp.getString(TAG_CONSTANT_VALUE);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

	@Override
	public Object evaluate() {
		if(valueStr == null || valueStr.isEmpty() || valueStr.length() > 5)
			valueStr = "0";

		try {
			return Double.parseDouble(valueStr);
		} catch(NumberFormatException e) {
			return 0D;
		}
	}

	@Override
	public Object execute(SpellContext context) {
		return evaluate();
	}

}
