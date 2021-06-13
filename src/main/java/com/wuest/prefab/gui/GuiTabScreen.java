package com.wuest.prefab.gui;

import com.wuest.prefab.gui.controls.GuiTab;
import com.wuest.prefab.gui.controls.GuiTabTray;
import com.wuest.prefab.structures.gui.GuiStructure;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author WuestMan
 */
public class GuiTabScreen extends GuiStructure {
    protected GuiTabTray Tabs;

    public GuiTabScreen() {
        super();
        this.Tabs = new GuiTabTray();
    }

    /**
     * Processes when this tab is clicked.
     *
     * @param tab The tab which was clicked.
     */
    protected void tabClicked(GuiTab tab) {
    }

    protected GuiTab getSelectedTab() {
        return this.Tabs.GetSelectedTab();
    }

    @Override
    public void initGui() {
        this.Tabs.GetTabs().clear();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw the default labels and buttons.
        super.drawScreen(mouseX, mouseY, partialTicks);

        // Draw the tabs.
        this.Tabs.DrawTabs(this.mc, mouseX, mouseY);
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {

    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            // This handles the button presses.
            super.mouseClicked(mouseX, mouseY, mouseButton);

            // Handle the tab clicking.
            ArrayList<GuiTab> guiTabs = this.Tabs.GetTabs();

            for (GuiTab tab : guiTabs) {
                if (tab.mousePressed(mc, mouseX, mouseY)) {
                    tab.playPressSound(mc.getSoundHandler());
                    this.tabClicked(tab);
                    break;
                }
            }
        }
    }
}
