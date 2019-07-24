package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiTabScreen;
import com.wuest.prefab.Structures.Config.VillagerHouseConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureVillagerHouses;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * @author WuestMan
 */
public class GuiVillaerHouses extends GuiStructure {
    private static final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
    protected GuiButtonExt btnHouseStyle;
    protected VillagerHouseConfiguration configuration;
    protected VillagerHouseConfiguration.HouseStyle houseStyle;

    public GuiVillaerHouses() {
        super("Villager Houses");
        this.structureConfiguration = EnumStructureConfiguration.VillagerHouses;
    }

    @Override
    public void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Villager Houses", VillagerHouseConfiguration.class);
        this.configuration.pos = this.pos;
        this.configuration.houseFacing = Direction.NORTH;
        this.houseStyle = this.configuration.houseStyle;

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = this.getCenteredXAxis() - 205;
        int grayBoxY = this.getCenteredYAxis() - 83;

        this.btnHouseStyle = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, this.houseStyle.getDisplayName());

        // Create the buttons.
        this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

        this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
    public void render(int x, int y, float f) {
        int grayBoxX = this.getCenteredXAxis() - 205;
        int grayBoxY = this.getCenteredYAxis() - 83;

        this.renderBackground();

        // Draw the control background.
        this.minecraft.getTextureManager().bindTexture(this.houseStyle.getHousePicture());
        GuiTabScreen.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1,
                this.houseStyle.getImageWidth(), this.houseStyle.getImageHeight(),
                this.houseStyle.getImageWidth(), this.houseStyle.getImageHeight());

        this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

        // Draw the text here.
        this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), grayBoxX + 10, grayBoxY + 10, this.textColor);

        this.checkVisualizationSetting();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(Button button) {
        this.configuration.houseStyle = this.houseStyle;

        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnHouseStyle) {
            int id = this.houseStyle.getValue() + 1;
            this.houseStyle = VillagerHouseConfiguration.HouseStyle.ValueOf(id);

            this.btnHouseStyle.setMessage(this.houseStyle.getDisplayName());
        } else if (button == this.btnVisualize) {
            StructureVillagerHouses structure = StructureVillagerHouses.CreateInstance(this.houseStyle.getStructureLocation(), StructureVillagerHouses.class);
            StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
            this.minecraft.displayGuiScreen(null);
        }
    }
}