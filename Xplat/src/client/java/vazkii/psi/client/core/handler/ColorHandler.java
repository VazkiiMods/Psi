/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;

import vazkii.psi.common.client.PsiClientRuntime;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.ItemExosuitSensor;
import vazkii.psi.common.item.armor.ItemPsimetalArmor;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.component.ItemCADColorizer;

public class ColorHandler {

	public static void register(Registrar registrar) {
		registrar.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemPsimetalArmor) stack.getItem()).getColor(stack) : 0xFFFFFFFF, ModItems.psimetalExosuitBoots.get(), ModItems.psimetalExosuitChestplate.get(), ModItems.psimetalExosuitHelmet.get(), ModItems.psimetalExosuitLeggings.get());

		registrar.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemExosuitSensor) stack.getItem()).getColor(stack) : 0xFFFFFFFF, ModItems.exosuitSensorHeat.get(), ModItems.exosuitSensorLight.get(), ModItems.exosuitSensorStress.get(), ModItems.exosuitSensorWater.get(), ModItems.exosuitSensorTrigger.get());

		registrar.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemCAD) stack.getItem()).getSpellColor(stack) : 0xFFFFFFFF, ModItems.cad.get());

		registrar.register((stack, tintIndex) -> tintIndex != 1 ? -1 : ((ItemCADColorizer) stack.getItem()).getColor(stack), ModItems.cadColorizerWhite.get(), ModItems.cadColorizerOrange.get(), ModItems.cadColorizerMagenta.get(), ModItems.cadColorizerLightBlue.get(),
				ModItems.cadColorizerYellow.get(), ModItems.cadColorizerLime.get(), ModItems.cadColorizerPink.get(), ModItems.cadColorizerGray.get(), ModItems.cadColorizerLightGray.get(), ModItems.cadColorizerCyan.get(), ModItems.cadColorizerPurple.get(), ModItems.cadColorizerBlue.get(),
				ModItems.cadColorizerBrown.get(), ModItems.cadColorizerGreen.get(), ModItems.cadColorizerRed.get(), ModItems.cadColorizerBlack.get());
	}

	@FunctionalInterface
	public interface Registrar {
		void register(ItemColor color, Item... items);
	}

	public static int slideColor(int[] color, float speed) {
		int n = color.length;
		double t = (PsiClientRuntime.clientTicks() * speed * n / Math.PI) % n;
		int phase = (int) t;
		double dt = t - phase;
		if(dt == 0) {
			return color[phase];
		}
		int nextPhase = (phase + 1) % n;
		return slideColorTime(color[phase], color[nextPhase], (float) (dt * Math.PI));
	}

	public static int pulseColor(int source, float speed, int magnitude) {
		return pulseColor(source, 1f, speed, magnitude);
	}

	public static int pulseColor(int source, float multiplier, float speed, int magnitude) {
		int add = (int) (Mth.sin(PsiClientRuntime.clientTicks() * speed) * magnitude);
		int red = (0x00FF0000 & source) >> 16;
		int green = (0x0000FF00 & source) >> 8;
		int blue = 0x000000FF & source;
		int addedRed = Mth.clamp((int) (multiplier * (red + add)), 0, 255);
		int addedGreen = Mth.clamp((int) (multiplier * (green + add)), 0, 255);
		int addedBlue = Mth.clamp((int) (multiplier * (blue + add)), 0, 255);
		return 0xFF000000 | (addedRed << 16) | (addedGreen << 8) | addedBlue;
	}

	public static int slideColorTime(int color, int secondColor, float t) {
		float shift = (1 - Mth.cos(t)) / 2;
		if(shift == 0) {
			return color;
		} else if(shift == 1) {
			return secondColor;
		}

		int redA = (0x00FF0000 & color) >> 16;
		int greenA = (0x0000FF00 & color) >> 8;
		int blueA = 0x000000FF & color;
		int redB = (0x00FF0000 & secondColor) >> 16;
		int greenB = (0x0000FF00 & secondColor) >> 8;
		int blueB = 0x000000FF & secondColor;

		int newRed = (int) (redA * (1 - shift) + redB * shift);
		int newGreen = (int) (greenA * (1 - shift) + greenB * shift);
		int newBlue = (int) (blueA * (1 - shift) + blueB * shift);
		return 0xFF000000 | (newRed << 16) | (newGreen << 8) | newBlue;
	}

}
