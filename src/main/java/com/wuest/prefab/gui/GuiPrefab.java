package com.wuest.prefab.gui;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.config.ModConfiguration;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

/**
 * @author WuestMan
 */
public class GuiPrefab extends GuiConfig {
    public GuiPrefab(GuiScreen parent) {
        super(parent, new ConfigElement(Prefab.config.getCategory(ModConfiguration.OPTIONS)).getChildElements(), Prefab.MODID, null, false, false,
                GuiConfig.getAbridgedConfigPath(Prefab.config.toString()), null);

        ConfigCategory category = Prefab.config.getCategory(ModConfiguration.OPTIONS);
        String abridgedConfigPath = GuiConfig.getAbridgedConfigPath(Prefab.config.toString());
    }
}
