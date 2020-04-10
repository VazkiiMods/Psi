package vazkii.psi.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.ItemExosuitSensor;
import vazkii.psi.common.item.armor.ItemPsimetalArmor;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.component.ItemCADColorizer;

public class ColorHandler {

	public static void init() {
		ItemColors items = Minecraft.getInstance().getItemColors();
		items.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemPsimetalArmor) stack.getItem()).getColor(stack) : 0xFFFFFF, ModItems.psimetalExosuitBoots, ModItems.psimetalExosuitChestplate, ModItems.psimetalExosuitBoots, ModItems.psimetalExosuitLeggings);

		items.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemExosuitSensor) stack.getItem()).getColor(stack) : 0xFFFFFF, ModItems.exosuitSensorHeat, ModItems.exosuitSensorLight, ModItems.exosuitSensorStress, ModItems.exosuitSensorWater);

		items.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemCAD) stack.getItem()).getSpellColor(stack) : 0xFFFFFF, ModItems.cad);

		items.register((stack, tintIndex) -> tintIndex != 1 ? -1 : ((ItemCADColorizer) stack.getItem()).getColor(stack), ModItems.cadColorizerWhite, ModItems.cadColorizerOrange, ModItems.cadColorizerMagenta, ModItems.cadColorizerLightBlue,
				ModItems.cadColorizerYellow, ModItems.cadColorizerLime, ModItems.cadColorizerPink, ModItems.cadColorizerGray, ModItems.cadColorizerLightGray, ModItems.cadColorizerCyan, ModItems.cadColorizerPurple, ModItems.cadColorizerBlue,
				ModItems.cadColorizerBrown, ModItems.cadColorizerGreen, ModItems.cadColorizerRed, ModItems.cadColorizerBlack);
	}

	public static int slideColor(int[] color, float period) {
		int n = color.length;
		float cyclePos = (ClientTickHandler.total % period / period) * n;
		float tweenFactor = cyclePos % 1.0F;

		return slideColor(color[(int) cyclePos % n], color[(1 + (int)cyclePos) % n], tweenFactor);
	}

	public static int slideColor(int color, int secondColor, float tweenFactor) {
		int redA = (0xFF0000 & color) >> 16;
		int greenA = (0x00FF00 & color) >> 8;
		int blueA = 0x0000FF & color;
		int redB = (0xFF0000 & secondColor) >> 16;
		int greenB = (0x00FF00 & secondColor) >> 8;
		int blueB = 0x0000FF & secondColor;

		int newRed = (int) (redA * (1 - tweenFactor) + redB * tweenFactor);
		int newGreen = (int) (greenA * (1 - tweenFactor) + greenB * tweenFactor);
		int newBlue = (int) (blueA * (1 - tweenFactor) + blueB * tweenFactor);
		return (newRed << 16) | (newGreen << 8) | newBlue;
	}


}
