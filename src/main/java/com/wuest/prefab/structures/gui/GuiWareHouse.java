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
    private static final ResourceLocation wareHouseTopDown = new ResourceLocation("prefab", "textures/gui/warehouse_top_down.png");
    protected WareHouseConfiguration configuration;
    String clientGUIIdentifier;
    private ExtendedButton btnGlassColor;

    public GuiWareHouse() {
        super("Warehouse");
        this.structureConfiguration = EnumStructureConfiguration.WareHouse;
        this.clientGUIIdentifier = "Warehouse";
        this.modifiedInitialXAxis = 180;
        this.modifiedInitialYAxis = 83;
    }

    @Override
    public void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig(this.clientGUIIdentifier, WareHouseConfiguration.class);
        this.configuration.pos = this.pos;
        this.configuration.houseFacing = Direction.NORTH;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnGlassColor = this.createAndAddFullDyeButton(grayBoxX + 10, grayBoxY + 20, 90, 20, this.configuration.dyeColor);

        this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);

        this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.preButtonRender(matrixStack, x, y, mouseX, mouseY, partialTicks);

        GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(wareHouseTopDown, matrixStack, x + 250, y, 1, 132, 153, 132, 153);
    }

    @Override
    protected void postButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), x + 10, y + 10, this.textColor);

        // Draw the text here.
        this.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), x + 147, y + 10, 95, this.textColor);
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

            StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);

            this.closeScreen();
        }
    }
}