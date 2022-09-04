package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.config.ServerModConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.ExtendedButton;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import com.wuest.prefab.structures.config.HouseImprovedConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureModerateHouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author WuestMan
 */
public class GuiHouseImproved extends GuiStructure {
    protected HouseImprovedConfiguration specificConfiguration;
    protected ServerModConfiguration serverConfiguration;
    private ExtendedButton btnHouseStyle;
    private GuiCheckBox btnAddChest;
    private GuiCheckBox btnAddChestContents;
    private GuiCheckBox btnAddMineShaft;
    private ExtendedButton btnBedColor;
    private boolean allowItemsInChestAndFurnace = true;

    private ArrayList<HouseImprovedConfiguration.HouseStyle> availableHouseStyles;

    public GuiHouseImproved() {
        super("Moderate House");

        this.configurationEnum = EnumStructureConfiguration.ModerateHouse;
    }

    @Override
    public Component getNarrationMessage() {
        return Component.translatable(GuiLangKeys.translateString(GuiLangKeys.TITLE_MODERATE_HOUSE));
    }

    @Override
    protected void Initialize() {
        this.modifiedInitialXAxis = 215;
        this.modifiedInitialYAxis = 117;
        this.shownImageHeight = 150;
        this.shownImageWidth = 268;

        if (!Minecraft.getInstance().player.isCreative()) {
            this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
        }

        this.serverConfiguration = Prefab.proxy.getServerConfiguration();
        this.configuration = this.specificConfiguration = ClientEventHandler.playerConfig.getClientConfig("Moderate Houses", HouseImprovedConfiguration.class);
        this.configuration.pos = this.pos;

        this.availableHouseStyles = new ArrayList<>();
        HashMap<String, Boolean> houseConfigurationSettings = this.serverConfiguration.structureOptions.get("item.prefab.item_house_improved");
        boolean selectedStyleInListOfAvailable = false;

        for (HouseImprovedConfiguration.HouseStyle style : HouseImprovedConfiguration.HouseStyle.values()) {
            if (houseConfigurationSettings.get(style.getDisplayName())) {
                this.availableHouseStyles.add(style);

                if (this.specificConfiguration.houseStyle.getDisplayName().equals(style.getDisplayName())) {
                    selectedStyleInListOfAvailable = true;
                }
            }
        }

        if (this.availableHouseStyles.size() == 0) {
            // There are no options. Show the no options screen.
            this.showNoOptionsScreen();
            return;
        }

        if (!selectedStyleInListOfAvailable) {
            this.specificConfiguration.houseStyle = this.availableHouseStyles.get(0);
        }

        this.selectedStructure = StructureModerateHouse.CreateInstance(this.specificConfiguration.houseStyle.getStructureLocation(), StructureModerateHouse.class);

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        int yOffset = 25;

        if (this.availableHouseStyles.size() > 1) {
            this.btnHouseStyle = this.createAndAddButton(grayBoxX + 8, grayBoxY + yOffset, 90, 20, this.specificConfiguration.houseStyle.getDisplayName(), false, GuiLangKeys.translateString(GuiLangKeys.HOUSE_STYLE));
            yOffset = yOffset + 35;
        }

        this.btnBedColor = this.createAndAddDyeButton(grayBoxX + 8, grayBoxY + yOffset, 90, 20, this.specificConfiguration.bedColor, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR));
        yOffset = yOffset + 60;

        this.btnAddChest = this.createAndAddCheckBox(grayBoxX + 8, grayBoxY + yOffset, GuiLangKeys.HOUSE_ADD_CHEST, this.specificConfiguration.addChests, this::buttonClicked);
        yOffset = yOffset + 17;

        this.btnAddMineShaft = this.createAndAddCheckBox(grayBoxX + 8, grayBoxY + yOffset, GuiLangKeys.HOUSE_BUILD_MINESHAFT, this.specificConfiguration.addMineshaft, this::buttonClicked);
        yOffset = yOffset + 17;

        this.btnAddChestContents = this.createAndAddCheckBox(grayBoxX + 8, grayBoxY + yOffset, GuiLangKeys.HOUSE_ADD_CHEST_CONTENTS, this.specificConfiguration.addChestContents, this::buttonClicked);

        // Create the standard buttons.
        this.btnVisualize = this.createAndAddCustomButton(grayBoxX + 24, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
        this.btnBuild = this.createAndAddCustomButton(grayBoxX + 310, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
        this.btnCancel = this.createAndAddButton(grayBoxX + 154, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        int imagePanelUpperLeft = x + 136;
        int imagePanelWidth = 285;
        int imagePanelMiddle = imagePanelWidth / 2;

        this.renderBackground(matrixStack);

        this.drawControlLeftPanel(matrixStack, x + 2, y + 10, 135, 190);
        this.drawControlRightPanel(matrixStack, imagePanelUpperLeft, y + 10, imagePanelWidth, 190);

        int middleOfImage = this.shownImageWidth / 2;
        int imageLocation = imagePanelUpperLeft + (imagePanelMiddle - middleOfImage);

        GuiUtils.bindAndDrawScaledTexture(
                this.specificConfiguration.houseStyle.getHousePicture(),
                matrixStack,
                imageLocation,
                y + 15,
                this.shownImageWidth,
                this.shownImageHeight,
                this.shownImageWidth,
                this.shownImageHeight,
                this.shownImageWidth,
                this.shownImageHeight);

        this.btnAddChest.visible = this.serverConfiguration.addChests;
        this.btnAddChestContents.visible = this.allowItemsInChestAndFurnace && this.serverConfiguration.addChestContents;
        this.btnAddMineShaft.visible = this.serverConfiguration.addMineshaft;
    }

    @Override
    protected void postButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        // Draw the text here.
        int yOffSet = 15;

        if (this.availableHouseStyles.size() > 1) {
            this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.HOUSE_STYLE), x + 8, y + yOffSet, this.textColor);
            yOffSet = yOffSet + 35;
        }

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 8, y + yOffSet, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(AbstractButton button) {
        this.specificConfiguration.addChests = this.btnAddChest.visible && this.btnAddChest.isChecked();
        this.specificConfiguration.addChestContents = this.allowItemsInChestAndFurnace && (this.btnAddChestContents.visible && this.btnAddChestContents.isChecked());
        this.specificConfiguration.addMineshaft = this.btnAddMineShaft.visible && this.btnAddMineShaft.isChecked();
        this.configuration.houseFacing = this.getMinecraft().player.getDirection().getOpposite();

        this.performCancelOrBuildOrHouseFacing(button);

        if (button == this.btnHouseStyle) {
            for (int i = 0; i < this.availableHouseStyles.size(); i++) {
                HouseImprovedConfiguration.HouseStyle option = this.availableHouseStyles.get(i);
                HouseImprovedConfiguration.HouseStyle chosenOption = null;

                if (this.specificConfiguration.houseStyle.getDisplayName().equals(option.getDisplayName())) {
                    if (i == this.availableHouseStyles.size() - 1) {
                        // This is the last option, set the text to the first option.
                        chosenOption = this.availableHouseStyles.get(0);
                    } else {
                        chosenOption = this.availableHouseStyles.get(i + 1);
                    }
                }

                if (chosenOption != null) {
                    this.specificConfiguration.houseStyle = chosenOption;
                    this.selectedStructure = StructureModerateHouse.CreateInstance(this.specificConfiguration.houseStyle.getStructureLocation(), StructureModerateHouse.class);
                    GuiUtils.setButtonText(btnHouseStyle, this.specificConfiguration.houseStyle.getDisplayName());
                    break;
                }
            }
        } else if (button == this.btnVisualize) {
            this.performPreview();
        } else if (button == this.btnBedColor) {
            this.specificConfiguration.bedColor = DyeColor.byId(this.specificConfiguration.bedColor.getId() + 1);
            GuiUtils.setButtonText(btnBedColor, GuiLangKeys.translateDye(this.specificConfiguration.bedColor));
        }
    }
}
