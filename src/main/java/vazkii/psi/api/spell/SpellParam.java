/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 15:19:38 (GMT)]
 */
package vazkii.psi.api.spell;

import net.minecraft.util.StatCollector;

/**
 * Base abstract class for a spell parameter. See implementations
 * in vazkii.psi.api.spell.param.
 */
public abstract class SpellParam {

	// Colors
	public static final int RED = 0xD22A2A;
	public static final int GREEN = 0x3ED22A;
	public static final int BLUE = 0x2A55D2;
	public static final int PURPLE = 0x752AD2;
	public static final int CYAN = 0x2AD0D2;
	public static final int YELLOW = 0xD2CC2A; // For entities
	public static final int GRAY = 0x767676; // For connectors

	// Generic names
	public static final String GENERIC_NAME_TARGET = "psi.spellparam.target";
	public static final String GENERIC_NAME_NUMBER = "psi.spellparam.number";
	public static final String GENERIC_NAME_NUMBER1 = "psi.spellparam.number1";
	public static final String GENERIC_NAME_NUMBER2 = "psi.spellparam.number2";
	public static final String GENERIC_NAME_NUMBER3 = "psi.spellparam.number3";
	public static final String GENERIC_NAME_NUMBER4 = "psi.spellparam.number4";
	public static final String GENERIC_NAME_VECTOR1 = "psi.spellparam.vector1";
	public static final String GENERIC_NAME_VECTOR2 = "psi.spellparam.vector2";
	public static final String GENERIC_NAME_VECTOR3 = "psi.spellparam.vector3";
	public static final String GENERIC_NAME_VECTOR4 = "psi.spellparam.vector4";
	public static final String GENERIC_NAME_POSITION = "psi.spellparam.position";
	public static final String GENERIC_NAME_MIN = "psi.spellparam.min";
	public static final String GENERIC_NAME_MAX = "psi.spellparam.max";
	public static final String GENERIC_NAME_POWER = "psi.spellparam.power";
	public static final String GENERIC_NAME_X = "psi.spellparam.x";
	public static final String GENERIC_NAME_Y = "psi.spellparam.y";
	public static final String GENERIC_NAME_Z = "psi.spellparam.z";
	public static final String GENERIC_NAME_RADIUS = "psi.spellparam.radius";
	public static final String GENERIC_NAME_DISTANCE = "psi.spellparam.distance";
	public static final String GENERIC_NAME_TIME = "psi.spellparam.time";

	public final String name;
	public final int color;
	public final boolean canDisable;

	public SpellParam(String name, int color, boolean canDisable) {
		this.name = name;
		this.color = color;
		this.canDisable = canDisable;
	}

	/**
	 * Gets the type that this parameter requires. This is evaluated against
	 * {@link SpellPiece#getEvaluationType()}.<br>
	 * If you want any type to be able to be accepted, use {@link Any}.class.<br>
	 * This method is to only be used internally, and as such, is not required to be
	 * implemented fully. For better control, use {@link #canAccept(SpellPiece)} and
	 * override {@link #getRequiredTypeString()} for display.
	 */
	protected abstract Class<?> getRequiredType();

	/**
	 * Gets if this parameter requires a constant ({@link EnumPieceType#CONSTANT}). Similarly to {@link #getRequiredType()} this
	 * is for internal use only.
	 */
	protected boolean requiresConstant() {
		return false;
	}

	/**
	 * Gets the string for display for the required type.
	 */
	public String getRequiredTypeString() {
		Class<?> evalType = getRequiredType();
		String evalStr = evalType.getSimpleName();
		String s = StatCollector.translateToLocal("psi.datatype." + evalStr);
		if(requiresConstant())
			s += " " + StatCollector.translateToLocal("psimisc.constant");

		return s;
	}

	/**
	 * Gets if this paramtere can accept the piece passed in. Default implementation
	 * checks against {@link #getRequiredType()} and {@link #requiresConstant()}.
	 */
	public boolean canAccept(SpellPiece piece) {
		return (getRequiredType() == Any.class || getRequiredType().isAssignableFrom(piece.getEvaluationType())) && (!requiresConstant() || piece.getPieceType() == EnumPieceType.CONSTANT);
	}

	/**
	 * Helper Enum for the various sides a parameter can take.
	 */
	public enum Side {
		OFF(0, 0, 238, 0),
		TOP(0, -1, 222, 8),
		BOTTOM(0, 1, 230, 8),
		LEFT(-1, 0, 230, 0),
		RIGHT(1, 0, 222, 0);

		public final int offx, offy, u, v;

		private Side(int offx, int offy, int u, int v) {
			this.offx = offx;
			this.offy = offy;
			this.u = u;
			this.v = v;
		}

		public Side getOpposite() {
			switch(this) {
			case TOP: return BOTTOM;
			case BOTTOM: return TOP;
			case LEFT: return RIGHT;
			case RIGHT: return LEFT;
			default: return OFF;
			}
		}

		public boolean isEnabled() {
			return this != OFF;
		}

		public int asInt() {
			return ordinal();
		}

		public static Side fromInt(int i) {
			return Side.class.getEnumConstants()[i];
		}
	}

	/**
	 * Empty helper class for use with required types when any type is accepted.
	 */
	public static class Any { }

}
