/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 23:47:11 (GMT)]
 */
package vazkii.psi.common.spell.constant;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellPiece;

public class PieceConstantNumber extends SpellPiece {

	private static final String TAG_CONSTANT_VALUE = "constantValue";
	
	private String valueStr;
	
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
		Minecraft mc = Minecraft.getMinecraft();
		int color = 0xFFFFFF;
		int len = mc.fontRendererObj.getStringWidth(valueStr);
		float efflen = len;
		float scale = 1;
		
		while(efflen > 16) {
			scale++;
			efflen = mc.fontRendererObj.getStringWidth(valueStr) / scale;
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(1F / scale, 1F / scale, 1F);
		GlStateManager.translate((9 - efflen / 2) * scale, 4 * scale, 0);
		mc.fontRendererObj.drawString(valueStr, 0, 0, color);
		GlStateManager.popMatrix();
	}
	
	@Override
	public boolean interceptKeystrokes() {
		return true;
	}
	
	@Override
	public boolean onKeyPressed(char c, int i) {
		if("FDfd".indexOf(c) >= 0)
			return false;
		
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
		
		try {
			Double.parseDouble(newStr);
			valueStr = newStr;
		} catch(NumberFormatException e) {
			return false;
		}

		return true;
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
		return Double.parseDouble(valueStr);
	}

	@Override
	public Object execute(SpellContext context) {
		return evaluate();
	}
	
}
