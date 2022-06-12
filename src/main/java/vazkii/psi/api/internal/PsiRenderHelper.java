/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

public class PsiRenderHelper {

	/**
	 * Shim for rendering functions that don't take a MatrixStack when they should.
	 * Temporary, remove when Mojang adds MatrixStacks to those methods.
	 */
	public static void transferMsToGl(PoseStack ms, Runnable function) {
		var stack = RenderSystem.getModelViewStack();
		try {
			stack.pushPose();
			stack.mulPoseMatrix(ms.last().pose());
			RenderSystem.applyModelViewMatrix();
			function.run();
		} finally {
			stack.popPose();
			RenderSystem.applyModelViewMatrix();
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
