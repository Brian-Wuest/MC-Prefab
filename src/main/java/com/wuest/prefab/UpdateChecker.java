package com.wuest.prefab;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.CheckResult;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

/**
 * 
 * @author WuestMan
 *
 */
public class UpdateChecker
{
	private static ModInfo modInfo = null;
	
	/**
	 * Determines if a the message is shown to the user when they log into a world.
	 */
	public static boolean showMessage = false;
	
	/**
	 * The message to show to the user when they log into a world.
	 */
	public static String messageToShow = "";

	/**
	 * Checks the current version against the git-hub version.
	 */
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