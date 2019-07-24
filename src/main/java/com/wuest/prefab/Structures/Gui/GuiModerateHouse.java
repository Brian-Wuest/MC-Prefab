package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Config.ServerModConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.Controls.GuiCheckBox;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiTabScreen;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Structures.Config.ModerateHouseConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureModerateHouse;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;

/**
 * @author WuestMan
 */
public class GuiModerateHouse extends GuiStructure {
    protected ModerateHouseConfiguration configuration;
    protected GuiButtonExt btnHouseStyle;

    protected GuiCheckBox btnAddChest;
    protected GuiCheckBox btnAddChestContents;
    protected GuiCheckBox btnAddMineShaft;
    protected boolean allowItemsInChestAndFurnace = true;
    protected ServerModConfiguration serverConfiguration;

    public GuiModerateHouse() {
        super("Moderate House");

        this.structureConfiguration = EnumStructureConfiguration.ModerateHouse;

        if (!Minecraft.getInstance().player.isCreative()) {
            this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
        }
    }

    @Override
    protected void Initialize() {
        this.serverConfiguration = Prefab.proxy.getServerConfiguration();
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Moderate Houses", ModerateHouseConfiguration.class);
        this.configuration.pos = this.pos;
        int color = Color.DARK_GRAY.getRGB();

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = this.getCenteredXAxis() - 212;
        int grayBoxY = this.getCenteredYAxis() - 83;

        this.btnHouseStyle = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, this.configuration.houseStyle.getDisplayName());

        // Create the buttons.
        this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

        this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));

        int x = grayBoxX + 130;
        int y = grayBoxY + 10;

        this.btnAddChest = new GuiCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST), this.configuration.addChests);
        this.btnAddChest.setStringColor(color);
        this.btnAddChest.setWithShadow(false);
        this.addButton(this.btnAddChest);
        y += 15;

        this.btnAddMineShaft = new GuiCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT), this.configuration.addChestContents);
        this.btnAddMineShaft.setStringColor(color);
        this.btnAddMineShaft.setWithShadow(false);
        this.addButton(this.btnAddMineShaft);
        y += 15;

        this.btnAddChestContents = new GuiCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS), this.configuration.addMineshaft);
        this.btnAddChestContents.setStringColor(color);
        this.btnAddChestContents.setWithShadow(false);
        this.addButton(this.btnAddChestContents);
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
    public void render(int x, int y, float f) {
        int grayBoxX = this.getCenteredXAxis() - 212;
        int grayBoxY = this.getCenteredYAxis() - 83;

        this.renderBackground();

        // Draw the control background.
        this.minecraft.getTextureManager().bindTexture(this.configuration.houseStyle.getHousePicture());
        GuiTabScreen.drawModalRectWithCustomSizedTexture(grayBoxX + 249, grayBoxY, 1,
                this.configuration.houseStyle.getImageWidth(), this.configuration.houseStyle.getImageHeight(),
                this.configuration.houseStyle.getImageWidth(), this.configuration.houseStyle.getImageHeight());

        this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

        this.btnAddChest.visible = this.serverConfiguration.addChests;
        this.btnAddChestContents.visible = this.allowItemsInChestAndFurnace && this.serverConfiguration.addChestContents;
        this.btnAddMineShaft.visible = this.serverConfiguration.addMineshaft;

        // Draw the text here.
        this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), grayBoxX + 10, grayBoxY + 10, this.textColor);

        this.checkVisualizationSetting();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(Button button) {
        this.configuration.addChests = this.btnAddChest.visible && this.btnAddChest.isChecked();
        this.configuration.addChestContents = this.allowItemsInChestAndFurnace && (this.btnAddChestContents.visible && this.btnAddChestContents.isChecked());
        this.configuration.addMineshaft = this.btnAddMineShaft.visible && this.btnAddMineShaft.isChecked();

        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnHouseStyle) {
            int id = this.configuration.houseStyle.getValue() + 1;
            this.configuration.houseStyle = ModerateHouseConfiguration.HouseStyle.ValueOf(id);

            this.btnHouseStyle.setMessage(this.configuration.houseStyle.getDisplayName());
        } else if (button == this.btnVisualize) {
            StructureModerateHouse structure = StructureModerateHouse.CreateInstance(this.configuration.houseStyle.getStructureLocation(), StructureModerateHouse.class);
            StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
            this.minecraft.displayGuiScreen(null);
        }
    }
}
