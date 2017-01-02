/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/01/2016, 22:18:55 (GMT)]
 */
package vazkii.psi.common.item;

import vazkii.arl.item.ItemMod;
import vazkii.psi.common.item.base.IPsiItem;
import vazkii.psi.common.lib.LibItemNames;

public class ItemMaterial extends ItemMod implements IPsiItem {

	public static final String[] VARIANTS = {
			"psidust",
			"psimetal",
			"psigem",
			"ebony_psimetal",
			"ivory_psimetal",
			"ebony_substance",
			"ivory_substance"
	};

	public ItemMaterial() {
		super(LibItemNames.MATERIAL, VARIANTS);
	}
}
