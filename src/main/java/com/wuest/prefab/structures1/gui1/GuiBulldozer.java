package com.wuest.prefab.structures.gui;

import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.structures.config.BulldozerConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;
import java.io.IOException;

/**
 * @author WuestMan
 */
public class GuiBulldozer extends GuiStructure {

    protected BulldozerConfiguration configuration;

    /**
     * Intializes a new instance of the {@link GuiBulldozer} class.
     *
     * @param x The x-axis location.
     * @param y The y-axis location.
     * @param z the z-axis location.
     */
    public GuiBulldozer(int x, int y, int z) {
        super(x, y, z, true);

        this.structureConfiguration = EnumStructureConfiguration.Bulldozer;
    }

    @Override
    protected void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Bulldozer", BulldozerConfiguration.class);
        this.configuration.pos = this.pos;
        int color = Color.DARK_GRAY.getRGB();

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = this.getCenteredXAxis() - 125;
        int grayBoxY = this.getCenteredYAxis() - 83;

        // Create the done and cancel buttons.
        this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
        this.buttonList.add(this.btnBuild);

        this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
        this.buttonList.add(this.btnCancel);
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
    public void drawScreen(int x, int y, float f) {
        int grayBoxX = this.getCenteredXAxis() - 125;
        int grayBoxY = this.getCenteredYAxis() - 83;

        this.drawDefaultBackground();

        this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

        this.mc.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BULLDOZER_DESCRIPTION), grayBoxX + 10, grayBoxY + 10, 230, this.textColor);

        this.mc.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_CLEARED_AREA), grayBoxX + 10, grayBoxY + 40, 230, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.configuration.houseFacing = Minecraft.getMinecraft().player.getHorizontalFacing().getOpposite();
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);
    }

}
