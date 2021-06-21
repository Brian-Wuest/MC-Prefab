package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.structures.config.MonsterMasherConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureMonsterMasher;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;

/**
 * @author WuestMan
 */
public class GuiMonsterMasher extends GuiStructure {
    private static final ResourceLocation houseTopDown = new ResourceLocation("prefab", "textures/gui/monster_masher_top_down.png");
    protected GuiButtonExt btnGlassColor;
    protected MonsterMasherConfiguration configuration;

    public GuiMonsterMasher() {
        super();
        this.structureConfiguration = EnumStructureConfiguration.MonsterMasher;
        this.modifiedInitialXAxis = 210;
        this.modifiedInitialYAxis = 83;
    }

    @Override
    public void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Monster Masher", MonsterMasherConfiguration.class);
        this.configuration.pos = this.pos;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnGlassColor = this.createAndAddFullDyeButton(10, grayBoxX + 10, grayBoxY + 20, 90, 20, this.configuration.dyeColor);

        this.btnVisualize = this.createAndAddButton(4, grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);

        this.btnCancel = this.createAndAddButton(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.preButtonRender(x, y, mouseX, mouseY, partialTicks);

        GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(houseTopDown, x + 250, y, 1, 108, 156, 108, 156);
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        // Draw the text here.
        this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), x + 10, y + 10, this.textColor);
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
            StructureMonsterMasher structure = StructureMonsterMasher.CreateInstance(StructureMonsterMasher.ASSETLOCATION, StructureMonsterMasher.class);
            StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
            this.closeScreen();
        }
    }
}
