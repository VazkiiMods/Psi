/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import com.google.common.io.CharStreams;
import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.lib.LibResources;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public final class ShaderHandler {

	private static final int VERT_ST = ARBVertexShader.GL_VERTEX_SHADER_ARB;
	private static final int FRAG_ST = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
	public static boolean useShaders = false;

	private static final int VERT = 1;
	private static final int FRAG = 2;

	private static final String VERT_EXTENSION = ".vsh";
	private static final String FRAG_EXTENSION = ".fsh";

	public static int rawColor;
	public static int psiBar;
	public static int simpleBloom;

	public static void init() {
		useShaders = canUseShaders();
		if (!useShaders) {
			return;
		}
		Psi.logger.info("Initializing Psi shaders!");

		rawColor = createProgram(LibResources.SHADER_RAW_COLOR, FRAG);
		psiBar = createProgram(LibResources.SHADER_PSI_BAR, FRAG);
		simpleBloom = createProgram(LibResources.SHADER_SIMPLE_BLOOM, FRAG);
	}

	public static void useShader(int shader, Consumer<Integer> callback) {
		if (!useShaders) {
			return;
		}

		ARBShaderObjects.glUseProgramObjectARB(shader);

		if (shader != 0) {
			int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);

			if (callback != null) {
				callback.accept(shader);
			}
		}
	}

	public static void useShader(int shader) {
		useShader(shader, null);
	}

	public static void releaseShader() {
		useShader(0);
	}

	public static boolean canUseShaders() {
		RenderSystem.assertOnRenderThread();
		return ConfigHandler.CLIENT.useShaders.get() && ((GL.getCapabilities().OpenGL14 && (GL.getCapabilities().GL_ARB_framebuffer_object || GL.getCapabilities().GL_EXT_framebuffer_object || GL.getCapabilities().OpenGL30))
				&& (GL.getCapabilities().OpenGL21 || GL.getCapabilities().GL_ARB_fragment_shader && GL.getCapabilities().GL_ARB_fragment_shader && GL.getCapabilities().GL_ARB_shader_objects));
	}

	private static int createProgram(String s, int sides) {
		boolean vert = (sides & VERT) != 0;
		boolean frag = (sides & FRAG) != 0;

		return createProgram(vert ? s + VERT_EXTENSION : null, frag ? s + FRAG_EXTENSION : null);
	}

	// Most of the code taken from the LWJGL wiki
	// http://lwjgl.org/wiki/index.php?title=GLSL_Shaders_with_LWJGL

	private static int createProgram(String vert, String frag) {
		int vertId = 0, fragId = 0, program;
		if (vert != null) {
			vertId = createShader(vert, VERT_ST);
		}
		if (frag != null) {
			fragId = createShader(frag, FRAG_ST);
		}

		program = ARBShaderObjects.glCreateProgramObjectARB();
		if (program == 0) {
			return 0;
		}

		if (vert != null) {
			ARBShaderObjects.glAttachObjectARB(program, vertId);
		}
		if (frag != null) {
			ARBShaderObjects.glAttachObjectARB(program, fragId);
		}

		ARBShaderObjects.glLinkProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
			Psi.logger.error(getLogInfo(program));
			return 0;
		}

		ARBShaderObjects.glValidateProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
			Psi.logger.error(getLogInfo(program));
			return 0;
		}

		return program;
	}

	private static int createShader(String filename, int shaderType) {
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if (shader == 0) {
				return 0;
			}

			ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
			ARBShaderObjects.glCompileShaderARB(shader);

			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
			}

			return shader;
		} catch (Exception e) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			e.printStackTrace();
			return -1;
		}
	}

	private static String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	private static String readFileAsString(String filename) throws Exception {
		try (InputStream in = ShaderHandler.class.getResourceAsStream(filename)) {
			if (in == null) {
				return "";
			}

			return CharStreams.toString(new InputStreamReader(in, StandardCharsets.UTF_8));
		}
	}
}
