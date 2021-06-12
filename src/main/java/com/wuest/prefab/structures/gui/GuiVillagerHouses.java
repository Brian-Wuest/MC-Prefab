package com.wuest.prefab.Structures.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiUtils;
import com.wuest.prefab.Structures.Config.VillagerHouseConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureVillagerHouses;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import com.wuest.prefab.Tuple;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

/**
 * @author WuestMan
 */
public class GuiVillagerHouses extends GuiStructure {
    protected VillagerHouseConfiguration configuration;
    private ExtendedButton btnHouseStyle;
    private ExtendedButton btnBedColor;
    private VillagerHouseConfiguration.HouseStyle houseStyle;

    public GuiVillagerHouses() {
        super("Villager Houses");
        this.structureConfiguration = EnumStructureConfiguration.VillagerHouses;
        this.modifiedInitialXAxis = 205;
        this.modifiedInitialYAxis = 83;
    }

    @Override
    public void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Villager Houses", VillagerHouseConfiguration.class);
        this.configuration.pos = this.pos;
        this.configuration.houseFacing = Direction.NORTH;
        this.houseStyle = this.configuration.houseStyle;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        this.btnHouseStyle = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, this.houseStyle.getDisplayName(), false);

        // Create the buttons.
        this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);

        int x = grayBoxX + 130;
        int y = grayBoxY + 20;

        this.btnBedColor = this.createAndAddDyeButton(x, y, 90, 20, this.configuration.bedColor);

        this.btnBedColor.visible = this.houseStyle == VillagerHouseConfiguration.HouseStyle.LONG_HOUSE;

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);

        this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.preButtonRender(matrixStack, x, y, mouseX, mouseY, partialTicks);

        GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(this.houseStyle.getHousePicture(), matrixStack, x + 250, y, 1,
                this.houseStyle.getImageWidth(), this.houseStyle.getImageHeight(),
                this.houseStyle.getImageWidth(), this.houseStyle.getImageHeight());
    }

    @Override
    protected void postButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), x + 10, y + 10, this.textColor);

        if (this.houseStyle == VillagerHouseConfiguration.HouseStyle.LONG_HOUSE) {
            this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 130, y + 10, this.textColor);
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(AbstractButton button) {
        this.configuration.houseStyle = this.houseStyle;

        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnHouseStyle) {
            int id = this.houseStyle.getValue() + 1;
            this.houseStyle = VillagerHouseConfiguration.HouseStyle.ValueOf(id);

            GuiUtils.setButtonText(btnHouseStyle, this.houseStyle.getDisplayName());

            this.btnBedColor.visible = this.houseStyle == VillagerHouseConfiguration.HouseStyle.LONG_HOUSE;
        } else if (button == this.btnVisualize) {
            StructureVillagerHouses structure = StructureVillagerHouses.CreateInstance(this.houseStyle.getStructureLocation(), StructureVillagerHouses.class);
            StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);

            this.closeScreen();
        } else if (button == this.btnBedColor) {
            this.configuration.bedColor = DyeColor.byId(this.configuration.bedColor.getId() + 1);
            GuiUtils.setButtonText(btnBedColor, GuiLangKeys.translateDye(this.configuration.bedColor));
        }
    }
}