package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.Utils;
import com.wuest.prefab.gui.GuiBase;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.proxy.CommonProxy;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.StructureConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

/**
 * Generic GUI for all structures.
 *
 * @author WuestMan
 */
public abstract class GuiStructure extends GuiBase {
    public BlockPos pos;
    protected PlayerEntity player;
    protected Button btnCancel;
    protected Button btnBuild;
    protected Button btnVisualize;
    public StructureTagMessage.EnumStructureConfiguration structureConfiguration;
    protected ResourceLocation structureImageLocation;
    private Direction structureFacing;

    public GuiStructure(String title) {
        super(title);
    }

    @Override
    public void init() {
        this.player = this.getMinecraft().player;
        this.structureFacing = this.player.getDirection().getOpposite();
        this.Initialize();
    }

    /**
     * This method is used to initialize GUI specific items.
     */
    protected void Initialize() {
        super.Initialize();
    }

    protected void InitializeStandardButtons() {
        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnVisualize = this.createAndAddCustomButton(grayBoxX + 113, grayBoxY + 167, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
        this.btnBuild = this.createAndAddCustomButton(grayBoxX + 215, grayBoxY + 167, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
        this.btnCancel = this.createAndAddButton(grayBoxX + 10, grayBoxY + 167, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    public void checkVisualizationSetting() {
        if (!CommonProxy.proxyConfiguration.serverConfiguration.enableStructurePreview) {
            this.btnVisualize.visible = false;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float f) {
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();

        this.preButtonRender(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);

        this.renderButtons(matrixStack, x, y);

        this.postButtonRender(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);

        if (this.btnVisualize != null) {
            this.checkVisualizationSetting();
        }
    }

    @Override
    protected void preButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawStandardControlBoxAndImage(matrixStack, this.structureImageLocation, x, y, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void postButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void performCancelOrBuildOrHouseFacing(StructureConfiguration configuration, AbstractButton button) {
        configuration.houseFacing = this.structureFacing;

        if (button == this.btnCancel) {
            this.closeScreen();
        } else if (button == this.btnBuild) {
            Prefab.network.sendToServer(Utils.createStructureMessage(configuration.WriteToCompoundNBT(), this.structureConfiguration));
            this.closeScreen();
        }
    }

    protected void performPreview(Structure structure, StructureConfiguration structureConfiguration) {
        StructureRenderHandler.setStructure(structure, Direction.NORTH, structureConfiguration);
        this.closeScreen();
    }
}
