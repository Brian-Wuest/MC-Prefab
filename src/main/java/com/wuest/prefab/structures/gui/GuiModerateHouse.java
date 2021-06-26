package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.config.ModConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import com.wuest.prefab.gui.controls.GuiExtendedButton;
import com.wuest.prefab.structures.config.ModerateHouseConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureModerateHouse;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;
import java.io.IOException;

/**
 * @author WuestMan
 */
public class GuiModerateHouse extends GuiStructure {
    protected ModerateHouseConfiguration configuration;
    protected GuiButtonExt btnHouseStyle;

    protected GuiCheckBox btnAddChest;
    protected GuiCheckBox btnAddChestContents;
    protected GuiCheckBox btnAddMineShaft;
    protected GuiButtonExt btnBedColor;
    protected boolean allowItemsInChestAndFurnace = true;
    protected ModConfiguration serverConfiguration;

    public GuiModerateHouse() {
        super();

        this.structureConfiguration = EnumStructureConfiguration.ModerateHouse;
    }

    @Override
    protected void Initialize() {
        this.modifiedInitialXAxis = 212;
        this.modifiedInitialYAxis = 117;

        if (!Minecraft.getMinecraft().player.isCreative()) {
            this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
        }

        this.serverConfiguration = Prefab.proxy.getServerConfiguration();
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Moderate Houses", ModerateHouseConfiguration.class);
        this.configuration.pos = this.pos;
        int color = Color.DARK_GRAY.getRGB();

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnHouseStyle = this.createAndAddButton(4, grayBoxX + 15, grayBoxY + 45, 90, 20, this.configuration.houseStyle.getDisplayName(), false);
        this.btnBedColor = this.createAndAddDyeButton(19, grayBoxX + 15, grayBoxY + 90, 90, 20, this.configuration.bedColor);
        this.btnAddChest = this.createAndAddCheckBox(20, grayBoxX + 15, grayBoxY + 125, GuiLangKeys.STARTER_HOUSE_ADD_CHEST, this.configuration.addChests);
        this.btnAddChestContents = this.createAndAddCheckBox(22, grayBoxX + 15, grayBoxY + 155, GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS, this.configuration.addMineshaft);
        this.btnAddMineShaft = this.createAndAddCheckBox(21, grayBoxX + 15, grayBoxY + 140, GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT, this.configuration.addChestContents);

        // Create the standard buttons.
        this.btnVisualize = this.createAndAddButton(4, grayBoxX + 155, grayBoxY + 75, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);

        this.btnBuild = this.createAndAddButton(1, grayBoxX + 155, grayBoxY + 120, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
        this.btnCancel = this.createAndAddButton(2, grayBoxX + 155, grayBoxY + 165, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        this.drawControlBackGround(x + 10, y + 10, 125, 190);
        this.drawControlBackGround(x + 140, y + 10, 125, 190);
        this.drawControlBackGround(x + 270, y + 10, 150, 190);

        int imageWidth = this.configuration.houseStyle.getImageWidth();
        int shownWidth = 140;
        int shownHeight = 150;

        if (imageWidth < shownWidth) {
            shownWidth = imageWidth;
        }

        GuiUtils.bindTexture(this.configuration.houseStyle.getHousePicture());
        Gui.drawScaledCustomSizeModalRect(x + 275, y + 35, 0, 0, shownWidth, shownHeight, shownWidth, shownHeight, shownWidth, shownHeight);

        this.btnAddChest.visible = this.serverConfiguration.addChests;
        this.btnAddChestContents.visible = this.allowItemsInChestAndFurnace && this.serverConfiguration.addChestContents;
        this.btnAddMineShaft.visible = this.serverConfiguration.addMineshaft;
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        // Draw the text here.
        this.drawString("Structure Options", x + 15, y + 17, this.textColor);
        this.drawString(GuiLangKeys.translateString("item.prefab:item_moderate_house.name"), x + 145, y + 17, this.textColor);

        this.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), x + 15, y + 35, this.textColor);

        this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 15, y + 80, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.configuration.addChests = this.btnAddChest.visible && this.btnAddChest.isChecked();
        this.configuration.addChestContents = this.allowItemsInChestAndFurnace && (this.btnAddChestContents.visible && this.btnAddChestContents.isChecked());
        this.configuration.addMineshaft = this.btnAddMineShaft.visible && this.btnAddMineShaft.isChecked();

        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnHouseStyle) {
            int id = this.configuration.houseStyle.getValue() + 1;
            this.configuration.houseStyle = ModerateHouseConfiguration.HouseStyle.ValueOf(id);

            this.btnHouseStyle.displayString = this.configuration.houseStyle.getDisplayName();
        } else if (button == this.btnVisualize) {
            StructureModerateHouse structure = StructureModerateHouse.CreateInstance(this.configuration.houseStyle.getStructureLocation(), StructureModerateHouse.class);
            StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
            this.closeScreen();
        } else if (button == this.btnBedColor) {
            this.configuration.bedColor = EnumDyeColor.byMetadata(this.configuration.bedColor.getMetadata() + 1);
            GuiUtils.setButtonText(btnBedColor, GuiLangKeys.translateDye(this.configuration.bedColor));
        }
    }
}
