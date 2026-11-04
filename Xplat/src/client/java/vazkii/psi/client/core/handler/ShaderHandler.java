/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.lib.LibResources;

import java.io.IOException;
import java.util.function.Consumer;

public final class ShaderHandler {

	private static ShaderInstance psiBarShader;

	public static void register(Registrar registrar) throws IOException {
		registrar.register(PsiAPI.location(LibResources.SHADER_PSI_BAR), DefaultVertexFormat.POSITION_TEX_COLOR,
				shader -> psiBarShader = shader);
	}

	public static ShaderInstance getPsiBarShader() {
		return psiBarShader;
	}

	@FunctionalInterface
	public interface Registrar {
		void register(ResourceLocation id, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoaded) throws IOException;
	}

}
