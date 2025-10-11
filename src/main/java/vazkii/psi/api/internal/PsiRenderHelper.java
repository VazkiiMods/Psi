/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

public class PsiRenderHelper {

	public static int r(int color) {
		return (color >> 16) & 0xFF;
	}

	public static int g(int color) {
		return (color >> 8) & 0xFF;
	}

	public static int b(int color) {
		return color & 0xFF;
	}

	public static int a(int color) {
		return (color >> 24) & 0xFF;
	}

}
