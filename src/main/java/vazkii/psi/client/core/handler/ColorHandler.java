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

		items.register((stack, tintIndex) -> 0xFFFFF, ModItems.cadColorizerRainbow, ModItems.cadColorizerPsi);

		items.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemPsimetalArmor) stack.getItem()).getColor(stack) : 0xFFFFFF, ModItems.psimetalExosuitBoots, ModItems.psimetalExosuitChestplate, ModItems.psimetalExosuitBoots, ModItems.psimetalExosuitLeggings);

		items.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemExosuitSensor) stack.getItem()).getColor(stack) : 0xFFFFFF, ModItems.exosuitSensorHeat, ModItems.exosuitSensorLight, ModItems.exosuitSensorStress, ModItems.exosuitSensorWater);

		items.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemCAD) stack.getItem()).getSpellColor(stack) : 0xFFFFFF, ModItems.cad);

		items.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemCADColorizer) stack.getItem()).getColor(stack) : 0xFFFFF, ModItems.cadColorizerWhite, ModItems.cadColorizerOrange, ModItems.cadColorizerMagenta, ModItems.cadColorizerLightBlue,
				ModItems.cadColorizerYellow, ModItems.cadColorizerLime, ModItems.cadColorizerPink, ModItems.cadColorizerGray, ModItems.cadColorizerLightGray, ModItems.cadColorizerCyan, ModItems.cadColorizerPurple, ModItems.cadColorizerBlue,
				ModItems.cadColorizerBrown, ModItems.cadColorizerGreen, ModItems.cadColorizerRed, ModItems.cadColorizerBlack);
	}
}
