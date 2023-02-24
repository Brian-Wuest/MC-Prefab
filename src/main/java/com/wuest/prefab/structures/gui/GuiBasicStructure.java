package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.structures.config.BasicStructureConfiguration;
import com.wuest.prefab.structures.config.enums.BaseOption;
import com.wuest.prefab.structures.items.ItemBasicStructure;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureBasic;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is used as the gui for all basic structures.
 *
 * @author WuestMan
 */
@SuppressWarnings({"ConstantConditions", "SpellCheckingInspection"})
public class GuiBasicStructure extends GuiStructure {
    protected BasicStructureConfiguration configuration;
    private GuiButtonExt btnBedColor = null;
    private GuiButtonExt btnGlassColor = null;
    private GuiButtonExt btnStructureOptions = null;
    private ArrayList<BaseOption> availableOptions;
    private boolean showConfigurationOptions;
    private int imagePanelUpperLeft;

    public GuiBasicStructure() {
        super();
        this.structureConfiguration = EnumStructureConfiguration.Basic;
        this.showConfigurationOptions = false;
    }

    @Override
    protected void Initialize() {
        super.Initialize();

        // Get the structure configuration for this itemstack.
        ItemStack stack = ItemBasicStructure.getBasicStructureItemInHand(this.player);

        if (stack != null) {
            ItemBasicStructure item = (ItemBasicStructure) stack.getItem();
            this.configuration = ClientEventHandler.playerConfig.getClientConfig(item.structureType.getName(), BasicStructureConfiguration.class);
            this.configuration.basicStructureName = item.structureType;

            if (this.configuration.chosenOption.getClass() != item.structureType.getBaseOption().getClass()) {
                this.configuration.chosenOption = item.structureType.getBaseOption();
            }

            this.structureImageLocation = this.configuration.chosenOption.getPictureLocation();
            this.availableOptions = this.configuration.chosenOption.getSpecificOptions();
        }

        this.configuration.pos = this.pos;

        if (this.availableOptions.size() > 1
            || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.MineshaftEntrance
                || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.WatchTower
                || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.WelcomeCenter
                || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.VillagerHouses
                || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.AdvancedWarehouse
                || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.Warehouse
                || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.ProduceFarm
                || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.MonsterMasher
                || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.MachineryTower) {
            this.showConfigurationOptions = true;
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
                this.btnStructureOptions = this.createAndAddButton(7, x, y, 100, 20, this.configuration.chosenOption.getTranslationString());
                this.btnStructureOptions.visible = true;
                y += 45;
            } else if (this.btnStructureOptions != null) {
                this.btnStructureOptions.visible = false;
            }

            this.btnBedColor = this.createAndAddDyeButton(5, x, y, 90, 20, this.configuration.bedColor);
            this.btnBedColor.visible = false;

            this.btnGlassColor = this.createAndAddFullDyeButton(6, x, y, 90, 20, this.configuration.glassColor);
            this.btnGlassColor.visible = false;

            // Create the standard buttons.
            this.btnVisualize = this.createAndAddCustomButton(3, grayBoxX + 24, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
            this.btnBuild = this.createAndAddCustomButton(1, grayBoxX + 310, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
            this.btnCancel = this.createAndAddButton(2, grayBoxX + 154, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
        }
    }

    @Override
    protected void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (!this.showConfigurationOptions) {
            super.preButtonRender(x, y, mouseX, mouseY, partialTicks);
        } else {
            // Draw the control background.
            int imagePanelUpperLeft = x + 136;
            int imagePanelMiddle = this.imagePanelWidth / 2;

            this.drawDefaultBackground();

            this.drawControlLeftPanel(x + 2, y + 10, 185, 190);
            this.drawControlRightPanel(imagePanelUpperLeft, y + 10, this.imagePanelWidth, 190);

            int middleOfImage = this.shownImageWidth / 2;
            int imageLocation = imagePanelUpperLeft + (imagePanelMiddle - middleOfImage);
            GuiUtils.bindTexture(this.structureImageLocation);

            // Draw the picture.
            Gui.drawScaledCustomSizeModalRect(
                    imageLocation,
                    y + 15,
                    0,
                    0,
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

        if (this.configuration.chosenOption.getHasBedColor()) {
            this.btnBedColor.visible = true;
            this.btnBedColor.y = yValue;

            yValue = yValue + 45;
        }

        if (this.configuration.chosenOption.getHasGlassColor()) {
            this.btnGlassColor.visible = true;
            this.btnGlassColor.y = yValue;
        }
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this.showConfigurationOptions) {
            this.drawSplitString(GuiLangKeys.translateString(this.configuration.basicStructureName.getItemTranslationString()), x + 8, y + 17, 128, this.textColor);

            int yValue = y + 35;

            if (this.availableOptions.size() > 1) {
                this.drawString(GuiLangKeys.translateString(GuiLangKeys.BUILDING_OPTIONS), x + 8, yValue, this.textColor);
                yValue += 45;
            }

            // Draw the text here.
            if (this.configuration.chosenOption.getHasBedColor()) {
                this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 8, yValue, this.textColor);
                yValue += 45;
            }

            if (this.configuration.chosenOption.getHasGlassColor()) {
                this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), x + 8, yValue, this.textColor);
                yValue += 45;
            }
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnVisualize) {
            StructureBasic structure = StructureBasic.CreateInstance(this.configuration.chosenOption.getAssetLocation(), StructureBasic.class);
            this.performPreview(structure, this.configuration);
        } else if (button == this.btnBedColor) {
            this.configuration.bedColor = EnumDyeColor.byMetadata(this.configuration.bedColor.getMetadata() + 1);
            GuiUtils.setButtonText(this.btnBedColor, GuiLangKeys.translateDye(this.configuration.bedColor));
        } else if (button == this.btnGlassColor) {
            this.configuration.glassColor = FullDyeColor.ById(this.configuration.glassColor.getId() + 1);
            GuiUtils.setButtonText(this.btnGlassColor, GuiLangKeys.translateFullDye(this.configuration.glassColor));
        } else if (button == this.btnStructureOptions) {
            for (int i = 0; i < this.availableOptions.size(); i++) {
                BaseOption option = this.availableOptions.get(i);
                BaseOption chosenOption = null;

                if (this.configuration.chosenOption.getTranslationString().equals(option.getTranslationString())) {
                    if (i == this.availableOptions.size() - 1) {
                        // This is the last option, set the text to the first option.
                        chosenOption = this.availableOptions.get(0);
                    } else {
                        chosenOption = this.availableOptions.get(i + 1);
                    }
                }

                if (chosenOption != null) {
                    this.configuration.chosenOption = chosenOption;
                    this.structureImageLocation = this.configuration.chosenOption.getPictureLocation();
                    GuiUtils.setButtonText(btnStructureOptions, GuiLangKeys.translateString(chosenOption.getTranslationString()));
                    break;
                }
            }
        }
    }
}
