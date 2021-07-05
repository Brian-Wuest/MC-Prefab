package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.structures.config.VillagerHouseConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureVillagerHouses;
import com.wuest.prefab.structures.render.StructureRenderHandler;
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
    }

    @Override
    public void Initialize() {
        this.modifiedInitialXAxis = 212;
        this.modifiedInitialYAxis = 117;
        this.shownImageHeight = 150;
        this.shownImageWidth = 268;
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Villager Houses", VillagerHouseConfiguration.class);
        this.configuration.pos = this.pos;
        this.configuration.houseFacing = Direction.NORTH;
        this.houseStyle = this.configuration.houseStyle;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnHouseStyle = this.createAndAddButton(grayBoxX + 15, grayBoxY + 45, 90, 20, this.houseStyle.getDisplayName(), false);
        this.btnBedColor = this.createAndAddDyeButton(grayBoxX + 15, grayBoxY + 90, 90, 20, this.configuration.bedColor);
        this.btnBedColor.visible = this.houseStyle == VillagerHouseConfiguration.HouseStyle.LONG_HOUSE;

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
                this.houseStyle.getHousePicture(),
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
        this.drawString(matrixStack, GuiLangKeys.translateString("item.prefab.item_villager_houses"), x + 15, y + 17, this.textColor);

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), x + 15, y + 35, this.textColor);

        if (this.houseStyle == VillagerHouseConfiguration.HouseStyle.LONG_HOUSE) {
            this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 15, y + 80, this.textColor);
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
            this.performPreview(structure, this.configuration);
        } else if (button == this.btnBedColor) {
            this.configuration.bedColor = DyeColor.byId(this.configuration.bedColor.getId() + 1);
            GuiUtils.setButtonText(btnBedColor, GuiLangKeys.translateDye(this.configuration.bedColor));
        }
    }
}