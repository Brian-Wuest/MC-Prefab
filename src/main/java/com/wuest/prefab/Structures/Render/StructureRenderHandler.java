package com.wuest.prefab.Structures.Render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Structures.Base.BuildBlock;
import com.wuest.prefab.Structures.Base.Structure;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.ChestRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 * @author WuestMan
 * This class was derived from Botania's MultiBlockRenderer.
 * Most changes are for extra comments for myself as well as to use my blocks class structure.
 * http://botaniamod.net/license.php
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public class StructureRenderHandler {
	private static final ChestRenderer chestRenderer = new ChestRenderer();

	// player's overlapping on structures and other things.
	public static StructureConfiguration currentConfiguration;
	public static Structure currentStructure;
	public static Direction assumedNorth;
	public static boolean rendering = false;
	public static boolean showedMessage = false;
	private static int dimension;

	/**
	 * Resets the structure to show in the world.
	 *
	 * @param structure     The structure to show in the world, pass null to clear out the client.
	 * @param assumedNorth  The assumed norther facing for this structure.
	 * @param configuration The configuration for this structure.
	 */
	public static void setStructure(Structure structure, Direction assumedNorth, StructureConfiguration configuration) {
		StructureRenderHandler.currentStructure = structure;
		StructureRenderHandler.assumedNorth = assumedNorth;
		StructureRenderHandler.currentConfiguration = configuration;
		StructureRenderHandler.showedMessage = false;

		Minecraft mc = Minecraft.getInstance();

		if (mc.world != null) {
			StructureRenderHandler.dimension = mc.world.getDimension().getType().getId();
		}
	}

	/**
	 * This is to render the currently bound structure.
	 *
	 * @param player The player to render the structure for.
	 * @param src    The ray trace for where the player is currently looking.
	 */
	public static void renderPlayerLook(PlayerEntity player, RayTraceResult src) {
		if (StructureRenderHandler.currentStructure != null
				&& StructureRenderHandler.dimension == player.world.getDimension().getType().getId()
				&& StructureRenderHandler.currentConfiguration != null
				&& CommonProxy.proxyConfiguration.serverConfiguration.enableStructurePreview) {
			GlStateManager.pushMatrix();
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableLighting();
			rendering = true;
			boolean didAny = false;

			// Use a unique shader for these blocks so the player can tell them apart from the rest of the world.
			ShaderHelper.useShader(ShaderHelper.alphaShader);

			for (BuildBlock buildBlock : StructureRenderHandler.currentStructure.getBlocks()) {
				Block foundBlock = Registry.BLOCK.getOrDefault(buildBlock.getResourceLocation());

				if (foundBlock != null) {
					// Get the unique block state for this block.
					BlockState blockState = foundBlock.getDefaultState();
					buildBlock = BuildBlock.SetBlockState(
							StructureRenderHandler.currentConfiguration,
							player.world,
							StructureRenderHandler.currentConfiguration.pos,
							StructureRenderHandler.assumedNorth,
							buildBlock,
							foundBlock,
							blockState,
							StructureRenderHandler.currentStructure);

					if (StructureRenderHandler.renderComponentInWorld(player.world, buildBlock)) {
						didAny = true;
					}
				}
			}

			// Release the shader so the whole world isn't using this shader.
			ShaderHelper.releaseShader();

			rendering = false;
			GL11.glPopAttrib();
			GlStateManager.popMatrix();

			if (!didAny) {
				// Nothing was generated, tell the user this through a chat message and re-set the structure information.
				StructureRenderHandler.setStructure(null, Direction.NORTH, null);
				player.sendMessage(
						new TranslationTextComponent(GuiLangKeys.GUI_PREVIEW_COMPLETE)
								.setStyle(new Style().setColor(TextFormatting.GREEN)));
			} else if (!StructureRenderHandler.showedMessage) {
				player.sendMessage(new TranslationTextComponent(GuiLangKeys.GUI_PREVIEW_NOTICE).setStyle(new Style().setColor(TextFormatting.GREEN)));
				StructureRenderHandler.showedMessage = true;
			}
		}
	}

	private static boolean renderComponentInWorld(World world, BuildBlock buildBlock) {
		double renderPosX = Minecraft.getInstance().getRenderManager().renderPosX;
		double renderPosY = Minecraft.getInstance().getRenderManager().renderPosY;
		double renderPosZ = Minecraft.getInstance().getRenderManager().renderPosZ;

		// In order to get the proper relative position I also need the structure's original facing.
		BlockPos pos = buildBlock.getStartingPosition().getRelativePosition(
				StructureRenderHandler.currentConfiguration.pos,
				StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection(),
				StructureRenderHandler.currentConfiguration.houseFacing);

		// Don't render this block if it's going to overlay a non-air block.
		if (!world.isAirBlock(pos)) {
			return false;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translated(-renderPosX, -renderPosY, -renderPosZ);
		// GlStateManager.disableDepthTest();
		StructureRenderHandler.doRenderComponent(buildBlock, pos);
		GlStateManager.popMatrix();

		if (buildBlock.getSubBlock() != null) {
			Block foundBlock = Registry.BLOCK.getOrDefault(buildBlock.getSubBlock().getResourceLocation());
			BlockState blockState = foundBlock.getDefaultState();

			BuildBlock subBlock = BuildBlock.SetBlockState(
					StructureRenderHandler.currentConfiguration,
					world, StructureRenderHandler.currentConfiguration.pos,
					assumedNorth,
					buildBlock.getSubBlock(),
					foundBlock,
					blockState,
					StructureRenderHandler.currentStructure);

			return StructureRenderHandler.renderComponentInWorld(world, subBlock);
		}

		return true;
	}

	private static void doRenderComponent(BuildBlock buildBlock, BlockPos pos) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		BlockState state = buildBlock.getBlockState();
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

		if (state == null) {
			GlStateManager.popMatrix();
			return;
		}

		GlStateManager.translated(pos.getX(), pos.getY(), pos.getZ() + 1);
		GlStateManager.color4f(1, 1, 1, 1);
		StructureRenderHandler.renderBlockBrightness(state, 1.0F);

		GlStateManager.color4f(1F, 1F, 1F, 1F);
		// GlStateManager.enableDepthTest();
		GlStateManager.popMatrix();
	}

	public static void renderBlockBrightness(BlockState state, float brightness) {
		BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();

		BlockRenderType blockrendertype = state.getRenderType();
		if (blockrendertype != BlockRenderType.INVISIBLE) {
			switch (blockrendertype) {
				case MODEL: {
					IBakedModel ibakedmodel = brd.getModelForState(state);

					try {
						brd.getBlockModelRenderer().renderModelBrightness(ibakedmodel, state, brightness, true);
					} catch (Exception ex) {
						// Don't do anything if a mod broke this vanilla block rendering. It just won't show up during the preview then.
					}

					break;
				}
				case ENTITYBLOCK_ANIMATED: {
					StructureRenderHandler.chestRenderer.renderChestBrightness(state.getBlock(), brightness);
				}
			}

		}
	}

}
