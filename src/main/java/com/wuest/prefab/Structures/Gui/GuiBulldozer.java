package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Config.BulldozerConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import net.minecraft.client.gui.widget.button.Button;

import java.awt.*;

/**
 * @author WuestMan
 */
public class GuiBulldozer extends GuiStructure {

    protected BulldozerConfiguration configuration;

    /**
     * Initializes a new instance of the {@link GuiBulldozer} class.
     */
    public GuiBulldozer() {
        super("Bulldozer");

        this.structureConfiguration = EnumStructureConfiguration.Bulldozer;
    }

    @Override
    protected void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Bulldozer", BulldozerConfiguration.class);
        this.configuration.pos = this.pos;

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = this.getCenteredXAxis() - 125;
        int grayBoxY = this.getCenteredYAxis() - 83;

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

        this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
    public void render(int x, int y, float f) {
        int grayBoxX = this.getCenteredXAxis() - 125;
        int grayBoxY = this.getCenteredYAxis() - 83;

        this.renderBackground();

        this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

        assert this.minecraft != null;
        this.minecraft.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BULLDOZER_DESCRIPTION), grayBoxX + 10, grayBoxY + 10, 230, this.textColor);

        this.minecraft.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_CLEARED_AREA), grayBoxX + 10, grayBoxY + 40, 230, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(Button button) {
        assert this.minecraft != null;
        this.configuration.houseFacing = this.minecraft.player.getHorizontalFacing().getOpposite();
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);
    }

}
