package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.ExtendedButton;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.CustomStructureConfiguration;
import com.wuest.prefab.structures.custom.base.CustomStructureInfo;
import com.wuest.prefab.structures.items.ItemBlueprint;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import com.wuest.prefab.structures.predefined.StructureCustom;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.DyeColor;

public class GuiCustomStructure extends GuiStructure {
    protected CustomStructureInfo selectedStructureInfo;
    protected CustomStructureConfiguration specificConfiguration;
    protected boolean hasColorOptions;

    private ExtendedButton btnBedColor = null;
    private ExtendedButton btnGlassColor = null;

    public GuiCustomStructure() {
        super("Custom Structure");
        this.configurationEnum = StructureTagMessage.EnumStructureConfiguration.Custom;
    }

    @Override
    protected void Initialize() {
        super.Initialize();

        this.selectedStructureInfo = ItemBlueprint.getCustomStructureInHand(this.player);

        this.configuration = this.specificConfiguration = new CustomStructureConfiguration();
        this.configuration.pos = this.pos;
        this.configuration.houseFacing = this.houseFacing;
        this.specificConfiguration.customStructureName = this.selectedStructureInfo.displayName;
        this.selectedStructure = Structure.CreateInstanceFromFile(this.selectedStructureInfo.structureFilePath, StructureCustom.class);

        if (this.selectedStructureInfo.hasBedColorOptions || this.selectedStructureInfo.hasGlassColorOptions) {
            this.hasColorOptions = true;
        }

        this.modifiedInitialXAxis = 135;
        this.modifiedInitialYAxis = 100;
        this.imagePanelWidth = 270;
        this.imagePanelHeight = 220 - (!hasColorOptions ? 40 : 0);

        // Get the upper left-hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        int x = grayBoxX + 20;
        int y = grayBoxY + 35 - (!hasColorOptions ? 25 : 0);

        this.btnBedColor = this.createAndAddDyeButton(x, y, 90, 20, this.specificConfiguration.bedColor, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR));
        this.btnBedColor.visible = false;

        this.btnGlassColor = this.createAndAddFullDyeButton(x, y + 40, 90, 20, this.specificConfiguration.glassColor, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS));
        this.btnGlassColor.visible = false;

        // Create the standard buttons.
        this.btnVisualize = this.createAndAddCustomButton(grayBoxX + 20, y + 77, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
        this.btnBuild = this.createAndAddCustomButton(grayBoxX + 150, y + 40, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
        this.btnCancel = this.createAndAddButton(grayBoxX + 150, y + 77, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    public Component getNarrationMessage() {
        return new TranslatableComponent(this.selectedStructureInfo.displayName);
    }

    @Override
    protected void preButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        // Enable/disable button options here.
        super.preButtonRender(matrixStack, x, y, mouseX, mouseY, partialTicks);

        if (this.hasColorOptions) {
            if (this.selectedStructureInfo.hasGlassColorOptions) {
                this.btnGlassColor.visible = true;
            }

            if (this.selectedStructureInfo.hasBedColorOptions) {
                this.btnBedColor.visible = true;
            }
        }
    }

    @Override
    protected void postButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        int yValue = y + 5;
        int xValue = x + 20;

        // Write out text here for labels.
        this.drawSplitString(GuiLangKeys.translateString(this.specificConfiguration.customStructureName), xValue, yValue, 128, this.textColor);

        yValue += 20;

        // Draw the text here.
        if (this.selectedStructureInfo.hasBedColorOptions) {
            this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), xValue, yValue, this.textColor);
            yValue += 40;
        }

        if (this.selectedStructureInfo.hasGlassColorOptions) {
            this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), xValue, yValue, this.textColor);
        }
    }

    @Override
    public void buttonClicked(AbstractButton button) {
        this.performCancelOrBuildOrHouseFacing(button);

        if (button == this.btnVisualize) {
            this.performPreview();
        } else if (button == this.btnBedColor) {
            this.specificConfiguration.bedColor = DyeColor.byId(this.specificConfiguration.bedColor.getId() + 1);
            GuiUtils.setButtonText(this.btnBedColor, GuiLangKeys.translateDye(this.specificConfiguration.bedColor));
        } else if (button == this.btnGlassColor) {
            this.specificConfiguration.glassColor = FullDyeColor.ById(this.specificConfiguration.glassColor.getId() + 1);
            GuiUtils.setButtonText(this.btnGlassColor, GuiLangKeys.translateFullDye(this.specificConfiguration.glassColor));
        }
    }
}
