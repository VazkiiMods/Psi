/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [13/01/2016, 12:21:53 (GMT)]
 */
package vazkii.psi.common.item.component;

import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.cad.ICADColorizer;

public class ItemCADColorizer extends ItemCADComponent implements ICADColorizer {


	private final DyeColor color;
	private String contributorName = "";

	public ItemCADColorizer(Item.Properties properties, DyeColor color) {
		super(properties);
		this.color = color;
	}

	public ItemCADColorizer(Properties properties) {
		super(properties);
		color = DyeColor.BLACK;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getColor(ItemStack stack) {
		return color.getColorValue();
	}

	@Override
	public String getContributorName(ItemStack stack) {
		return contributorName;
	}

	private static String getProperDyeName(DyeColor color) {
		return color.getName();
	}

	@Override
	public void setContributorName(ItemStack stack, String name) {
		contributorName = name;
	}
}
