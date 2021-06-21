package com.wuest.prefab.structures.gui;

import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.structures.config.ModularHouseConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureModularHouse;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * @author WuestMan
 */
public class GuiModularHouse extends GuiStructure {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/modular_house_top_down.png");
    protected ModularHouseConfiguration configuration;

    public GuiModularHouse() {
        super();
        this.structureConfiguration = EnumStructureConfiguration.ModularHouse;
        this.modifiedInitialXAxis = 188;
        this.modifiedInitialYAxis = 83;
    }

    @Override
    protected void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.preButtonRender(x, y, mouseX, mouseY, partialTicks);

        GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(structureTopDown, x + 250, y, 1, 171, 87, 171, 87);
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnVisualize) {
            StructureModularHouse structure = StructureModularHouse.CreateInstance(StructureModularHouse.ASSETLOCATION, StructureModularHouse.class);
            StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
            this.closeScreen();
        }
    }

    @Override
    protected void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Modular House", ModularHouseConfiguration.class);
        this.configuration.pos = this.pos;

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = (this.width / 2) - 213;
        int grayBoxY = (this.height / 2) - 83;

        // Create the buttons.
        this.btnVisualize = this.createAndAddButton(4, grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);

        this.btnCancel = this.createAndAddButton(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }
}