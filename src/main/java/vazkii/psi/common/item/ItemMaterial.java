/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [08/01/2016, 22:18:55 (GMT)]
 */
package vazkii.psi.common.item;

import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.lib.LibItemNames;

public class ItemMaterial extends ItemMod {

	public static final String[] VARIANTS = {
			"psidust",
			"psimetal",
			"psigem",
			"ebonyPsimetal",
			"ivoryPsimetal"
	};
	
	public ItemMaterial() {
		super(LibItemNames.MATERIAL, VARIANTS);
	}
}
