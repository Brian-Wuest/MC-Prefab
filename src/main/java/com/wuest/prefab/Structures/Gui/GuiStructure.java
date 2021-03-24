package com.wuest.prefab.Structures.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wuest.prefab.Gui.GuiBase;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Tuple;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
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
	protected ExtendedButton btnCancel;
	protected ExtendedButton btnBuild;
	protected ExtendedButton btnVisualize;
	protected int textColor = Color.DARK_GRAY.getRGB();
	protected EnumStructureConfiguration structureConfiguration;
	private Direction structureFacing;

	public GuiStructure(String title) {
		super(title);
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
		BufferBuilder vertexBuffer = tessellator.getBuilder();

		vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);

		// This was the "pos" and "tex" method.
		vertexBuffer.vertex(x, y + height, z).uv(u * f, (v + height) * f1).endVertex();

		vertexBuffer.vertex(x + width, y + height, z).uv((u + width) * f, (v + height) * f1).endVertex();

		vertexBuffer.vertex(x + width, y, z).uv((u + width) * f, v * f1).endVertex();

		vertexBuffer.vertex(x, y, z).uv(u * f, v * f1).endVertex();

		tessellator.end();
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

		this.preButtonRender(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond());

		this.renderButtons(matrixStack, x, y);

		this.postButtonRender(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond());

		if (this.btnVisualize != null) {
			this.checkVisualizationSetting();
		}
	}

	@Override
	protected void preButtonRender(MatrixStack matrixStack, int x, int y) {
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
