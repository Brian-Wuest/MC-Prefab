package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.structures.config.BasicStructureConfiguration;
import com.wuest.prefab.structures.items.ItemBasicStructure;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureBasic;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;

/**
 * This class is used as the gui for all basic structures.
 *
 * @author WuestMan
 */
public class GuiBasicStructure extends GuiStructure {
    protected BasicStructureConfiguration configuration;
    protected boolean includePicture = true;
    protected int modifiedInitialXAxis = 213;
    protected int modifiedINitialYAxis = 83;

    public GuiBasicStructure(int x, int y, int z) {
        super(x, y, z, true);
        this.structureConfiguration = EnumStructureConfiguration.Basic;
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
    public void drawScreen(int x, int y, float f) {
        int grayBoxX = this.getCenteredXAxis() - this.modifiedInitialXAxis;
        int grayBoxY = this.getCenteredYAxis() - this.modifiedINitialYAxis;

        this.drawDefaultBackground();

        if (this.includePicture) {
            // Draw the control background.
            this.mc.getTextureManager().bindTexture(this.configuration.basicStructureName.getTopDownPictureLocation());

            this.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1,
                    this.configuration.basicStructureName.getImageWidth(), this.configuration.basicStructureName.getImageHeight(),
                    this.configuration.basicStructureName.getImageWidth(), this.configuration.basicStructureName.getImageHeight());
        }

        this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

        // Draw the text here.
        this.mc.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), grayBoxX + 147, grayBoxY + 10, 95, this.textColor);

        if (!Prefab.proxy.proxyConfiguration.enableStructurePreview) {
            this.btnVisualize.enabled = false;
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnVisualize) {
            StructureBasic structure = StructureBasic.CreateInstance(this.configuration.basicStructureName.getAssetLocation(), StructureBasic.class);
            StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void Initialize() {
        // Get the structure configuration for this itemstack.
        ItemStack stack = ItemBasicStructure.getBasicStructureItemInHand(this.player);

        if (stack != null) {
            ItemBasicStructure item = (ItemBasicStructure) stack.getItem();
            this.configuration = ClientEventHandler.playerConfig.getClientConfig(item.structureType.getName(), BasicStructureConfiguration.class);
            this.configuration.basicStructureName = item.structureType;
            //this.configuration.chosenOption = item.structureType.getBaseOption();
            this.includePicture = this.doesPictureExist();
            //this.availableOptions = this.configuration.chosenOption.getSpecificOptions();
        }

        this.configuration.pos = this.pos;

        if (!this.includePicture) {
            this.modifiedInitialXAxis = 125;
        }

        // Get the upper left hand corner of the GUI box.
        int grayBoxX = this.getCenteredXAxis() - this.modifiedInitialXAxis;
        int grayBoxY = this.getCenteredYAxis() - this.modifiedINitialYAxis;

        // Create the buttons.
        this.btnVisualize = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));
        this.buttonList.add(this.btnVisualize);

        // Create the done and cancel buttons.
        this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
        this.buttonList.add(this.btnBuild);

        this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
        this.buttonList.add(this.btnCancel);
    }

    /**
     * Determines if the picture for this screen exists in the resources.
     *
     * @return A value indicating whether the picture exists.
     */
    protected boolean doesPictureExist() {
        try {
            this.mc.getResourceManager().getResource(this.configuration.basicStructureName.getTopDownPictureLocation());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
