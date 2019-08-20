package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Config.FishPondConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureFishPond;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

/**
 * @author WuestMan
 */
public class GuiFishPond extends GuiStructure {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/fish_pond_top_down.png");
    protected FishPondConfiguration configuration;

    public GuiFishPond() {
        super("Fish Pond");
        this.structureConfiguration = EnumStructureConfiguration.FishPond;
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
    public void render(int x, int y, float f) {
        int grayBoxX = this.getCenteredXAxis() - 188;
        int grayBoxY = this.getCenteredYAxis() - 83;

        this.renderBackground();

        // Draw the control background.
        // Create class to de-compress image from resource path.
        // This class should inherit from "SimpleTexture" and override it's loadTexture method
        // After the buffered image has been loaded, the GlStateManager.bindTexture class should be called.
        // Will probably want to keep the buffered image around in a class so the resources aren't constantly being de-compressed as this happens on every tick.
        //BufferedImage image = ZipUtil.decompressImageResource(structureTopDown.getResourcePath());
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(structureTopDown);

        GuiFishPond.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 151, 149, 151, 149);

        this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

        // Draw the text here.
        this.minecraft.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), grayBoxX + 147, grayBoxY + 10, 95, this.textColor);

        this.checkVisualizationSetting();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(Button button) {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnVisualize) {
            StructureFishPond structure = StructureFishPond.CreateInstance(StructureFishPond.ASSETLOCATION, StructureFishPond.class);
            StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
            assert this.minecraft != null;
            this.minecraft.displayGuiScreen(null);
        }
    }

    @Override
    protected void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Fish Pond", FishPondConfiguration.class);
        this.configuration.pos = this.pos;

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = (this.width / 2) - 188;
        int grayBoxY = (this.height / 2) - 83;

        // Create the buttons.
        this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

        this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
    }
}
