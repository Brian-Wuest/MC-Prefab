package com.wuest.prefab.Structures.Render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wuest.prefab.Events.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraftforge.fml.ModList;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * This class we derived from Botania's ShaderHelper. The only real change was to change the location of the assets when
 * initializing the shader. http://botaniamod.net/license.php
 *
 * @author WuestMan
 */
@SuppressWarnings({"SpellCheckingInspection", "SameParameterValue", "WeakerAccess"})
public class ShaderHelper {
	public static final FloatBuffer FLOAT_BUF = MemoryUtil.memAllocFloat(1);
	public static boolean hasIncompatibleMods = false;
	public static int alphaShader = 0;

	private static final int VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;
	private static final int FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
	private static boolean lighting;
	private static boolean checkedIncompatibility = false;

	public static void Initialize() {
		if (Minecraft.getInstance().getResourceManager() instanceof IReloadableResourceManager) {
			((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(
					(IResourceManagerReloadListener) manager -> {
						ShaderHelper.checkIncompatibleMods();

						ShaderHelper.deleteShader(alphaShader);
						ShaderHelper.alphaShader = 0;

						ShaderHelper.alphaShader = ShaderHelper.createProgram("/assets/prefab/shader/alpha.vert", "/assets/prefab/shader/alpha.frag");
					});
		}
	}

	public static void useShader(int shader, ShaderCallback callback) {
		if (ShaderHelper.alphaShader == 0) {
			// Shader wasn't initialized, initialize it now.
			ShaderHelper.alphaShader = ShaderHelper.createProgram("/assets/prefab/shader/alpha.vert", "/assets/prefab/shader/alpha.frag");
		}

		lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);

		// disableLighting
		GlStateManager._disableLighting();

		// useProgram
		GlStateManager._glUseProgram(shader);

		if (shader != 0) {
			// getUniformLocation
			int time = GlStateManager._glGetUniformLocation(shader, "time");

			// uniform1
			GlStateManager._glUniform1i(time, ClientEventHandler.ticksInGame);

			if (callback != null)
				callback.call(shader);
		}
	}

	public static void useShader(int shader) {
		useShader(shader, null);
	}

	public static void releaseShader() {
		if (lighting) {
			RenderSystem.enableLighting();
		}

		ShaderHelper.useShader(0);
	}

	// Most of the code taken from the LWJGL wiki
	// http://lwjgl.org/wiki/index.php?title=GLSL_Shaders_with_LWJGL

	private static int createProgram(String vert, String frag) {
		int vertId = 0, fragId = 0, program;

		if (vert != null) {
			vertId = createShader(vert, VERT);
		}

		if (frag != null) {
			fragId = createShader(frag, FRAG);
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
			return 0;
		}

		ARBShaderObjects.glValidateProgramARB(program);

		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
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

	private static void deleteShader(int id) {
		if (id != 0) {
			ARBShaderObjects.glDeleteObjectARB(id);
		}
	}

	private static String readFileAsString(String filename) throws Exception {
		InputStream in = ShaderHelper.class.getResourceAsStream(filename);

		if (in == null) {
			return "";
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
			return reader.lines().collect(Collectors.joining("\n"));
		}
	}

	private static String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	private static boolean checkIncompatibleMods() {
		if (!checkedIncompatibility) {
			hasIncompatibleMods = ModList.get().isLoaded("optifine");
			checkedIncompatibility = true;
		}

		return !hasIncompatibleMods;
	}

	@FunctionalInterface
	public interface ShaderCallback {

		void call(int shader);

	}
}
