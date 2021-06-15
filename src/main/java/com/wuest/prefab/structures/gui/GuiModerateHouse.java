package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.config.ModConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import com.wuest.prefab.structures.config.ModerateHouseConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureModerateHouse;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
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
        this.modifiedInitialXAxis = 212;
        this.modifiedInitialYAxis = 83;
    }

    @Override
    protected void Initialize() {
        if (!Minecraft.getMinecraft().player.isCreative()) {
            this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
        }

        this.serverConfiguration = Prefab.proxy.getServerConfiguration();
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Moderate Houses", ModerateHouseConfiguration.class);
        this.configuration.pos = this.pos;
        int color = Color.DARK_GRAY.getRGB();

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = this.getCenteredXAxis() - 212;
        int grayBoxY = this.getCenteredYAxis() - 83;

        this.btnHouseStyle = this.createAndAddButton(4, grayBoxX + 10, grayBoxY + 20, 90, 20, this.configuration.houseStyle.getDisplayName(), false);

        // Create the buttons.
        this.btnVisualize = this.createAndAddButton(4, grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);

        this.btnCancel = this.createAndAddButton(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);

        int x = grayBoxX + 130;
        int y = grayBoxY + 20;

        this.btnBedColor = this.createAndAddDyeButton(19, x, y, 90, 20, this.configuration.bedColor);

        y += 30;

        this.btnAddChest = this.createAndAddCheckBox(6, x, y, GuiLangKeys.STARTER_HOUSE_ADD_CHEST, this.configuration.addChests);
        y += 15;

        this.btnAddMineShaft = this.createAndAddCheckBox(7, x, y, GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT, this.configuration.addChestContents);
        y += 15;

        this.btnAddChestContents = this.createAndAddCheckBox(8, x, y, GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS, this.configuration.addMineshaft);
    }

    @Override
    protected void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.preButtonRender(x, y, mouseX, mouseY, partialTicks);

        GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(this.configuration.houseStyle.getHousePicture(), x + 249, y, 1,
                this.configuration.houseStyle.getImageWidth(), this.configuration.houseStyle.getImageHeight(),
                this.configuration.houseStyle.getImageWidth(), this.configuration.houseStyle.getImageHeight());
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.btnAddChest.visible = this.serverConfiguration.addChests;
        this.btnAddChestContents.visible = this.allowItemsInChestAndFurnace && this.serverConfiguration.addChestContents;
        this.btnAddMineShaft.visible = this.serverConfiguration.addMineshaft;

        // Draw the text here.
        this.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), x + 10, y + 10, this.textColor);

        this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 130, y + 10, this.textColor);
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
