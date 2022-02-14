package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.ExtendedButton;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import com.wuest.prefab.gui.controls.GuiSlider;
import com.wuest.prefab.structures.base.EnumStructureMaterial;
import com.wuest.prefab.structures.config.InstantBridgeConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureInstantBridge;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("SpellCheckingInspection")
public class GuiInstantBridge extends GuiStructure {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/instant_bridge_top_down.png");
    protected InstantBridgeConfiguration specificConfiguration;
    private ExtendedButton btnMaterialType;
    private GuiSlider sldrBridgeLength;
    private GuiCheckBox chckIncludeRoof;
    private GuiSlider sldrInteriorHeight;

    public GuiInstantBridge() {
        super("Instant Bridge");
        this.configurationEnum = EnumStructureConfiguration.InstantBridge;
    }

    @Override
    public Component getNarrationMessage() {
        return new TranslatableComponent(GuiLangKeys.translateString(GuiLangKeys.TITLE_INSTANT_BRIDGE));
    }

    @Override
    protected void Initialize() {
        this.modifiedInitialXAxis = 212;
        this.modifiedInitialYAxis = 117;
        this.shownImageHeight = 150;
        this.shownImageWidth = 268;
        this.configuration = this.specificConfiguration = ClientEventHandler.playerConfig.getClientConfig("InstantBridge", InstantBridgeConfiguration.class);
        this.configuration.pos = this.pos;
        this.structureImageLocation = structureTopDown;

        StructureInstantBridge structure = new StructureInstantBridge();
        structure.getClearSpace().getShape().setDirection(Direction.SOUTH);
        structure.setupStructure(this.specificConfiguration, this.pos);
        this.selectedStructure = structure;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnMaterialType = this.createAndAddButton(grayBoxX + 15, grayBoxY + 45, 90, 20, this.specificConfiguration.bridgeMaterial.getName());
        this.sldrBridgeLength = this.createAndAddSlider(grayBoxX + 15, grayBoxY + 85, 90, 20, "", "", 25, 75, this.specificConfiguration.bridgeLength, false, true, this::buttonClicked);
        this.chckIncludeRoof = this.createAndAddCheckBox(grayBoxX + 15, grayBoxY + 112, GuiLangKeys.INCLUDE_ROOF, this.specificConfiguration.includeRoof, this::buttonClicked);
        this.sldrInteriorHeight = this.createAndAddSlider(grayBoxX + 15, grayBoxY + 140, 90, 20, "", "", 3, 8, this.specificConfiguration.interiorHeight, false, true, this::buttonClicked);
        this.sldrInteriorHeight.visible = this.chckIncludeRoof.isChecked();

        // Create the standard buttons.
        this.btnVisualize = this.createAndAddCustomButton(grayBoxX + 25, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
        this.btnBuild = this.createAndAddCustomButton(grayBoxX + 310, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
        this.btnCancel = this.createAndAddButton(grayBoxX + 150, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        int imagePanelUpperLeft = x + 132;
        int imagePanelWidth = 285;
        int imagePanelMiddle = imagePanelWidth / 2;

        this.renderBackground(matrixStack);

        this.drawControlLeftPanel(matrixStack, x + 10, y + 10, 125, 190);
        this.drawControlRightPanel(matrixStack, imagePanelUpperLeft, y + 10, imagePanelWidth, 190);

        int middleOfImage = this.shownImageWidth / 2;
        int imageLocation = imagePanelUpperLeft + (imagePanelMiddle - middleOfImage);

        GuiUtils.bindAndDrawScaledTexture(
                this.structureImageLocation,
                matrixStack,
                imageLocation,
                y + 15,
                this.shownImageWidth,
                this.shownImageHeight,
                this.shownImageWidth,
                this.shownImageHeight,
                this.shownImageWidth,
                this.shownImageHeight);
    }

    @Override
    protected void postButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawSplitString(GuiLangKeys.translateString("item.prefab.item_instant_bridge"), x + 15, y + 17, 100, this.textColor);

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.BRIDGE_MATERIAL), x + 15, y + 35, this.textColor);

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.BRIDGE_LENGTH), x + 15, y + 75, this.textColor);

        if (this.chckIncludeRoof.isChecked()) {
            this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.INTERIOR_HEIGHT), x + 15, y + 130, this.textColor);
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(AbstractButton button) {
        int sliderValue = this.sldrBridgeLength.getValueInt();

        if (sliderValue > 75) {
            sliderValue = 75;
        } else if (sliderValue < 25) {
            sliderValue = 25;
        }

        this.specificConfiguration.bridgeLength = sliderValue;

        sliderValue = this.sldrInteriorHeight.getValueInt();

        if (sliderValue > 8) {
            sliderValue = 8;
        } else if (sliderValue < 3) {
            sliderValue = 3;
        }

        this.specificConfiguration.interiorHeight = sliderValue;
        this.specificConfiguration.includeRoof = this.chckIncludeRoof.isChecked();
        this.configuration.houseFacing = player.getDirection().getOpposite();
        this.configuration.pos = this.pos;

        this.performCancelOrBuildOrHouseFacing(button);

        if (button == this.chckIncludeRoof) {
            this.specificConfiguration.includeRoof = this.chckIncludeRoof.isChecked();

            this.sldrInteriorHeight.visible = this.specificConfiguration.includeRoof;
        }
        if (button == this.btnMaterialType) {
            this.specificConfiguration.bridgeMaterial = EnumStructureMaterial.getMaterialByNumber(this.specificConfiguration.bridgeMaterial.getNumber() + 1);
            GuiUtils.setButtonText(btnMaterialType, this.specificConfiguration.bridgeMaterial.getTranslatedName());
        } else if (button == this.btnVisualize) {
            this.performPreview();
        }
    }
}
