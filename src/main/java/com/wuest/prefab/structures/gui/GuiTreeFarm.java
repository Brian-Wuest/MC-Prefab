package com.wuest.prefab.structures.gui;

import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.structures.config.TreeFarmConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureTreeFarm;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * @author WuestMan
 */
public class GuiTreeFarm extends GuiStructure {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/tree_farm_top_down.png");
    protected TreeFarmConfiguration configuration;

    public GuiTreeFarm() {
        super();
        this.structureConfiguration = EnumStructureConfiguration.TreeFarm;
        this.modifiedInitialXAxis = 213;
        this.modifiedInitialYAxis = 83;
    }

    @Override
    protected void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.preButtonRender(x, y, mouseX, mouseY, partialTicks);

        GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(structureTopDown, x + 250, y, 1, 177, 175, 177, 175);
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        String strToDraw = GuiLangKeys.translateString(GuiLangKeys.TREE_FARM_SIZE);
        this.drawSplitString(strToDraw, x + 147, y + 10, 100, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnVisualize) {
            StructureTreeFarm structure = StructureTreeFarm.CreateInstance(StructureTreeFarm.ASSETLOCATION, StructureTreeFarm.class);
            StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
            this.closeScreen();
        }
    }

    @Override
    protected void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Tree Farm", TreeFarmConfiguration.class);
        this.configuration.pos = this.pos;
        this.configuration.houseFacing = EnumFacing.NORTH;

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = this.getCenteredXAxis() - 213;
        int grayBoxY = this.getCenteredYAxis() - 83;

        // Create the buttons.
        this.btnVisualize = this.createAndAddButton(4, grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);

        this.btnCancel = this.createAndAddButton(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }
}
