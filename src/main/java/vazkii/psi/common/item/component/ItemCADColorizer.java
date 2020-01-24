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

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.util.ClientTicker;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.crafting.recipe.ColorizerChangeRecipe;
import vazkii.psi.common.lib.LibItemNames;

import java.awt.*;

public class ItemCADColorizer extends ItemCADComponent implements ICADColorizer, IItemColorProvider {

	public static final String[] VARIANTS = {
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.WHITE),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.ORANGE),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.MAGENTA),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.LIGHT_BLUE),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.YELLOW),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.LIME),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.PINK),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.GRAY),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.SILVER),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.CYAN),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.PURPLE),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.BLUE),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.BROWN),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.GREEN),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.RED),
			LibItemNames.CAD_COLORIZER + getProperDyeName(DyeColor.BLACK),
			LibItemNames.CAD_COLORIZER + "rainbow",
			LibItemNames.CAD_COLORIZER + "psi"
	};

	// must be length 16
	public static final int[] colorTable = DyeItem.DYE_COLORS;

	public ItemCADColorizer(String name, Item.Properties properties) {
		super(name, properties);

		new ColorizerChangeRecipe();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public IItemColor getItemColor() {
		return (stack, tintIndex) -> tintIndex == 1 && stack.getItemDamage() < 16 ? colorTable[15 - stack.getItemDamage()] : 0xFFFFFF;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getColor(ItemStack stack) {
		if (stack.getItemDamage() < 16)
			return colorTable[15 - stack.getItemDamage()];

		switch (stack.getItemDamage()) {
			case 16: {
				float time = ClientTicker.total;
			return Color.HSBtoRGB(time * 0.005F, 1F, 1F);
		}
		case 17:
			float time = ClientTicker.total;
			float w = (float) (Math.sin(time * 0.4) * 0.5 + 0.5) * 0.1F;
			float r = (float) (Math.sin(time * 0.1) * 0.5 + 0.5) * 0.5F + 0.25F + w;
			float g = 0.5F + w;
			float b = 1F;

			return new Color((int) (r * 255), (int) (g * 255), (int) (b * 255)).getRGB();
		}

		return 0xFFFFFF;
	}

	private static String getProperDyeName(DyeColor color) {
		return color.getName();
	}

}
