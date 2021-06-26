package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Tuple;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.structures.config.ChickenCoopConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureChickenCoop;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * @author WuestMan
 */
public class GuiChickenCoop extends GuiStructure {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/chicken_coop_top_down.png");
    protected ChickenCoopConfiguration configuration;

    public GuiChickenCoop() {
        super();
        this.structureConfiguration = EnumStructureConfiguration.ChickenCoop;
    }

    @Override
    protected void Initialize() {
        this.modifiedInitialXAxis = 160;;
        this.modifiedInitialYAxis = 120;
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Chicken Coop", ChickenCoopConfiguration.class);
        this.configuration.pos = this.pos;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnVisualize = this.createAndAddCustomButton(4, grayBoxX + 113, grayBoxY + 165, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
        this.btnBuild = this.createAndAddCustomButton(1, grayBoxX + 215, grayBoxY + 165, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
        this.btnCancel = this.createAndAddButton(2, grayBoxX + 10, grayBoxY + 165, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        int imagePanelWidth = 330;
        int imagePanelHeight = 300;
        this.drawDefaultBackground();

        this.drawControlBackground(x, y, imagePanelWidth, imagePanelHeight);


        int imagePanelMiddle = imagePanelWidth / 2;

        int imageWidth = 268;
        int shownHeight = 150;
        int middleOfImage = imageWidth / 2;
        int imageLocation = x + (imagePanelMiddle - middleOfImage);

        GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(structureTopDown, imageLocation, y + 10, 1, imageWidth, shownHeight, imageWidth, shownHeight);
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnVisualize) {
            StructureChickenCoop structure = StructureChickenCoop.CreateInstance(StructureChickenCoop.ASSETLOCATION, StructureChickenCoop.class);
            StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
            this.closeScreen();
        }
    }
}
