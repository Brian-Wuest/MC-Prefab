package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.structures.config.WareHouseConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureWarehouse;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;


/**
 * This class is used to hold the gui options for the warehouse.
 *
 * @author WuestMan
 */
public class GuiWareHouse extends GuiStructure {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/warehouse_top_down.png");
    protected WareHouseConfiguration configuration;
    protected String structureIdentifier;
    String clientGUIIdentifier;
    private ExtendedButton btnGlassColor;

    public GuiWareHouse() {
        super("Warehouse");
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
        this.structureIdentifier = "item.prefab.item_warehouse";

        this.configuration = ClientEventHandler.playerConfig.getClientConfig(this.clientGUIIdentifier, WareHouseConfiguration.class);
        this.configuration.pos = this.pos;
        this.configuration.houseFacing = Direction.NORTH;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnGlassColor = this.createAndAddFullDyeButton(grayBoxX + 15, grayBoxY + 45, 90, 20, this.configuration.dyeColor);

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
    protected void postButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        // Draw the text here.
        this.drawString(matrixStack, GuiLangKeys.translateString(this.structureIdentifier), x + 15, y + 17, this.textColor);

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), x + 15, y + 35, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(AbstractButton button) {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnGlassColor) {
            this.configuration.dyeColor = FullDyeColor.ById(this.configuration.dyeColor.getId() + 1);
            GuiUtils.setButtonText(btnGlassColor, GuiLangKeys.translateFullDye(this.configuration.dyeColor));
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