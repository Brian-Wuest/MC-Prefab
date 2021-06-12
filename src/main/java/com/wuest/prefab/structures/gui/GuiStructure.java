package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.gui.GuiBase;
import com.wuest.prefab.proxy.CommonProxy;
import com.wuest.prefab.structures.config.StructureConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.awt.*;

/**
 * Generic GUI for all structures.
 *
 * @author WuestMan
 */
public abstract class GuiStructure extends GuiBase {
    public BlockPos pos;
    protected PlayerEntity player;
    protected ExtendedButton btnCancel;
    protected ExtendedButton btnBuild;
    protected ExtendedButton btnVisualize;
    protected int textColor = Color.DARK_GRAY.getRGB();
    protected EnumStructureConfiguration structureConfiguration;
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
        this.renderBackground(matrixStack);

        this.drawControlBackground(matrixStack, x, y);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void performCancelOrBuildOrHouseFacing(StructureConfiguration configuration, AbstractButton button) {
        configuration.houseFacing = this.structureFacing;

        if (button == this.btnCancel) {
            this.closeScreen();
        } else if (button == this.btnBuild) {
            Prefab.network.sendToServer(new StructureTagMessage(configuration.WriteToCompoundNBT(), this.structureConfiguration));
            this.closeScreen();
        }
    }
}
