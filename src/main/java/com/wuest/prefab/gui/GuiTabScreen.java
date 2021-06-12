package com.wuest.prefab.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.gui.controls.GuiTab;
import com.wuest.prefab.gui.controls.GuiTabTray;
import com.wuest.prefab.structures.gui.GuiStructure;
import net.minecraft.client.gui.widget.button.AbstractButton;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
public class GuiTabScreen extends GuiStructure {
    protected GuiTabTray Tabs;

    public GuiTabScreen() {
        super("TabScreen");
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
    public void init() {
        this.Tabs.GetTabs().clear();
        this.children.add(this.Tabs);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Draw the default labels and buttons.
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        // Draw the tabs.
        assert this.minecraft != null;
        this.Tabs.DrawTabs(this.minecraft, matrixStack, mouseX, mouseY);
    }

    public void buttonClicked(AbstractButton button) {
        // This does nothing on purpose.
    }

    @Override
    protected void postButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {

    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean returnValue = false;

        if (mouseButton == 0) {
            // This handles the button presses.
            returnValue = super.mouseClicked(mouseX, mouseY, mouseButton);

            if (returnValue) {
                // Handle the tab clicking.
                ArrayList<GuiTab> guiTabs = this.Tabs.GetTabs();

                for (GuiTab tab : guiTabs) {
                    if (tab.mouseClicked(mouseX, mouseY, mouseButton)) {
                        assert this.minecraft != null;
                        tab.playDownSound(this.minecraft.getSoundManager());
                        this.tabClicked(tab);
                        break;
                    }
                }
            }
        }

        return returnValue;
    }
}
