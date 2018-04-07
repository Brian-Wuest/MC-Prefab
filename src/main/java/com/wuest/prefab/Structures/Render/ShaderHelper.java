package com.wuest.prefab.Structures.Render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import com.wuest.prefab.Events.ClientEventHandler;

import net.minecraft.client.renderer.OpenGlHelper;

/**
 * This class we derived from Botania's ShaderHelper. The only real change was to change the location of the assets when initializing the shader.
 * http://botaniamod.net/license.php
 * @author WuestMan
 *
 */
public class ShaderHelper
{
	private static final int VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;
	private static final int FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;

	public static int alphaShader = 0;

	public static void Initialize()
	{
		ShaderHelper.alphaShader = ShaderHelper.createProgram("/assets/prefab/shader/alpha.vert", "/assets/prefab/shader/alpha.frag");
	}

	public static void useShader(int shader)
	{
		if (!OpenGlHelper.shadersSupported)
		{
			return;
		}

		ARBShaderObjects.glUseProgramObjectARB(shader);

		if (shader != 0)
		{
			int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			ARBShaderObjects.glUniform1iARB(time, ClientEventHandler.ticksInGame);

			int alpha = ARBShaderObjects.glGetUniformLocationARB(shader, "alpha");
			ARBShaderObjects.glUniform1fARB(alpha, 0.4F);
		}
	}
	
	public static void releaseShader() 
	{
		ShaderHelper.useShader(0);
	}

	// Most of the code taken from the LWJGL wiki
	// http://lwjgl.org/wiki/index.php?title=GLSL_Shaders_with_LWJGL

	private static int createProgram(String vert, String frag)
	{
		int vertId = 0, fragId = 0, program;

		if (vert != null)
		{
			vertId = createShader(vert, VERT);
		}

		if (frag != null)
		{
			fragId = createShader(frag, FRAG);
		}

		program = ARBShaderObjects.glCreateProgramObjectARB();

		if (program == 0)
		{
			return 0;
		}

		if (vert != null)
		{
			ARBShaderObjects.glAttachObjectARB(program, vertId);
		}

		if (frag != null)
		{
			ARBShaderObjects.glAttachObjectARB(program, fragId);
		}

		ARBShaderObjects.glLinkProgramARB(program);

		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE)
		{
			return 0;
		}

		ARBShaderObjects.glValidateProgramARB(program);

		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
		{
			return 0;
		}

		return program;
	}

	private static int createShader(String filename, int shaderType)
	{
		int shader = 0;

		try
		{
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if (shader == 0)
			{
				return 0;
			}

			ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
			ARBShaderObjects.glCompileShaderARB(shader);

			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
			{
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
			}

			return shader;
		}
		catch (Exception e)
		{
			ARBShaderObjects.glDeleteObjectARB(shader);
			e.printStackTrace();
			return -1;
		}
	}

	private static String readFileAsString(String filename) throws Exception
	{
		InputStream in = ShaderHelper.class.getResourceAsStream(filename);

		if (in == null)
		{
			return "";
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8")))
		{
			return reader.lines().collect(Collectors.joining("\n"));
		}
	}

	private static String getLogInfo(int obj)
	{
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
}
