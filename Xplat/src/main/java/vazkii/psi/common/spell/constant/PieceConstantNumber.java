/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.constant;

import net.minecraft.nbt.CompoundTag;

import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellPiece;

public class PieceConstantNumber extends SpellPiece {

	private static final String TAG_CONSTANT_VALUE = "constantValue";
	private static final int KEY_BACKSPACE = 259;

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
		if(keyCode == KEY_BACKSPACE) {
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
