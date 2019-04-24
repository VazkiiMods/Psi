/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [28/08/2016, 01:15:29 (GMT)]
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
