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

import java.awt.Color;

import org.apache.commons.lang3.text.WordUtils;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.RecipeSorter;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.common.crafting.recipe.ColorizerChangeRecipe;
import vazkii.psi.common.lib.LibItemNames;

public class ItemCADColorizer extends ItemCADComponent implements ICADColorizer, IItemColorProvider {

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
			LibItemNames.CAD_COLORIZER + "rainbow",
			LibItemNames.CAD_COLORIZER + "psi"
	};

	public ItemCADColorizer() {
		super(LibItemNames.CAD_COLORIZER, VARIANTS);

		new ColorizerChangeRecipe();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IItemColor getItemColor() {
		return new IItemColor() {
			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex) {
				return tintIndex == 1 && stack.getItemDamage() < 16 ? ItemDye.DYE_COLORS[15 - stack.getItemDamage()] : 0xFFFFFF;
			}
		};
	}

	@Override
	public int getColor(ItemStack stack) {
		if(stack.getItemDamage() < 16)
			return ItemDye.DYE_COLORS[15 - stack.getItemDamage()];

		switch(stack.getItemDamage()) {
		case 16: {
			float time = ClientTickHandler.total;
			return Color.HSBtoRGB(time * 0.005F, 1F, 1F);
		}
		case 17:
			float time = ClientTickHandler.total;
			float w = (float) (Math.sin(time * 0.4) * 0.5 + 0.5) * 0.1F;
			float r = (float) (Math.sin(time * 0.1) * 0.5 + 0.5) * 0.5F + 0.25F + w;
			float g = 0.5F + w;
			float b = 1F;

			return new Color((int) (r * 255), (int) (g * 255), (int) (b * 255)).getRGB();
		}

		return 0xFFFFFF;
	}

	private static String getProperDyeName(EnumDyeColor color) {
		return color.getName();
	}

}
