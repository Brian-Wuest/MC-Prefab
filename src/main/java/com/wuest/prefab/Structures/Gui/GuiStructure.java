package com.wuest.prefab.Structures.Gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Generic GUI for all structures.
 *
 * @author WuestMan
 */
public abstract class GuiStructure extends Screen {
    public final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
    public BlockPos pos;
    protected PlayerEntity player;
    protected Direction structureFacing;

    protected GuiButtonExt btnCancel;
    protected GuiButtonExt btnBuild;
    protected GuiButtonExt btnVisualize;

    protected int textColor = Color.DARK_GRAY.getRGB();
    protected EnumStructureConfiguration structureConfiguration;
    protected boolean pauseGame;

    public GuiStructure(String title) {
        super(new StringTextComponent(title));
        this.pauseGame = true;
    }

    @Override
    public void init() {
        this.player = this.getMinecraft().player;
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
    public boolean isPauseScreen() {
        return this.pauseGame;
    }

    public void checkVisualizationSetting() {
        if (!CommonProxy.proxyConfiguration.serverConfiguration.enableStructurePreview) {
            this.btnVisualize.visible = false;
        }
    }

    /**
     * Creates a {@link GuiButtonExt} using the button clicked event as the handler. Then adds it to the buttons list and returns the created object.
     *
     * @param x      The x-axis position.
     * @param y      The y-axis position.
     * @param width  The width of the button.
     * @param height The height of the button.
     * @param text   The text of the button.
     * @return A new button.
     */
    public GuiButtonExt createAndAddButton(int x, int y, int width, int height, String text) {
        GuiButtonExt returnValue = new GuiButtonExt(x, y, width, height, text, this::buttonClicked);

        this.addButton(returnValue);

        return returnValue;
    }

    public abstract void buttonClicked(Button button);

    protected void drawControlBackgroundAndButtonsAndLabels(int grayBoxX, int grayBoxY, int mouseX, int mouseY) {
        this.getMinecraft().getTextureManager().bindTexture(this.backgroundTextures);
        this.blit(grayBoxX, grayBoxY, 0, 0, 256, 256);

        for (int i = 0; i < this.buttons.size(); ++i) {
            Button currentButton = (Button) this.buttons.get(i);

            if (currentButton != null && currentButton.visible) {
                currentButton.renderButton(mouseX, mouseY, this.getMinecraft().getRenderPartialTicks());
            }
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void performCancelOrBuildOrHouseFacing(StructureConfiguration configuration, Button button) {
        configuration.houseFacing = this.structureFacing;

        if (button == this.btnCancel) {
            this.getMinecraft().displayGuiScreen(null);
        } else if (button == this.btnBuild) {
            Prefab.network.sendToServer(new StructureTagMessage(configuration.WriteToCompoundNBT(), this.structureConfiguration));
            this.getMinecraft().displayGuiScreen(null);
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
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

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
