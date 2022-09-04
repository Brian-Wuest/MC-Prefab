package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.config.ServerModConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.ExtendedButton;
import com.wuest.prefab.structures.config.BasicStructureConfiguration;
import com.wuest.prefab.structures.config.enums.BaseOption;
import com.wuest.prefab.structures.items.ItemBasicStructure;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureBasic;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used as the gui for all basic structures.
 *
 * @author WuestMan
 */
@SuppressWarnings({"ConstantConditions", "SpellCheckingInspection"})
public class GuiBasicStructure extends GuiStructure {
    public BasicStructureConfiguration specificConfiguration;
    protected ServerModConfiguration serverConfiguration;
    private ExtendedButton btnBedColor = null;
    private ExtendedButton btnGlassColor = null;
    private ExtendedButton btnStructureOptions = null;
    private ArrayList<BaseOption> availableOptions;
    private boolean showConfigurationOptions;

    public GuiBasicStructure() {
        super("Basic Structure");
        this.configurationEnum = EnumStructureConfiguration.Basic;
        this.showConfigurationOptions = false;
    }

    @Override
    public Component getNarrationMessage() {
        return Component.translatable(this.specificConfiguration.getDisplayName());
    }

    @Override
    protected void Initialize() {
        super.Initialize();

        // Get the structure configuration for this itemstack.
        ItemStack stack = ItemBasicStructure.getBasicStructureItemInHand(this.player);

        if (stack != null) {
            ItemBasicStructure item = (ItemBasicStructure) stack.getItem();
            this.serverConfiguration = Prefab.proxy.getServerConfiguration();
            this.configuration = this.specificConfiguration = ClientEventHandler.playerConfig.getClientConfig(item.structureType.getName(), BasicStructureConfiguration.class);
            this.specificConfiguration.basicStructureName = item.structureType;

            HashMap<String, Boolean> configurationSettings = this.serverConfiguration.structureOptions.get(item.structureType.getItemTranslationString());
            ArrayList<BaseOption> tempOptions = item.structureType.getBaseOption().getSpecificOptions();
            this.availableOptions = new ArrayList<>();

            for (BaseOption option : tempOptions) {
                if (configurationSettings == null || configurationSettings.get(option.getTranslationString())) {
                    this.availableOptions.add(option);
                }
            }
        }

        if (this.availableOptions.size() == 0) {
            this.showNoOptionsScreen();
            return;
        }

        this.specificConfiguration.chosenOption = this.availableOptions.get(0);
        this.structureImageLocation = this.specificConfiguration.chosenOption.getPictureLocation();

        this.configuration.pos = this.pos;
        this.configuration.houseFacing = this.houseFacing;
        this.selectedStructure = StructureBasic.CreateInstance(this.specificConfiguration.chosenOption.getAssetLocation(), StructureBasic.class);

        this.updatedRenderedStructure();

        if (this.availableOptions.size() > 1 || this.specificConfiguration.basicStructureName.shouldShowConfigurationOptions()) {
            this.showConfigurationOptions = true;
        }

        if (this.availableOptions.size() == 1 && this.showConfigurationOptions) {
            // Make sure that the only available option still needs settings to show.
            BaseOption option = this.availableOptions.get(0);

            if (!option.getHasBedColor() && !option.getHasGlassColor()) {
                this.showConfigurationOptions = false;
            }
        }

        if (!this.showConfigurationOptions) {
            this.InitializeStandardButtons();
        } else {
            this.modifiedInitialXAxis = 215;
            this.modifiedInitialYAxis = 117;
            this.imagePanelWidth = 285;

            // Get the upper left hand corner of the GUI box.
            Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
            int grayBoxX = adjustedXYValue.getFirst();
            int grayBoxY = adjustedXYValue.getSecond();

            // Create the buttons.
            int x = grayBoxX + 8;
            int y = grayBoxY + 45;

            if (this.availableOptions.size() > 1) {
                this.btnStructureOptions = this.createAndAddButton(x, y, 100, 20, this.specificConfiguration.chosenOption.getTranslationString(), GuiLangKeys.translateString(GuiLangKeys.BUILDING_OPTIONS));
                this.btnStructureOptions.visible = true;
                y += 45;
            } else if (this.btnStructureOptions != null) {
                this.btnStructureOptions.visible = false;
            }

            this.btnBedColor = this.createAndAddDyeButton(x, y, 90, 20, this.specificConfiguration.bedColor, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR));
            this.btnBedColor.visible = false;

            this.btnGlassColor = this.createAndAddFullDyeButton(x, y, 90, 20, this.specificConfiguration.glassColor, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS));
            this.btnGlassColor.visible = false;

            // Create the standard buttons.
            this.btnVisualize = this.createAndAddCustomButton(grayBoxX + 24, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
            this.btnBuild = this.createAndAddCustomButton(grayBoxX + 310, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
            this.btnCancel = this.createAndAddButton(grayBoxX + 154, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
        }
    }

    @Override
    protected void preButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (!this.showConfigurationOptions) {
            super.preButtonRender(matrixStack, x, y, mouseX, mouseY, partialTicks);
        } else {
            // Draw the control background.
            int imagePanelUpperLeft = x + 136;
            int imagePanelMiddle = this.imagePanelWidth / 2;

            this.renderBackground(matrixStack);

            this.drawControlLeftPanel(matrixStack, x + 2, y + 10, 185, 190);
            this.drawControlRightPanel(matrixStack, imagePanelUpperLeft, y + 10, this.imagePanelWidth, 190);

            // TODO: Remove this when structure is generated in GUI instead of showing a picture.
            int middleOfImage = this.shownImageWidth / 2;
            int imageLocation = imagePanelUpperLeft + (imagePanelMiddle - middleOfImage);

            // Draw the picture.
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

        if (this.btnBedColor != null) {
            this.btnBedColor.visible = false;
        }

        if (this.btnGlassColor != null) {
            this.btnGlassColor.visible = false;
        }

        int yValue = y + 45;

        if (this.availableOptions.size() > 1) {
            yValue = yValue + 45;
        }

        if (this.specificConfiguration.chosenOption.getHasBedColor()) {
            this.btnBedColor.visible = true;
            this.btnBedColor.y = yValue;

            yValue = yValue + 45;
        }

        if (this.specificConfiguration.chosenOption.getHasGlassColor()) {
            this.btnGlassColor.visible = true;
            this.btnGlassColor.y = yValue;
        }
    }

    @Override
    protected void postButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this.showConfigurationOptions) {
            this.drawSplitString(GuiLangKeys.translateString(this.specificConfiguration.basicStructureName.getItemTranslationString()), x + 8, y + 17, 128, this.textColor);

            int yValue = y + 35;

            if (this.availableOptions.size() > 1) {
                this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.BUILDING_OPTIONS), x + 8, yValue, this.textColor);
                yValue += 45;
            }

            // Draw the text here.
            if (this.specificConfiguration.chosenOption.getHasBedColor()) {
                this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 8, yValue, this.textColor);
                yValue += 45;
            }

            if (this.specificConfiguration.chosenOption.getHasGlassColor()) {
                this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), x + 8, yValue, this.textColor);
                yValue += 45;
            }
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

            this.updatedRenderedStructure();
        } else if (button == this.btnStructureOptions) {
            for (int i = 0; i < this.availableOptions.size(); i++) {
                BaseOption option = this.availableOptions.get(i);
                BaseOption chosenOption = null;

                if (this.specificConfiguration.chosenOption.getTranslationString().equals(option.getTranslationString())) {
                    if (i == this.availableOptions.size() - 1) {
                        // This is the last option, set the text to the first option.
                        chosenOption = this.availableOptions.get(0);
                    } else {
                        chosenOption = this.availableOptions.get(i + 1);
                    }
                }

                if (chosenOption != null) {
                    this.selectedStructure = StructureBasic.CreateInstance(chosenOption.getAssetLocation(), StructureBasic.class);
                    this.specificConfiguration.chosenOption = chosenOption;
                    this.structureImageLocation = this.specificConfiguration.chosenOption.getPictureLocation();
                    GuiUtils.setButtonText(btnStructureOptions, GuiLangKeys.translateString(chosenOption.getTranslationString()));
                    this.updatedRenderedStructure();
                    break;
                }
            }
        }
    }
}
