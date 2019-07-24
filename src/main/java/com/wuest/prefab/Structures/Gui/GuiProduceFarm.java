package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Config.ProduceFarmConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureProduceFarm;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * @author WuestMan
 */
public class GuiProduceFarm extends GuiStructure {
    private static final ResourceLocation houseTopDown = new ResourceLocation("prefab", "textures/gui/produce_farm_top_down.png");
    protected GuiButtonExt btnGlassColor;
    protected ProduceFarmConfiguration configuration;

    public GuiProduceFarm() {
        super("Produce Farm");
        this.structureConfiguration = EnumStructureConfiguration.ProduceFarm;
    }

    @Override
    public void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Produce Farm", ProduceFarmConfiguration.class);
        this.configuration.pos = this.pos;

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = this.getCenteredXAxis() - 210;
        int grayBoxY = this.getCenteredYAxis() - 83;

        // Create the buttons.
        this.btnGlassColor = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateDye(this.configuration.dyeColor));

        this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

        this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
    public void render(int x, int y, float f) {
        int grayBoxX = this.getCenteredXAxis() - 210;
        int grayBoxY = this.getCenteredYAxis() - 83;

        this.renderBackground();

        // Draw the control background.
        this.minecraft.getTextureManager().bindTexture(houseTopDown);
        this.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 170, 171, 170, 171);

        this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

        // Draw the text here.
        this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), grayBoxX + 10, grayBoxY + 10, this.textColor);

        // Draw the text here.
        this.minecraft.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), grayBoxX + 147, grayBoxY + 10, 100, this.textColor);
        this.minecraft.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.PRODUCE_FARM_SIZE), grayBoxX + 147, grayBoxY + 50, 100, this.textColor);

        this.checkVisualizationSetting();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(Button button) {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnGlassColor) {
            this.configuration.dyeColor = DyeColor.byId(this.configuration.dyeColor.getId() + 1);
            this.btnGlassColor.setMessage(GuiLangKeys.translateDye(this.configuration.dyeColor));
        } else if (button == this.btnVisualize) {
            StructureProduceFarm structure = StructureProduceFarm.CreateInstance(StructureProduceFarm.ASSETLOCATION, StructureProduceFarm.class);
            StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
            this.minecraft.displayGuiScreen(null);
        }
    }
}