package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.structures.config.ProduceFarmConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureProduceFarm;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

/**
 * @author WuestMan
 */
public class GuiProduceFarm extends GuiStructure {
    private static final ResourceLocation houseTopDown = new ResourceLocation("prefab", "textures/gui/produce_farm_top_down.png");
    protected ProduceFarmConfiguration configuration;
    private ExtendedButton btnGlassColor;

    public GuiProduceFarm() {
        super("Produce Farm");
        this.structureConfiguration = EnumStructureConfiguration.ProduceFarm;
        this.modifiedInitialXAxis = 210;
        this.modifiedInitialYAxis = 83;
    }

    @Override
    public void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Produce Farm", ProduceFarmConfiguration.class);
        this.configuration.pos = this.pos;

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
    protected Tuple<Integer, Integer> getAdjustedXYValue() {
        return new Tuple<>(this.getCenteredXAxis() - 210, this.getCenteredYAxis() - 83);
    }

    @Override
    protected void preButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.preButtonRender(matrixStack, x, y, mouseX, mouseY, partialTicks);

        GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(houseTopDown, matrixStack, x + 250, y, 1, 170, 171, 170, 171);
    }

    @Override
    protected void postButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), x + 10, y + 10, this.textColor);

        // Draw the text here.
        String strToDraw = GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED) + "\n \n" + GuiLangKeys.translateString(GuiLangKeys.PRODUCE_FARM_SIZE);
        this.drawSplitString(strToDraw, x + 147, y + 10, 100, this.textColor);
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
            StructureProduceFarm structure = StructureProduceFarm.CreateInstance(StructureProduceFarm.ASSETLOCATION, StructureProduceFarm.class);
            StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
            this.closeScreen();
        }
    }
}