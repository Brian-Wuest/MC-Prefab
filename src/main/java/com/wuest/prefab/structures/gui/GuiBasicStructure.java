package com.wuest.prefab.Structures.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiUtils;
import com.wuest.prefab.Structures.Config.BasicStructureConfiguration;
import com.wuest.prefab.Structures.Config.Enums.BaseOption;
import com.wuest.prefab.Structures.Items.ItemBasicStructure;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureBasic;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import com.wuest.prefab.Tuple;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

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
    private boolean includePicture = true;
    private ExtendedButton btnBedColor = null;
    private ExtendedButton btnStructureOptions = null;
    private ArrayList<BaseOption> availableOptions;

    public GuiBasicStructure() {
        super("Basic Structure");
        this.structureConfiguration = EnumStructureConfiguration.Basic;
        this.modifiedInitialXAxis = 213;
        this.modifiedInitialYAxis = 83;
    }

    @Override
    protected void preButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.preButtonRender(matrixStack, x, y, mouseX, mouseY, partialTicks);

        if (this.includePicture) {
            // Draw the control background.
            GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(this.configuration.chosenOption.getPictureLocation(), matrixStack, x + 250, y, 1,
                    this.configuration.chosenOption.getImageWidth(), this.configuration.chosenOption.getImageHeight(),
                    this.configuration.chosenOption.getImageWidth(), this.configuration.chosenOption.getImageHeight());
        }
    }

    @Override
    protected void postButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        int yValue = y + 10;

        // Draw the text here.
        if (this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.MineshaftEntrance
                || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.WatchTower
                || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.WelcomeCenter) {

            this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 10, yValue, this.textColor);
            yValue += 40;
        }

        if (this.availableOptions.size() > 1) {
            this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.BUILDING_OPTIONS), x + 10, yValue, this.textColor);
        }

        // Second column
        yValue = y + 10;
        this.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), x + 147, yValue, 95, this.textColor);
    }

    @Override
    protected void Initialize() {
        // Get the structure configuration for this itemstack.
        ItemStack stack = ItemBasicStructure.getBasicStructureItemInHand(this.player);

        if (stack != null) {
            ItemBasicStructure item = (ItemBasicStructure) stack.getItem();
            this.configuration = ClientEventHandler.playerConfig.getClientConfig(item.structureType.getName(), BasicStructureConfiguration.class);
            this.configuration.basicStructureName = item.structureType;
            this.configuration.chosenOption = item.structureType.getBaseOption();
            this.includePicture = this.doesPictureExist();
            this.availableOptions = this.configuration.chosenOption.getSpecificOptions();
        }

        this.configuration.pos = this.pos;

        if (!this.includePicture) {
            this.modifiedInitialXAxis = 125;
        }

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        int x = grayBoxX + 10;
        int y = grayBoxY + 20;

        this.btnBedColor = this.createAndAddDyeButton(x, y, 90, 20, this.configuration.bedColor);

        if (this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.MineshaftEntrance
                || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.WatchTower
                || this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.WelcomeCenter) {
            this.btnBedColor.visible = true;
            y += 40;
        } else {
            this.btnBedColor.visible = false;
        }

        if (this.availableOptions.size() > 1) {
            this.btnStructureOptions = this.createAndAddButton(x, y, 90, 20, this.configuration.chosenOption.getTranslationString());
            this.btnStructureOptions.visible = true;
            y += 40;
        } else if (this.btnStructureOptions != null) {
            this.btnStructureOptions.visible = false;
        }

        this.btnVisualize = this.createAndAddButton(x, y, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);

        this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    /**
     * Determines if the picture for this screen exists in the resources.
     *
     * @return A value indicating whether the picture exists.
     */
    private boolean doesPictureExist() {
        try {
            this.getMinecraft().getResourceManager().getResource(this.configuration.chosenOption.getPictureLocation());
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    @Override
    public void buttonClicked(AbstractButton button) {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnVisualize) {
            StructureBasic structure = StructureBasic.CreateInstance(this.configuration.chosenOption.getAssetLocation(), StructureBasic.class);
            StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
            this.closeScreen();
        } else if (button == this.btnBedColor) {
            this.configuration.bedColor = DyeColor.byId(this.configuration.bedColor.getId() + 1);
            GuiUtils.setButtonText(btnBedColor, GuiLangKeys.translateDye(this.configuration.bedColor));
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
                    GuiUtils.setButtonText(btnStructureOptions, GuiLangKeys.translateString(chosenOption.getTranslationString()));
                    break;
                }
            }
        }
    }
}
