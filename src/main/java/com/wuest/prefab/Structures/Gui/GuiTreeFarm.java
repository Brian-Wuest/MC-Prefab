package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Config.TreeFarmConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureTreeFarm;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

/**
 * @author WuestMan
 */
public class GuiTreeFarm extends GuiStructure {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/tree_farm_top_down.png");
    protected TreeFarmConfiguration configuration;

    public GuiTreeFarm() {
        super("Tree Farm");
        this.structureConfiguration = EnumStructureConfiguration.TreeFarm;
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
    public void render(int x, int y, float f) {
        int grayBoxX = this.getCenteredXAxis() - 213;
        int grayBoxY = this.getCenteredYAxis() - 83;

        this.renderBackground();

        // Draw the control background.
        this.minecraft.getTextureManager().bindTexture(structureTopDown);
        this.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 177, 175, 177, 175);

        this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

        // Draw the text here.
        this.minecraft.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), grayBoxX + 147, grayBoxY + 10, 100, this.textColor);
        this.minecraft.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.TREE_FARM_SIZE), grayBoxX + 147, grayBoxY + 50, 100, this.textColor);

        this.checkVisualizationSetting();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(Button button) {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnVisualize) {
            StructureTreeFarm structure = StructureTreeFarm.CreateInstance(StructureTreeFarm.ASSETLOCATION, StructureTreeFarm.class);
            StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
            this.minecraft.displayGuiScreen(null);
        }
    }

    @Override
    protected void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Tree Farm", TreeFarmConfiguration.class);
        this.configuration.pos = this.pos;
        this.configuration.houseFacing = Direction.NORTH;

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = this.getCenteredXAxis() - 213;
        int grayBoxY = this.getCenteredYAxis() - 83;

        // Create the buttons.
        this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

        this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
    }
}
