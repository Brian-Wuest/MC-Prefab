package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.config.ServerModConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import com.wuest.prefab.structures.config.HouseConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import com.wuest.prefab.structures.predefined.StructureAlternateStart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.item.DyeColor;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

/**
 * @author WuestMan
 */
public class GuiStartHouseChooser extends GuiStructure {
    protected ServerModConfiguration serverConfiguration;

    // General:
    private ExtendedButton btnHouseStyle;
    // Blocks/Size
    private ExtendedButton btnGlassColor;
    private ExtendedButton btnBedColor;
    // Config:
    private GuiCheckBox btnAddChest;
    private GuiCheckBox btnAddChestContents;
    private GuiCheckBox btnAddMineShaft;
    private boolean allowItemsInChestAndFurnace = true;

    private HouseConfiguration configuration;

    public GuiStartHouseChooser() {
        super("Starter House");
        this.structureConfiguration = StructureTagMessage.EnumStructureConfiguration.StartHouse;
    }

    @Override
    protected void Initialize() {
        this.modifiedInitialXAxis = 212;
        this.modifiedInitialYAxis = 117;
        this.shownImageHeight = 150;
        this.shownImageWidth = 268;

        if (!Minecraft.getInstance().player.isCreative()) {
            this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
        }

        this.serverConfiguration = Prefab.proxy.getServerConfiguration();
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Starter House", HouseConfiguration.class);
        this.configuration.pos = this.pos;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnHouseStyle = this.createAndAddButton(grayBoxX + 15, grayBoxY + 30, 90, 20, this.configuration.houseStyle.getDisplayName(), false);
        this.btnBedColor = this.createAndAddDyeButton(grayBoxX + 15, grayBoxY + 65, 90, 20, this.configuration.bedColor);
        this.btnGlassColor = this.createAndAddFullDyeButton(grayBoxX + 15, grayBoxY + 100, 90, 20, this.configuration.glassColor);
        this.btnAddChest = this.createAndAddCheckBox(grayBoxX + 15, grayBoxY + 125, GuiLangKeys.STARTER_HOUSE_ADD_CHEST, this.configuration.addChest, null);
        this.btnAddMineShaft = this.createAndAddCheckBox(grayBoxX + 15, grayBoxY + 140, GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT, this.configuration.addChestContents, null);
        this.btnAddChestContents = this.createAndAddCheckBox(grayBoxX + 15, grayBoxY + 155, GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS, this.configuration.addMineShaft, null);

        // Create the standard buttons.
        this.btnVisualize = this.createAndAddCustomButton(grayBoxX + 25, grayBoxY + 175, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
        this.btnBuild = this.createAndAddCustomButton(grayBoxX + 310, grayBoxY + 175, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
        this.btnCancel = this.createAndAddButton(grayBoxX + 150, grayBoxY + 175, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        int imagePanelUpperLeft = x + 132;
        int imagePanelWidth = 285;
        int imagePanelMiddle = imagePanelWidth / 2;

        this.renderBackground(matrixStack);

        this.drawControlLeftPanel(matrixStack, x + 10, y + 10, 125, 190);
        this.drawControlRightPanel(matrixStack, imagePanelUpperLeft, y + 10, imagePanelWidth, 190);

        int middleOfImage = this.shownImageWidth / 2;
        int imageLocation = imagePanelUpperLeft + (imagePanelMiddle - middleOfImage);

        GuiUtils.bindAndDrawScaledTexture(
                this.configuration.houseStyle.getHousePicture(),
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
    protected void postButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        // Draw the text here.
        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), x + 15, y + 20, this.textColor);

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 15, y + 55, this.textColor);

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), x + 15, y + 90, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(AbstractButton button) {
        this.configuration.addBed = this.serverConfiguration.addBed;
        this.configuration.addChest = this.serverConfiguration.addChests && this.btnAddChest.isChecked();
        this.configuration.addChestContents = this.allowItemsInChestAndFurnace && (this.serverConfiguration.addChestContents && this.btnAddChestContents.isChecked());
        this.configuration.addCraftingTable = this.serverConfiguration.addCraftingTable;
        this.configuration.addFurnace = this.serverConfiguration.addFurnace;
        this.configuration.addMineShaft = this.serverConfiguration.addMineshaft && this.btnAddMineShaft.isChecked();
        this.configuration.addTorches = this.serverConfiguration.addTorches;
        this.configuration.houseFacing = this.getMinecraft().player.getDirection().getOpposite();

        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnHouseStyle) {
            int id = this.configuration.houseStyle.getValue() + 1;
            this.configuration.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);

            // Skip the loft if it's not enabled.
            if (this.configuration.houseStyle == HouseConfiguration.HouseStyle.LOFT
                    && !this.serverConfiguration.enableLoftHouse) {
                id = this.configuration.houseStyle.getValue() + 1;
                this.configuration.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);
            }

            GuiUtils.setButtonText(btnHouseStyle, this.configuration.houseStyle.getDisplayName());
        } else if (button == this.btnGlassColor) {
            this.configuration.glassColor = FullDyeColor.ById(this.configuration.glassColor.getId() + 1);
            GuiUtils.setButtonText(this.btnGlassColor, GuiLangKeys.translateFullDye(this.configuration.glassColor));
        } else if (button == this.btnBedColor) {
            this.configuration.bedColor = DyeColor.byId(this.configuration.bedColor.getId() + 1);
            GuiUtils.setButtonText(btnBedColor, GuiLangKeys.translateDye(this.configuration.bedColor));
        } else if (button == this.btnVisualize) {
            StructureAlternateStart structure = StructureAlternateStart.CreateInstance(this.configuration.houseStyle.getStructureLocation(), StructureAlternateStart.class);
            this.performPreview(structure, this.configuration);
        }
    }
}
