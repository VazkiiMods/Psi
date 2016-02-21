/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [21/02/2016, 16:34:43 (GMT)]
 */
package vazkii.psi.common.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.common.crafting.recipe.SensorAttachRecipe;
import vazkii.psi.common.crafting.recipe.SensorRemoveRecipe;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.lib.LibItemNames;

public class ItemExosuitSensor extends ItemMod implements IExosuitSensor {

	public static final String[] VARIANTS = {
			"exosuitSensorLight",
			"exosuitSensorWater",
			"exosuitSensorHeat",
			"exosuitSensorStress"
	};
	
	public ItemExosuitSensor() {
		super(LibItemNames.EXOSUIT_SENSOR, VARIANTS);
		setMaxStackSize(1);
		
		GameRegistry.addRecipe(new SensorAttachRecipe());
		GameRegistry.addRecipe(new SensorRemoveRecipe());
		RecipeSorter.register("psi:sensorAttach", SensorAttachRecipe.class, Category.SHAPELESS, "");
		RecipeSorter.register("psi:sensorRemove", SensorRemoveRecipe.class, Category.SHAPELESS, "");
	}

	@Override
	public String getEventType(ItemStack stack) {
		switch(stack.getItemDamage()) {
		case 0: return PsiArmorEvent.LOW_LIGHT;
		case 1: return PsiArmorEvent.UNDERWATER;
		case 2: return PsiArmorEvent.ON_FIRE;
		case 3: return PsiArmorEvent.LOW_HP;
		default: return PsiArmorEvent.NONE;
		}
	}

	@Override
	public int getColor(ItemStack stack) {
		switch(stack.getItemDamage()) {
		case 0: return 0xFFEC13;
		case 1: return 0x1350FF;
		case 2: return 0xFF1E13;
		case 3: return 0xFF8CC5;
		default: return ICADColorizer.DEFAULT_SPELL_COLOR;
		}
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		return renderPass == 1 ? getColor(stack) : super.getColorFromItemStack(stack, renderPass);
	}

}
