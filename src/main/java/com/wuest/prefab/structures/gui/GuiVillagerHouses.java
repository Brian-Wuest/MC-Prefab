package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Tuple;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.structures.config.VillagerHouseConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureVillagerHouses;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;

/**
 * @author WuestMan
 */
public class GuiVillagerHouses extends GuiStructure {
    private static final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
    protected GuiButtonExt btnHouseStyle;
    protected GuiButtonExt btnBedColor;
    protected VillagerHouseConfiguration configuration;
    protected VillagerHouseConfiguration.HouseStyle houseStyle;

    public GuiVillagerHouses() {
        super();
        this.structureConfiguration = EnumStructureConfiguration.VillagerHouses;
        this.modifiedInitialXAxis = 205;
        this.modifiedInitialYAxis = 83;
    }

    @Override
    public void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Villager Houses", VillagerHouseConfiguration.class);
        this.configuration.pos = this.pos;
        this.configuration.houseFacing = EnumFacing.NORTH;
        this.houseStyle = this.configuration.houseStyle;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        this.btnHouseStyle = this.createAndAddButton(4, grayBoxX + 10, grayBoxY + 20, 90, 20, this.houseStyle.getDisplayName(), false);

        // Create the buttons.
        this.btnVisualize = this.createAndAddButton(4, grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);

        int x = grayBoxX + 130;
        int y = grayBoxY + 20;

        this.btnBedColor = this.createAndAddDyeButton(8, x, y, 90, 20, this.configuration.bedColor);

        this.btnBedColor.visible = this.houseStyle == VillagerHouseConfiguration.HouseStyle.LONG_HOUSE;

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);

        this.btnCancel = this.createAndAddButton(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.preButtonRender(x, y, mouseX, mouseY, partialTicks);

        GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(this.houseStyle.getHousePicture(), x + 250, y, 1,
                this.houseStyle.getImageWidth(), this.houseStyle.getImageHeight(),
                this.houseStyle.getImageWidth(), this.houseStyle.getImageHeight());
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), x + 10, y + 10, this.textColor);

        if (this.houseStyle == VillagerHouseConfiguration.HouseStyle.LONG_HOUSE) {
            this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 130, y + 10, this.textColor);
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.configuration.houseStyle = this.houseStyle;

        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnHouseStyle) {
            int id = this.houseStyle.getValue() + 1;
            this.houseStyle = VillagerHouseConfiguration.HouseStyle.ValueOf(id);

            this.btnHouseStyle.displayString = this.houseStyle.getDisplayName();
        } else if (button == this.btnVisualize) {
            StructureVillagerHouses structure = StructureVillagerHouses.CreateInstance(this.houseStyle.getStructureLocation(), StructureVillagerHouses.class);
            StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
            this.closeScreen();
        } else if (button == this.btnBedColor) {
            this.configuration.bedColor = EnumDyeColor.byMetadata(this.configuration.bedColor.getMetadata() + 1);
            GuiUtils.setButtonText(btnBedColor, GuiLangKeys.translateDye(this.configuration.bedColor));
        }
    }
}