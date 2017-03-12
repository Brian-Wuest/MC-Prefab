package com.wuest.prefab;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.CheckResult;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class UpdateChecker
{
	private static ModInfo modInfo = null;
	public static boolean showMessage = false;
	public static String messageToShow = "";

	public static void checkVersion()
	{
		// Pull the repository information.
		ModContainer prefabMod = null;
		
		for (ModContainer modContainer : Loader.instance().getModList())
		{
			if (modContainer.getName().toLowerCase().equals(Prefab.MODID.toLowerCase()))
			{
				prefabMod = modContainer;
				break;
			}
		}
		
		if (prefabMod != null)
		{
			CheckResult result = ForgeVersion.getResult(prefabMod);
			
			if (result != null && result.status == Status.OUTDATED)
			{
				// Current version is out dated, show the message when the user is logged in.
				UpdateChecker.messageToShow = "[Prefab] There is a new version available! New Version: [" + result.target.toString() + "] Your Version: ["
						+ Prefab.VERSION + "]";
				
				UpdateChecker.showMessage = true;
			}
		}
	}
}