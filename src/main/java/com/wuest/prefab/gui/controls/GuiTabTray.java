package com.wuest.prefab.gui.controls;

import com.wuest.prefab.gui.GuiTabScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
public class GuiTabTray extends Gui {
    private static final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
    public int trayWidth = 50;
    public int trayX = 0;
    public int trayY = 0;
    private ArrayList<GuiTab> tabs;

    public GuiTabTray() {
        this.Initialize();
    }

    protected void Initialize() {
        this.tabs = new ArrayList<GuiTab>();
    }

    public GuiTab AddTab(GuiTab tab) {
        // The first tab is always selected.
        if (this.tabs.size() == 0) {
            tab.InternalSetSelected(true);
        }

        this.tabs.add(tab);
        return tab;
    }

    public void RemoveTab(GuiTab tab) {
        this.tabs.remove(tab);
    }

    public int TabCount() {
        return this.tabs.size();
    }

    public GuiTab GetSelectedTab() {
        if (this.tabs.size() > 0) {
            for (GuiTab tab : this.tabs) {
                if (tab.getIsSelected()) {
                    return tab;
                }
            }
        }

        return null;
    }

    public void SetSelectedTab(GuiTab tab) {
        GuiTab firstTab = null;

        for (GuiTab guiTab : this.tabs) {
            if (firstTab == null) {
                firstTab = guiTab;
            }

            guiTab.InternalSetSelected(false);
        }

        if (tab != null) {
            tab.InternalSetSelected(true);
        } else {
            firstTab.InternalSetSelected(true);
        }
    }

    public boolean DoesTabNameExist(String name) {
        for (GuiTab tab : this.tabs) {
            if (tab.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public void DrawTabs(Minecraft mc, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(backgroundTextures);
        GuiTabScreen.drawModalRectWithCustomSizedTexture(this.trayX, this.trayY, 0, this.trayWidth, 35, this.trayWidth, 35);

        for (GuiTab tab : this.tabs) {
            tab.drawTab(mc, mouseX, mouseY);
        }
    }

    public ArrayList<GuiTab> GetTabs() {
        return this.tabs;
    }
}