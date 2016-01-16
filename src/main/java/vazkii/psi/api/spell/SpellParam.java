/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 15:19:38 (GMT)]
 */
package vazkii.psi.api.spell;

public abstract class SpellParam {

	public static final int RED = 0xD22A2A;
	public static final int GREEN = 0x3ED22A;
	public static final int BLUE = 0x2A55D2;
	public static final int PURPLE = 0x752AD2;
	public static final int CYAN = 0x2AD0D2;
	public static final int YELLOW = 0xD2CC2A;
	public static final int GRAY = 0x767676;

	public static final String GENERIC_NAME_TARGET = "psi.spellparam.target";
	public static final String GENERIC_NAME_NUMBER = "psi.spellparam.number";

	public final String name;
	public final int color;
	public final boolean canDisable;
	
	public SpellParam(String name, int color, boolean canDisable) {
		this.name = name;
		this.color = color;
		this.canDisable = canDisable;
	}
	
	public abstract boolean canAccept(Class<?> type);
	
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
	
}
