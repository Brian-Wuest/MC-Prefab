package com.wuest.prefab.Structures.Gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wuest.prefab.Gui.GuiBase;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import javafx.util.Pair;
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
public abstract class GuiStructure extends GuiBase {
	public BlockPos pos;
	protected PlayerEntity player;
	private Direction structureFacing;

	protected GuiButtonExt btnCancel;
	protected GuiButtonExt btnBuild;
	protected GuiButtonExt btnVisualize;

	protected int textColor = Color.DARK_GRAY.getRGB();
	protected EnumStructureConfiguration structureConfiguration;

	public GuiStructure(String title) {
		super(title);
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

	public void checkVisualizationSetting() {
		if (!CommonProxy.proxyConfiguration.serverConfiguration.enableStructurePreview) {
			this.btnVisualize.visible = false;
		}
	}

	@Override
	public void render(int x, int y, float f) {
		Pair<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();

		this.preButtonRender(adjustedXYValue.getKey(), adjustedXYValue.getValue());

		this.renderButtons(x, y);

		this.postButtonRender(adjustedXYValue.getKey(), adjustedXYValue.getValue());

		if (this.btnVisualize != null)
		{
			this.checkVisualizationSetting();
		}
	}

	@Override
	protected void preButtonRender(int x, int y) {
		this.renderBackground();

		this.drawControlBackground(x, y);
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
	public static void drawModalRectWithCustomSizedTexture(int x, int y, int z, int width, int height, float textureWidth, float textureHeight) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

		float u = 0;
		float v = 0;
		float f = 1.0F / textureWidth;
		float f1 = 1.0F / textureHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexBuffer = tessellator.getBuffer();

		vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);

		// These function names used to be called "pos" and "tex" when they had proper names.
		// This probably will be reverted back when mappings are added.
		vertexBuffer.func_225582_a_(x, y + height, z).func_225583_a_(u * f, (v + height) * f1).endVertex();

		vertexBuffer.func_225582_a_(x + width, y + height, z).func_225583_a_((u + width) * f, (v + height) * f1).endVertex();

		vertexBuffer.func_225582_a_(x + width, y, z).func_225583_a_((u + width) * f, v * f1).endVertex();

		vertexBuffer.func_225582_a_(x, y, z).func_225583_a_(u * f, v * f1).endVertex();

		tessellator.draw();
	}
}
