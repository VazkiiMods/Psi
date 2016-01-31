/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [31/01/2016, 20:32:53 (GMT)]
 */
package vazkii.psi.common.block.base;

import net.minecraft.util.IStringSerializable;

public interface IVariantEnumHolder<T extends Enum<T> & IStringSerializable> {

	public static final String HEADER = "variant";
	
	public Class<T> getVariantEnum();
	
}
