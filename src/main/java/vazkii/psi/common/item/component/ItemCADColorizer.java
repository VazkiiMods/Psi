/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [13/01/2016, 12:21:53 (GMT)]
 */
package vazkii.psi.common.item.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.lang3.text.WordUtils;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.common.lib.LibItemNames;

public class ItemCADColorizer extends ItemCADComponent implements ICADColorizer {

	public static final String[] VARIANTS = {
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.WHITE),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.ORANGE),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.MAGENTA),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.LIGHT_BLUE),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.YELLOW),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.LIME),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.PINK),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.GRAY),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.SILVER),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.CYAN),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.PURPLE),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.BLUE),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.BROWN),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.GREEN),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.RED),
			LibItemNames.CAD_COLORIZER + getProperDyeName(EnumDyeColor.BLACK),
			LibItemNames.CAD_COLORIZER + "Rainbow",
			LibItemNames.CAD_COLORIZER + "Golden"
		};	
	
	public ItemCADColorizer() {
		super(LibItemNames.CAD_COLORIZER, VARIANTS);
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		if(renderPass == 1 && stack.getItemDamage() < 16)
			return ItemDye.dyeColors[15 - stack.getItemDamage()];
		return 0xFFFFFF;
	}
	
	@Override
	public int getColor(ItemStack stack) {
		if(stack.getItemDamage() < 16)
			return ItemDye.dyeColors[15 - stack.getItemDamage()];
		
		switch(stack.getItemDamage()) {
		case 16: {
			float time = ClientTickHandler.total;
			int color = Color.HSBtoRGB(time * 0.005F, 1F, 1F);
			return color;
		}
		case 17: return 0xD7B32C;

		}
		
		return 0xFFFFFF;
	}
	
	private static final String getProperDyeName(EnumDyeColor color) {
		return WordUtils.capitalize(color.getName(), '_').replaceAll("_", "");
	}

}
