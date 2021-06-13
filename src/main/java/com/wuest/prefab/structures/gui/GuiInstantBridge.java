package com.wuest.prefab.structures.gui;

import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import com.wuest.prefab.structures.base.EnumStructureMaterial;
import com.wuest.prefab.structures.config.InstantBridgeConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureInstantBridge;
import com.wuest.prefab.structures.render.StructureRenderHandler;
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
        this.modifiedInitialXAxis = 210;
        this.modifiedInitialYAxis = 83;
    }

    @Override
    protected void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("InstantBridge", InstantBridgeConfiguration.class);
        this.configuration.pos = this.pos;

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = this.getCenteredXAxis() - 213;
        int grayBoxY = this.getCenteredYAxis() - 83;

        // Create the buttons.
        this.btnMaterialType = this.createAndAddButton(3, grayBoxX + 10, grayBoxY + 20, 90, 20, this.configuration.bridgeMaterial.getName());

        this.sldrBridgeLength = this.createAndAddSlider(5, grayBoxX + 147, grayBoxY + 20, 90, 20, "", "", 25, 75, this.configuration.bridgeLength, false, true);

        this.chckIncludeRoof = this.createAndAddCheckBox(6, grayBoxX + 147, grayBoxY + 55, GuiLangKeys.INCLUDE_ROOF, this.configuration.includeRoof);

        this.sldrInteriorHeight = this.createAndAddSlider(7, grayBoxX + 147, grayBoxY + 90, 90, 20, "", "", 3, 8, this.configuration.interiorHeight, false, true);
        this.sldrInteriorHeight.enabled = this.chckIncludeRoof.isChecked();

        this.btnVisualize = this.createAndAddButton(4, grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);

        this.btnCancel = this.createAndAddButton(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.preButtonRender(x, y, mouseX, mouseY, partialTicks);

        GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(structureTopDown, x + 250, y, 1, 165, 58, 165, 58);
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawString(GuiLangKeys.translateString(GuiLangKeys.BRIDGE_MATERIAL), x + 10, y + 10, this.textColor);

        if (this.chckIncludeRoof.enabled) {
            this.drawString(GuiLangKeys.translateString(GuiLangKeys.INTERIOR_HEIGHT), x + 144, y + 80, this.textColor);
        }

        this.drawString(GuiLangKeys.translateString(GuiLangKeys.BRIDGE_LENGTH), x + 147, y + 10, this.textColor);
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

            this.sldrInteriorHeight.enabled = this.configuration.includeRoof;
        }
        if (button == this.btnMaterialType) {
            this.configuration.bridgeMaterial = EnumStructureMaterial.getMaterialByNumber(this.configuration.bridgeMaterial.getNumber() + 1);
            this.btnMaterialType.displayString = this.configuration.bridgeMaterial.getTranslatedName();
        } else if (button == this.btnVisualize) {
            StructureInstantBridge structure = new StructureInstantBridge();
            structure.getClearSpace().getShape().setDirection(EnumFacing.SOUTH);
            structure.setupStructure(this.configuration, this.pos);

            StructureRenderHandler.setStructure(structure, EnumFacing.SOUTH, this.configuration);
            this.closeScreen();
        }
    }
}
