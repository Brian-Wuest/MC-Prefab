package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Tuple;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.structures.base.EnumStairsMaterial;
import com.wuest.prefab.structures.base.EnumStructureMaterial;
import com.wuest.prefab.structures.config.StructurePartConfiguration;
import com.wuest.prefab.structures.config.StructurePartConfiguration.EnumStyle;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructurePart;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

/**
 * This class is used as the gui for structure parts.
 *
 * @author WuestMan
 */
public class GuiStructurePart extends GuiStructure {
    protected StructurePartConfiguration configuration;
    protected GuiSlider sldrStairWidth;
    protected GuiSlider sldrStairHeight;
    protected GuiSlider sldrGeneralWidth;
    protected GuiSlider sldrGeneralHeight;
    protected GuiButtonExt btnPartStyle;
    protected GuiButtonExt btnMaterialType;
    protected GuiButtonExt btnStairsMaterialType;
    protected int modifiedInitialXAxis = 213;
    protected int modifiedInitialYAxis = 83;

    public GuiStructurePart() {
        super();
        this.structureConfiguration = EnumStructureConfiguration.Parts;
        this.modifiedInitialXAxis = 213;
        this.modifiedInitialYAxis = 83;
    }

    @Override
    protected void Initialize() {
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Parts", StructurePartConfiguration.class);
        this.configuration.pos = this.pos;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedValue.getFirst();
        int grayBoxY = adjustedValue.getSecond();

        // Create the done and cancel buttons.
        this.btnBuild = this.createAndAddButton(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);

        this.btnCancel = this.createAndAddButton(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);

        this.btnVisualize = this.createAndAddButton(3, grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);

        this.sldrStairHeight = this.createAndAddSlider(4, grayBoxX + 147, grayBoxY + 100, 90, 20, "", "", 1, 9, this.configuration.stairHeight, false, true);

        this.sldrStairWidth = this.createAndAddSlider(5, grayBoxX + 147, grayBoxY + 60, 90, 20, "", "", 1, 9, this.configuration.stairWidth, false, true);

        this.sldrGeneralHeight = this.createAndAddSlider(6, grayBoxX + 147, grayBoxY + 100, 90, 20, "", "", 3, 9, this.configuration.generalHeight, false, true);

        this.sldrGeneralWidth = this.createAndAddSlider(7, grayBoxX + 147, grayBoxY + 60, 90, 20, "", "", 3, 9, this.configuration.generalWidth, false, true);

        this.btnPartStyle = this.createAndAddButton(8, grayBoxX + 10, grayBoxY + 20, 90, 20, this.configuration.style.translateKey);

        this.btnMaterialType = this.createAndAddButton(9, grayBoxX + 147, grayBoxY + 20, 90, 20, this.configuration.partMaterial.getTranslatedName(), false);

        this.btnStairsMaterialType = this.createAndAddButton(10, grayBoxX + 147, grayBoxY + 20, 90, 20, this.configuration.stairsMaterial.getTranslatedName(), false);
    }

    @Override
    protected void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.preButtonRender(x, y, mouseX, mouseY, partialTicks);

        GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(this.configuration.style.getPictureLocation(), x + 250, y, 1,
                this.configuration.style.imageWidth, this.configuration.style.imageHeight,
                this.configuration.style.imageWidth, this.configuration.style.imageHeight);
    }

    @Override
    protected void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawString(GuiLangKeys.translateString(GuiLangKeys.STYLE), x + 10, y + 10, this.textColor);
        this.drawString(GuiLangKeys.translateString(GuiLangKeys.MATERIAL), x + 147, y + 10, this.textColor);

        if (this.configuration.style == EnumStyle.Stairs
                || this.configuration.style == EnumStyle.Roof) {
            this.sldrStairHeight.visible = this.configuration.style != EnumStyle.Roof;
            this.sldrStairWidth.visible = true;
            this.sldrGeneralHeight.visible = false;
            this.sldrGeneralWidth.visible = false;
            this.btnStairsMaterialType.visible = true;
            this.btnMaterialType.visible = false;
        } else {
            this.btnStairsMaterialType.visible = false;
            this.btnMaterialType.visible = true;
            this.sldrStairHeight.visible = false;
            this.sldrStairWidth.visible = false;
            this.sldrGeneralHeight.visible = true;
            this.sldrGeneralWidth.visible = true;
        }

        if (this.configuration.style != EnumStyle.Roof) {
            if (this.configuration.style == EnumStyle.Floor) {
                this.drawString(GuiLangKeys.translateString(GuiLangKeys.LENGTH), x + 147, y + 90, this.textColor);
            } else {
                this.drawString(GuiLangKeys.translateString(GuiLangKeys.HEIGHT), x + 147, y + 90, this.textColor);
            }
        }

        if (this.configuration.style == EnumStyle.Roof) {
            this.drawString(GuiLangKeys.translateString(GuiLangKeys.HEIGHT), x + 147, y + 50, this.textColor);
        } else {
            this.drawString(GuiLangKeys.translateString(GuiLangKeys.WIDTH), x + 147, y + 50, this.textColor);
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.configuration.houseFacing = Minecraft.getMinecraft().player.getHorizontalFacing().getOpposite();
        this.configuration.stairHeight = this.sldrStairHeight.getValueInt();
        this.configuration.stairWidth = this.sldrStairWidth.getValueInt();
        this.configuration.generalHeight = this.sldrGeneralHeight.getValueInt();
        this.configuration.generalWidth = this.sldrGeneralWidth.getValueInt();

        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnMaterialType) {
            this.configuration.partMaterial = EnumStructureMaterial.getMaterialByNumber(this.configuration.partMaterial.getNumber() + 1);
            this.btnMaterialType.displayString = this.configuration.partMaterial.getTranslatedName();
        } else if (button == this.btnStairsMaterialType) {
            this.configuration.stairsMaterial = EnumStairsMaterial.getByOrdinal(this.configuration.stairsMaterial.ordinal() + 1);
            this.btnStairsMaterialType.displayString = this.configuration.stairsMaterial.getTranslatedName();
        } else if (button == this.btnPartStyle) {
            this.configuration.style = EnumStyle.getByOrdinal(this.configuration.style.ordinal() + 1);
            this.btnPartStyle.displayString = GuiLangKeys.translateString(this.configuration.style.translateKey);
        } else if (button == this.btnVisualize) {
            StructurePart structure = new StructurePart();
            structure.getClearSpace().getShape().setDirection(EnumFacing.NORTH);
            structure.setupStructure(this.mc.world, this.configuration, this.pos);

            StructureRenderHandler.setStructure(structure, EnumFacing.SOUTH, this.configuration);
            this.closeScreen();
        }
    }
}
