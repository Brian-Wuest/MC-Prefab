package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.structures.config.WareHouseConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureWarehouse;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;

/**
 * This class is used to hold the gui options for the warehouse.
 *
 * @author WuestMan
 */
public class GuiWareHouse extends GuiStructure {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/warehouse_top_down.png");
    protected GuiButtonExt btnGlassColor;
    protected WareHouseConfiguration configuration;
    protected String clientGUIIdentifier;
    protected String structureIdentifier;

    public GuiWareHouse() {
        super();
        this.structureConfiguration = EnumStructureConfiguration.WareHouse;
        this.clientGUIIdentifier = "Warehouse";
    }

    @Override
    public void Initialize() {
        this.modifiedInitialXAxis = 212;
        this.modifiedInitialYAxis = 117;
        this.shownImageHeight = 150;
        this.shownImageWidth = 268;
        this.structureImageLocation = structureTopDown;
        this.structureIdentifier = "item.prefab:item_warehouse.name";

        this.configuration = ClientEventHandler.playerConfig.getClientConfig(this.clientGUIIdentifier, WareHouseConfiguration.class);
        this.configuration.pos = this.pos;
        this.configuration.houseFacing = EnumFacing.NORTH;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnGlassColor = this.createAndAddFullDyeButton(4, grayBoxX + 15, grayBoxY + 45, 90, 20, this.configuration.dyeColor);

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
        // Draw the text here.
        this.drawString(GuiLangKeys.translateString(this.structureIdentifier), x + 15, y + 17, this.textColor);

        this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), x + 15, y + 35, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnGlassColor) {
            this.configuration.dyeColor = FullDyeColor.ById(this.configuration.dyeColor.getId() + 1);
            this.btnGlassColor.displayString = GuiLangKeys.translateFullDye(this.configuration.dyeColor);
        } else if (button == this.btnVisualize) {
            StructureWarehouse structure;

            if (this.configuration.advanced) {
                structure = StructureWarehouse.CreateInstance(StructureWarehouse.ADVANCED_ASSET_LOCATION, StructureWarehouse.class);
            } else {
                structure = StructureWarehouse.CreateInstance(StructureWarehouse.ASSETLOCATION, StructureWarehouse.class);
            }

            this.performPreview(structure, this.configuration);
        }
    }
}