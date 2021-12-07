package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Tuple;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import com.wuest.prefab.structures.base.EnumStructureMaterial;
import com.wuest.prefab.structures.config.InstantBridgeConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureInstantBridge;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class GuiInstantBridge extends GuiStructure {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/instant_bridge_top_down.png");
    protected InstantBridgeConfiguration configuration;
    protected GuiButtonExt btnMaterialType;
    protected GuiSlider sldrBridgeLength;
    protected GuiCheckBox chckIncludeRoof;
    protected GuiSlider sldrInteriorHeight;

    public GuiInstantBridge() {
        super();
        this.structureConfiguration = EnumStructureConfiguration.InstantBridge;
    }

    @Override
    protected void Initialize() {
        this.modifiedInitialXAxis = 212;
        this.modifiedInitialYAxis = 117;
        this.shownImageHeight = 150;
        this.shownImageWidth = 268;
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("InstantBridge", InstantBridgeConfiguration.class);
        this.configuration.pos = this.pos;
        this.structureImageLocation = structureTopDown;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnMaterialType = this.createAndAddButton(4, grayBoxX + 15, grayBoxY + 45, 90, 20, this.configuration.bridgeMaterial.getName());
        this.sldrBridgeLength = this.createAndAddSlider(5, grayBoxX + 15, grayBoxY + 85, 90, 20, "", "", 25, 75, this.configuration.bridgeLength, false, true);
        this.chckIncludeRoof = this.createAndAddCheckBox(6, grayBoxX + 15, grayBoxY + 112, GuiLangKeys.INCLUDE_ROOF, this.configuration.includeRoof);
        this.sldrInteriorHeight = this.createAndAddSlider(7, grayBoxX + 15, grayBoxY + 140, 90, 20, "", "", 3, 8, this.configuration.interiorHeight, false, true);
        this.sldrInteriorHeight.enabled = this.chckIncludeRoof.isChecked();

        // Create the standard buttons.
        this.btnVisualize = this.createAndAddCustomButton(3, grayBoxX + 25, grayBoxY + 175, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
        this.btnBuild = this.createAndAddCustomButton(1, grayBoxX + 310, grayBoxY + 175, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
        this.btnCancel = this.createAndAddButton(2, grayBoxX + 150, grayBoxY + 175, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        int imagePanelUpperLeft = x + 132;
        int imagePanelWidth = 285;
        int imagePanelMiddle = imagePanelWidth / 2;

        this.drawDefaultBackground();

        this.drawControlLeftPanel(x + 10, y + 10, 125, 190);
        this.drawControlRightPanel(imagePanelUpperLeft, y + 10, imagePanelWidth, 190);

        int middleOfImage = this.shownImageWidth / 2;
        int imageLocation = imagePanelUpperLeft + (imagePanelMiddle - middleOfImage);

        GuiUtils.bindTexture(this.structureImageLocation);
        Gui.drawScaledCustomSizeModalRect(imageLocation, y + 15, 0, 0, this.shownImageWidth, this.shownImageHeight, this.shownImageWidth, this.shownImageHeight, this.shownImageWidth, this.shownImageHeight);
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawString(GuiLangKeys.translateString("item.prefab:item_instant_bridge.name"), x + 15, y + 17, this.textColor);

        this.drawString(GuiLangKeys.translateString(GuiLangKeys.BRIDGE_MATERIAL), x + 15, y + 35, this.textColor);

        this.drawString(GuiLangKeys.translateString(GuiLangKeys.BRIDGE_LENGTH), x + 15, y + 75, this.textColor);

        if (this.chckIncludeRoof.isChecked()) {
            this.drawString(GuiLangKeys.translateString(GuiLangKeys.INTERIOR_HEIGHT), x + 15, y + 130, this.textColor);
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        int sliderValue = this.sldrBridgeLength.getValueInt();

        if (sliderValue > 75) {
            sliderValue = 75;
        } else if (sliderValue < 25) {
            sliderValue = 25;
        }

        this.configuration.bridgeLength = sliderValue;

        sliderValue = this.sldrInteriorHeight.getValueInt();

        if (sliderValue > 8) {
            sliderValue = 8;
        } else if (sliderValue < 3) {
            sliderValue = 3;
        }

        this.configuration.interiorHeight = sliderValue;

        this.configuration.houseFacing = player.getHorizontalFacing().getOpposite();
        this.configuration.pos = this.pos;

        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.chckIncludeRoof) {
            this.configuration.includeRoof = this.chckIncludeRoof.isChecked();

            this.sldrInteriorHeight.visible = this.configuration.includeRoof;
        }
        if (button == this.btnMaterialType) {
            this.configuration.bridgeMaterial = EnumStructureMaterial.getMaterialByNumber(this.configuration.bridgeMaterial.getNumber() + 1);
            this.btnMaterialType.displayString = this.configuration.bridgeMaterial.getTranslatedName();
        } else if (button == this.btnVisualize) {
            StructureInstantBridge structure = new StructureInstantBridge();
            structure.getClearSpace().getShape().setDirection(EnumFacing.SOUTH);
            structure.setupStructure(this.configuration, this.pos);

            this.performPreview(structure, this.configuration);
        }
    }
}
