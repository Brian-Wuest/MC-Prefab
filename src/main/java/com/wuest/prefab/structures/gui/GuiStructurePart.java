package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.ExtendedButton;
import com.wuest.prefab.structures.base.EnumStairsMaterial;
import com.wuest.prefab.structures.base.EnumStructureMaterial;
import com.wuest.prefab.structures.config.StructurePartConfiguration;
import com.wuest.prefab.structures.config.StructurePartConfiguration.EnumStyle;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructurePart;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.core.Direction;
import net.minecraftforge.fmlclient.gui.widget.Slider;

/**
 * This class is used as the gui for structure parts.
 *
 * @author WuestMan
 */
@SuppressWarnings({"ConstantConditions", "SpellCheckingInspection"})
public class GuiStructurePart extends GuiStructure {
    protected StructurePartConfiguration configuration;
    private Slider sldrStairWidth;
    private Slider sldrStairHeight;
    private Slider sldrGeneralWidth;
    private Slider sldrGeneralHeight;
    private ExtendedButton btnPartStyle;
    private ExtendedButton btnMaterialType;
    private ExtendedButton btnStairsMaterialType;

    public GuiStructurePart() {
        super("Structure Part");
        this.structureConfiguration = EnumStructureConfiguration.Parts;
    }

    @Override
    protected void Initialize() {
        this.modifiedInitialXAxis = 212;
        this.modifiedInitialYAxis = 117;
        this.shownImageHeight = 150;
        this.shownImageWidth = 268;

        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Parts", StructurePartConfiguration.class);
        this.configuration.pos = this.pos;
        this.structureImageLocation = this.configuration.style.getPictureLocation();

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedValue.getFirst();
        int grayBoxY = adjustedValue.getSecond();

        this.btnPartStyle = this.createAndAddButton(grayBoxX + 15, grayBoxY + 40, 90, 20, this.configuration.style.translateKey);
        this.btnMaterialType = this.createAndAddButton(grayBoxX + 15, grayBoxY + 75, 90, 20, this.configuration.partMaterial.getTranslatedName(), false);

        this.btnStairsMaterialType = this.createAndAddButton(grayBoxX + 15, grayBoxY + 75, 90, 20, this.configuration.stairsMaterial.getTranslatedName(), false);
        this.sldrStairHeight = this.createAndAddSlider(grayBoxX + 15, grayBoxY + 110, 90, 20, "", "", 1, 9, this.configuration.stairHeight, false, true, this::buttonClicked);
        this.sldrStairWidth = this.createAndAddSlider(grayBoxX + 15, grayBoxY + 145, 90, 20, "", "", 1, 9, this.configuration.stairWidth, false, true, this::buttonClicked);
        this.sldrGeneralHeight = this.createAndAddSlider(grayBoxX + 15, grayBoxY + 110, 90, 20, "", "", 3, 9, this.configuration.generalHeight, false, true, this::buttonClicked);
        this.sldrGeneralWidth = this.createAndAddSlider(grayBoxX + 15, grayBoxY + 145, 90, 20, "", "", 3, 9, this.configuration.generalWidth, false, true, this::buttonClicked);

        this.btnVisualize = this.createAndAddCustomButton(grayBoxX + 25, grayBoxY + 175, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
        this.btnBuild = this.createAndAddCustomButton(grayBoxX + 310, grayBoxY + 175, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
        this.btnCancel = this.createAndAddButton(grayBoxX + 150, grayBoxY + 175, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        int imagePanelUpperLeft = x + 132;
        int imagePanelWidth = 285;
        int imagePanelMiddle = imagePanelWidth / 2;

        this.renderBackground(matrixStack);

        this.drawControlLeftPanel(matrixStack, x + 10, y + 10, 125, 190);
        this.drawControlRightPanel(matrixStack, imagePanelUpperLeft, y + 10, imagePanelWidth, 190);

        int middleOfImage = this.shownImageWidth / 2;
        int imageLocation = imagePanelUpperLeft + (imagePanelMiddle - middleOfImage);

        GuiUtils.bindAndDrawScaledTexture(
                this.structureImageLocation,
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
    protected void postButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        // Draw the text here.
        this.drawString(matrixStack, GuiLangKeys.translateString("item.prefab.item_structure_part"), x + 15, y + 17, this.textColor);

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.STYLE), x + 15, y + 30, this.textColor);
        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.MATERIAL), x + 15, y + 65, this.textColor);

        if (this.configuration.style == StructurePartConfiguration.EnumStyle.Stairs
                || this.configuration.style == StructurePartConfiguration.EnumStyle.Roof) {
            this.sldrStairHeight.visible = this.configuration.style != StructurePartConfiguration.EnumStyle.Roof;
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

        if (this.configuration.style != StructurePartConfiguration.EnumStyle.Roof) {
            if (this.configuration.style == StructurePartConfiguration.EnumStyle.Floor) {
                this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.LENGTH), x + 15, y + 100, this.textColor);
            } else {
                this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.HEIGHT), x + 15, y + 100, this.textColor);
            }
        }

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.WIDTH), x + 15, y + 135, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(AbstractButton button) {
        this.configuration.houseFacing = this.minecraft.player.getDirection().getOpposite();
        this.configuration.stairHeight = this.sldrStairHeight.getValueInt();
        this.configuration.stairWidth = this.sldrStairWidth.getValueInt();
        this.configuration.generalHeight = this.sldrGeneralHeight.getValueInt();
        this.configuration.generalWidth = this.sldrGeneralWidth.getValueInt();

        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnMaterialType) {
            this.configuration.partMaterial = EnumStructureMaterial.getMaterialByNumber(this.configuration.partMaterial.getNumber() + 1);
            GuiUtils.setButtonText(btnMaterialType, this.configuration.partMaterial.getTranslatedName());
        } else if (button == this.btnStairsMaterialType) {
            this.configuration.stairsMaterial = EnumStairsMaterial.getByOrdinal(this.configuration.stairsMaterial.ordinal() + 1);
            GuiUtils.setButtonText(btnStairsMaterialType, this.configuration.stairsMaterial.getTranslatedName());
        } else if (button == this.btnPartStyle) {
            this.configuration.style = EnumStyle.getByOrdinal(this.configuration.style.ordinal() + 1);
            GuiUtils.setButtonText(btnPartStyle, GuiLangKeys.translateString(this.configuration.style.translateKey));
            this.structureImageLocation = this.configuration.style.getPictureLocation();
        } else if (button == this.btnVisualize) {
            StructurePart structure = new StructurePart();
            structure.getClearSpace().getShape().setDirection(Direction.NORTH);
            structure.setupStructure(this.minecraft.level, this.configuration, this.pos);

            this.performPreview(structure, this.configuration);
        }
    }
}
