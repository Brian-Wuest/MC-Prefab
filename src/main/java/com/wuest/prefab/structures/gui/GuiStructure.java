package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.config.StructureConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

/**
 * Generic GUI for all structures.
 *
 * @author WuestMan
 */
public class GuiStructure extends GuiScreen {
    public final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
    public BlockPos pos;
    protected EntityPlayer player;
    protected EnumFacing structureFacing;

    protected GuiButtonExt btnCancel;
    protected GuiButtonExt btnBuild;
    protected GuiButtonExt btnVisualize;

    protected int textColor = Color.DARK_GRAY.getRGB();
    protected EnumStructureConfiguration structureConfiguration;
    protected boolean pauseGame;

    public GuiStructure(int x, int y, int z, boolean pauseGame) {
        this.pos = new BlockPos(x, y, z);
        this.pauseGame = pauseGame;
    }

    @Override
    public void initGui() {
        this.player = this.mc.player;
        this.structureFacing = this.player.getHorizontalFacing().getOpposite();
        this.Initialize();
    }

    /**
     * This method is used to initialize GUI specific items.
     */
    protected void Initialize() {
    }

    protected int getCenteredXAxis() {
        return this.width / 2;
    }

    protected int getCenteredYAxis() {
        return this.height / 2;
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    @Override
    public boolean doesGuiPauseGame() {
        return this.pauseGame;
    }

    protected void drawControlBackgroundAndButtonsAndLabels(int grayBoxX, int grayBoxY, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(this.backgroundTextures);
        this.drawTexturedModalRect(grayBoxX, grayBoxY, 0, 0, 256, 256);

        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton currentButton = this.buttonList.get(i);

            if (currentButton != null && currentButton.visible) {
                currentButton.drawButton(this.mc, mouseX, mouseY, this.mc.getRenderPartialTicks());
            }
        }

        for (int j = 0; j < this.labelList.size(); ++j) {
            GuiLabel currentLabel = this.labelList.get(j);

            if (currentLabel != null) {
                currentLabel.drawLabel(this.mc, mouseX, mouseY);
            }
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void performCancelOrBuildOrHouseFacing(StructureConfiguration configuration, GuiButton button) throws IOException {
        configuration.houseFacing = this.structureFacing;

        if (button == this.btnCancel) {
            this.mc.displayGuiScreen(null);
        } else if (button == this.btnBuild) {
            Prefab.network.sendToServer(new StructureTagMessage(configuration.WriteToNBTTagCompound(), this.structureConfiguration));
            this.mc.displayGuiScreen(null);
        }
    }

    /**
     * Draws a textured rectangle Args: x, y, z, width, height, textureWidth, textureHeight
     *
     * @param x             The X-Axis screen coordinate.
     * @param y             The Y-Axis screen coordinate.
     * @param z             The Z-Axis screen coordinate.
     * @param width         The width of the rectangle.
     * @param height        The height of the rectangle.
     * @param textureWidth  The width of the texture.
     * @param textureHeight The height of the texture.
     */
    public void drawModalRectWithCustomSizedTexture(int x, int y, int z, int width, int height, float textureWidth, float textureHeight) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        float u = 0;
        float v = 0;
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();

        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);

        vertexbuffer.pos(x, y + height, z)
                .tex(u * f, (v + height) * f1).endVertex();

        vertexbuffer.pos(x + width, y + height, z)
                .tex((u + width) * f, (v + height) * f1).endVertex();

        vertexbuffer.pos(x + width, y, z)
                .tex((u + width) * f, v * f1).endVertex();

        vertexbuffer.pos(x, y, z)
                .tex(u * f, v * f1).endVertex();

        tessellator.draw();
    }
}
