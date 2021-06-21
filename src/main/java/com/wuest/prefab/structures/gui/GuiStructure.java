package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.gui.GuiBase;
import com.wuest.prefab.structures.config.StructureConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;
import java.io.IOException;

/**
 * Generic GUI for all structures.
 *
 * @author WuestMan
 */
public abstract class GuiStructure extends GuiBase {
    public BlockPos pos;
    protected EntityPlayer player;
    protected EnumFacing structureFacing;

    protected GuiButtonExt btnCancel;
    protected GuiButtonExt btnBuild;
    protected GuiButtonExt btnVisualize;

    protected EnumStructureConfiguration structureConfiguration;
    protected boolean pauseGame;

    public GuiStructure() {
        super();
    }

    /**
     * This method is used to initialize GUI specific items.
     */
    protected void Initialize() {
    }

    public void checkVisualizationSetting() {
        if (!Prefab.proxy.proxyConfiguration.enableStructurePreview) {
            this.btnVisualize.visible = false;
        }
    }

    @Override
    public void initGui() {
        this.player = this.mc.player;
        this.structureFacing = this.player.getHorizontalFacing().getOpposite();
        this.Initialize();
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();

        this.preButtonRender(adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);

        this.renderButtons(x, y);

        this.postButtonRender(adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);

        if (this.btnVisualize != null) {
            this.checkVisualizationSetting();
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    @Override
    public boolean doesGuiPauseGame() {
        return this.pauseGame;
    }

    @Override
    protected void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        this.drawControlBackground(x, y);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void performCancelOrBuildOrHouseFacing(StructureConfiguration configuration, GuiButton button) throws IOException {
        configuration.houseFacing = this.structureFacing;

        if (button == this.btnCancel) {
            this.closeScreen();
        } else if (button == this.btnBuild) {
            Prefab.network.sendToServer(new StructureTagMessage(configuration.WriteToNBTTagCompound(), this.structureConfiguration));
            this.closeScreen();
        }
    }
}
