package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiTabScreen;
import com.wuest.prefab.Structures.Config.ChickenCoopConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureChickenCoop;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import javafx.util.Pair;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

/**
 * @author WuestMan
 */
public class GuiChickenCoop extends GuiStructure {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/chicken_coop_top_down.png");
    protected ChickenCoopConfiguration configuration;

    public GuiChickenCoop() {
        super("Chicken Coop");
        this.structureConfiguration = EnumStructureConfiguration.ChickenCoop;
    }

    @Override
    protected Pair<Integer, Integer> getAdjustedXYValue() {
        return new Pair<>(this.getCenteredXAxis() - 213, this.getCenteredYAxis() - 83);
    }

    @Override
    protected void preButtonRender(int x, int y)
    {
        super.preButtonRender(x , y);

        this.minecraft.getTextureManager().bindTexture(structureTopDown);
        GuiStructure.drawModalRectWithCustomSizedTexture(x + 250, y, 1, 171, 87, 171, 87);
    }

    @Override
    protected void postButtonRender(int x, int y) {
        this.minecraft.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), x + 147, y + 10, 95, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(Button button) {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnVisualize) {
            StructureChickenCoop structure = StructureChickenCoop.CreateInstance(StructureChickenCoop.ASSETLOCATION, StructureChickenCoop.class);
            StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
            assert this.minecraft != null;
            this.minecraft.displayGuiScreen(null);
        }
    }

    @Override
    protected void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Chicken Coop", ChickenCoopConfiguration.class);
        this.configuration.pos = this.pos;

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = this.getCenteredXAxis() - 213;
        int grayBoxY = this.getCenteredYAxis() - 83;

        // Create the buttons.
        this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

        this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
    }

}
