/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.client;

import net.minecraft.util.Mth;

public final class PsiColorHelper {

	private PsiColorHelper() {}

	public static int slide(int[] colors, float speed) {
		int count = colors.length;
		double time = (PsiClientRuntime.clientTicks() * speed * count / Math.PI) % count;
		int phase = (int) time;
		double delta = time - phase;
		if(delta == 0) {
			return colors[phase];
		}
		int next = (phase + 1) % count;
		float shift = (1 - Mth.cos((float) (delta * Math.PI))) / 2;
		return interpolate(colors[phase], colors[next], shift);
	}

	public static int pulse(int source, float speed, int magnitude) {
		int add = (int) (Mth.sin(PsiClientRuntime.clientTicks() * speed) * magnitude);
		int red = Mth.clamp(((source >> 16) & 0xFF) + add, 0, 255);
		int green = Mth.clamp(((source >> 8) & 0xFF) + add, 0, 255);
		int blue = Mth.clamp((source & 0xFF) + add, 0, 255);
		return 0xFF000000 | (red << 16) | (green << 8) | blue;
	}

	private static int interpolate(int first, int second, float shift) {
		int red = (int) (((first >> 16) & 0xFF) * (1 - shift) + ((second >> 16) & 0xFF) * shift);
		int green = (int) (((first >> 8) & 0xFF) * (1 - shift) + ((second >> 8) & 0xFF) * shift);
		int blue = (int) ((first & 0xFF) * (1 - shift) + (second & 0xFF) * shift);
		return 0xFF000000 | (red << 16) | (green << 8) | blue;
	}
}
