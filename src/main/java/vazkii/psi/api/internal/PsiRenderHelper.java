/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class PsiRenderHelper {

	/**
	 * Shim for rendering functions that don't take a MatrixStack when they should.
	 * Temporary, remove when Mojang adds MatrixStacks to those methods.
	 */
	public static void transferMsToGl(MatrixStack ms, Runnable function) {
		try {
			RenderSystem.pushMatrix();
			RenderSystem.multMatrix(ms.getLast().getMatrix());
			function.run();
		} finally {
			RenderSystem.popMatrix();
		}
	}

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
