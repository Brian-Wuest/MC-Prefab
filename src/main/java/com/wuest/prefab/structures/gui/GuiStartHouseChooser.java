package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.config.ModConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiTabScreen;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import com.wuest.prefab.gui.controls.GuiTab;
import com.wuest.prefab.structures.config.HouseConfiguration;
import com.wuest.prefab.structures.config.HouseConfiguration.HouseStyle;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureAlternateStart;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;
import java.io.IOException;

/**
 * @author WuestMan
 */
public class GuiStartHouseChooser extends GuiStructure {
    protected ModConfiguration serverConfiguration;
    // General:
    private GuiButtonExt btnHouseStyle;
    // Blocks/Size
    private GuiButtonExt btnGlassColor;
    private GuiButtonExt btnBedColor;
    // Config:
    private GuiCheckBox btnAddChest;
    private GuiCheckBox btnAddChestContents;
    private GuiCheckBox btnAddMineShaft;
    private boolean allowItemsInChestAndFurnace = true;

    private HouseConfiguration configuration;

    public GuiStartHouseChooser() {
        super();
        this.modifiedInitialXAxis = 198;
        this.modifiedInitialYAxis = 83;
        this.structureConfiguration = EnumStructureConfiguration.StartHouse;
    }

    @Override
    protected void Initialize() {
        this.modifiedInitialXAxis = 212;
        this.modifiedInitialYAxis = 117;
        this.shownImageHeight = 150;
        this.shownImageWidth = 268;

        if (!Minecraft.getMinecraft().player.isCreative()) {
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
        this.btnHouseStyle = this.createAndAddButton(4, grayBoxX + 15, grayBoxY + 30, 90, 20, this.configuration.houseStyle.getDisplayName(), false);
        this.btnBedColor = this.createAndAddDyeButton(5, grayBoxX + 15, grayBoxY + 65, 90, 20, this.configuration.bedColor);
        this.btnGlassColor = this.createAndAddFullDyeButton(6, grayBoxX + 15, grayBoxY + 100, 90, 20, this.configuration.glassColor);
        this.btnAddChest = this.createAndAddCheckBox(7, grayBoxX + 15, grayBoxY + 125, GuiLangKeys.STARTER_HOUSE_ADD_CHEST, this.configuration.addChest);
        this.btnAddMineShaft = this.createAndAddCheckBox(9, grayBoxX + 15, grayBoxY + 140, GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT, this.configuration.addChestContents);
        this.btnAddChestContents = this.createAndAddCheckBox(8, grayBoxX + 15, grayBoxY + 155, GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS, this.configuration.addMineShaft);

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

        GuiUtils.bindTexture(this.configuration.houseStyle.getHousePicture());
        Gui.drawScaledCustomSizeModalRect(imageLocation, y + 15, 0, 0, this.shownImageWidth, this.shownImageHeight, this.shownImageWidth, this.shownImageHeight, this.shownImageWidth, this.shownImageHeight);

        this.btnAddChest.visible = this.serverConfiguration.addChests;
        this.btnAddChestContents.visible = this.allowItemsInChestAndFurnace && this.serverConfiguration.addChestContents;
        this.btnAddMineShaft.visible = this.serverConfiguration.addMineshaft;
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        // Draw the text here.
        this.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), x + 15, y + 20, this.textColor);

        this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 15, y + 55, this.textColor);

        this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), x + 15, y + 90, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        this.configuration.addBed = this.serverConfiguration.addBed;
        this.configuration.addChest = this.serverConfiguration.addChests && this.btnAddChest.isChecked();
        this.configuration.addChestContents = this.allowItemsInChestAndFurnace && (this.serverConfiguration.addChestContents && this.btnAddChestContents.isChecked());
        this.configuration.addCraftingTable = this.serverConfiguration.addCraftingTable;
        this.configuration.addFurnace = this.serverConfiguration.addFurnace;
        this.configuration.addMineShaft = this.serverConfiguration.addMineshaft && this.btnAddMineShaft.isChecked();
        this.configuration.addTorches = this.serverConfiguration.addTorches;
        this.configuration.houseFacing = this.getMinecraft().player.getHorizontalFacing().getOpposite();

        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnHouseStyle) {
            int id = this.configuration.houseStyle.getValue() + 1;
            this.configuration.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);

            // Skip the loft if it's not enabled.
            if (this.configuration.houseStyle == HouseStyle.LOFT
                    && !this.serverConfiguration.enableLoftHouse) {
                id = this.configuration.houseStyle.getValue() + 1;
                this.configuration.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);
            }

            GuiUtils.setButtonText(btnHouseStyle, this.configuration.houseStyle.getDisplayName());
        } else if (button == this.btnGlassColor) {
            this.configuration.glassColor = FullDyeColor.ById(this.configuration.glassColor.getId() + 1);
            GuiUtils.setButtonText(this.btnGlassColor, GuiLangKeys.translateFullDye(this.configuration.glassColor));
        } else if (button == this.btnBedColor) {
            this.configuration.bedColor = EnumDyeColor.byMetadata(this.configuration.bedColor.getMetadata() + 1);
            GuiUtils.setButtonText(btnBedColor, GuiLangKeys.translateDye(this.configuration.bedColor));
        } else if (button == this.btnVisualize) {
            StructureAlternateStart structure = StructureAlternateStart.CreateInstance(this.configuration.houseStyle.getStructureLocation(), StructureAlternateStart.class);
            this.performPreview(structure, this.configuration);
        }
    }
}
