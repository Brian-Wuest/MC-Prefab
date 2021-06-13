package com.wuest.prefab.gui.controls;

import com.wuest.prefab.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * @author WuestMan
 */
public class GuiTab extends Gui {
    protected static final ResourceLocation TAB_TEXTURES = new ResourceLocation("prefab", "textures/gui/gui_tab.png");
    protected static final ResourceLocation TAB_TEXTURES_hovered = new ResourceLocation("prefab", "textures/gui/gui_tab_hovered.png");
    /*
     * Determines if this tab is visible.
     */
    public boolean visible;
    /**
     * Button width in pixels
     */
    public int width;
    /**
     * Button height in pixels
     */
    public int height;
    /**
     * The x position of this control.
     */
    public int xPosition;
    /**
     * The y position of this control.
     */
    public int yPosition;
    protected boolean hovered;
    private GuiTabTray parentTray;
    private boolean selected;
    private String name;

    public GuiTab(GuiTabTray parent, String name, int x, int y) {
        this.Initialize(parent, name, x, y);
    }

    protected void Initialize(GuiTabTray parent, String name, int x, int y) {
        this.parentTray = parent;
        this.selected = false;
        this.name = name;
        this.height = 20;
        this.width = 50;
        this.xPosition = x;
        this.yPosition = y;
        this.visible = true;
    }

    public GuiTabTray getParent() {
        return this.parentTray;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) throws Exception {
        if (this.parentTray.DoesTabNameExist(value)) {
            Exception exception = new Exception("A tab with the name of [" + value + "] already exists.");
            throw exception;
        }

        this.name = value;
    }

    public boolean getIsSelected() {
        return this.selected;
    }

    public void setIsSelected(boolean value) {
        this.selected = value;

        this.parentTray.SetSelectedTab(this.selected ? this : null);
    }

    /**
     * This is an internal method used to set the boolean property without triggering the parent tray method.
     *
     * @param value The new value for the selected property.
     */
    protected void InternalSetSelected(boolean value) {
        this.selected = value;
    }

    /**
     * Draws this button to the screen.
     *
     * @param mc     The minecraft object.
     * @param mouseX The location of the mouse X-Axis.
     * @param mouseY The lcoation of the mouse Y-Axis.
     */
    public void drawTab(Minecraft mc, int mouseX, int mouseY) {
        if (!this.visible) {
            return;
        }

        FontRenderer fontrenderer = mc.fontRenderer;
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

        if (this.selected || this.hovered) {
            mc.getTextureManager().bindTexture(TAB_TEXTURES_hovered);
        } else {
            mc.getTextureManager().bindTexture(TAB_TEXTURES);
        }

        /*
         * if (this.hovered) { System.out.println("Hovering Over Tab: X: " + mouseX + " Y: " + mouseY); }
         */

        GuiUtils.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0, this.width, this.height, this.width, this.height);
        this.mouseDragged(mc, mouseX, mouseY);
        int j = Color.LIGHT_GRAY.getRGB();

        int stringXPosition = ((this.xPosition + this.width / 2) - (fontrenderer.getStringWidth(this.name)) / 2);
        fontrenderer.drawString(this.name, stringXPosition, this.yPosition + (this.height - 8) / 2, j);
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     *
     * @param mc     The minecraft object.
     * @param mouseX The location of the mouse X-Axis.
     * @param mouseY The lcoation of the mouse Y-Axis.
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     *
     * @param mouseX The location of the mouse X-Axis.
     * @param mouseY The lcoation of the mouse Y-Axis.
     */
    public void mouseReleased(int mouseX, int mouseY) {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     *
     * @param mc     The minecraft object.
     * @param mouseX The location of the mouse X-Axis.
     * @param mouseY The lcoation of the mouse Y-Axis.
     * @return True if this tab was clicked, otherwise false.
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        boolean value = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

        if (value && !this.selected) {
            // Select this tab;
            this.setIsSelected(true);
        }

        return value;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     *
     * @return True if this tab is being hovered over.
     */
    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
