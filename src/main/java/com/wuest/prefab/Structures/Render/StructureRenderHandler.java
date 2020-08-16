package com.wuest.prefab.Structures.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Structures.Base.BuildBlock;
import com.wuest.prefab.Structures.Base.Structure;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import com.wuest.prefab.Tuple;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;

import java.util.ArrayList;

/**
 * @author WuestMan
 * This class was derived from Botania's MultiBlockRenderer.
 * Most changes are for extra comments for myself as well as to use my blocks class structure.
 * http://botaniamod.net/license.php
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public class StructureRenderHandler {
	// player's overlapping on structures and other things.
	public static StructureConfiguration currentConfiguration;
	public static Structure currentStructure;
	public static Direction assumedNorth;
	public static boolean rendering = false;
	public static boolean showedMessage = false;
	private static int dimension;
	private static int overlay = OverlayTexture.getPackedUV(5, 10);

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
			// TODO: This was the "getDimension" and "getID" methods.
			StructureRenderHandler.dimension = mc.world.func_230315_m_().func_241513_m_();
		}
	}

	/**
	 * This is to render the currently bound structure.
	 *
	 * @param player The player to render the structure for.
	 * @param src    The ray trace for where the player is currently looking.
	 */
	public static void renderPlayerLook(PlayerEntity player, RayTraceResult src, MatrixStack matrixStack) {
		// TODO: This was the "getDimension" and "getID" methods.
		if (StructureRenderHandler.currentStructure != null
				&& StructureRenderHandler.dimension == player.world.func_230315_m_().func_241513_m_()
				&& StructureRenderHandler.currentConfiguration != null
				&& CommonProxy.proxyConfiguration.serverConfiguration.enableStructurePreview) {
			rendering = true;
			boolean didAny = false;

			IRenderTypeBuffer.Impl entityVertexConsumer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
			ArrayList<Tuple<BlockState, BlockPos>> entityModels = new ArrayList<>();

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

					// In order to get the proper relative position I also need the structure's original facing.
					BlockPos pos = buildBlock.getStartingPosition().getRelativePosition(
							StructureRenderHandler.currentConfiguration.pos,
							StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection(),
							StructureRenderHandler.currentConfiguration.houseFacing);

					BlockRenderType blockRenderType = blockState.getRenderType();

					if (blockRenderType == BlockRenderType.ENTITYBLOCK_ANIMATED) {
						if (ShaderHelper.hasIncompatibleMods) {
							entityModels.add(new Tuple<>(buildBlock.getBlockState(), pos));
						}

						continue;
					}

					if (StructureRenderHandler.renderComponentInWorld(player.world, buildBlock, entityVertexConsumer, matrixStack, pos, blockRenderType)) {
						didAny = true;
					}
				}
			}

			ShaderHelper.useShader(ShaderHelper.alphaShader, shader -> {
				// getUniformLocation
				int alpha = GlStateManager.getUniformLocation(shader, "alpha");
				ShaderHelper.FLOAT_BUF.position(0);
				ShaderHelper.FLOAT_BUF.put(0, 0.4F);

				// uniform1
				GlStateManager.uniform1f(alpha, ShaderHelper.FLOAT_BUF);
			});

			// Draw function.
			// TODO: This used to be "getTranslucentBlockType"
			entityVertexConsumer.finish(Atlases.func_239280_i_());

			ShaderHelper.releaseShader();

			for (Tuple<BlockState, BlockPos> pair : entityModels) {
				BlockPos blockPos = pair.getSecond();
				StructureRenderHandler.renderBlock(matrixStack, new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), pair.getFirst(), entityVertexConsumer, BlockRenderType.ENTITYBLOCK_ANIMATED);
			}

			if (!didAny) {
				// Nothing was generated, tell the user this through a chat message and re-set the structure information.
				StructureRenderHandler.setStructure(null, Direction.NORTH, null);

				TranslationTextComponent message = new TranslationTextComponent(GuiLangKeys.GUI_PREVIEW_COMPLETE);
				message.setStyle(Style.EMPTY.setFormatting(TextFormatting.GREEN));
				player.sendMessage(message, player.getUniqueID());

			} else if (!StructureRenderHandler.showedMessage) {
				TranslationTextComponent message = new TranslationTextComponent(GuiLangKeys.GUI_PREVIEW_NOTICE);
				message.setStyle(Style.EMPTY.setFormatting(TextFormatting.GREEN));

				player.sendMessage(message, player.getUniqueID());
				StructureRenderHandler.showedMessage = true;
			}
		}
	}

	private static boolean renderComponentInWorld(World world, BuildBlock buildBlock, IRenderTypeBuffer entityVertexConsumer, MatrixStack matrixStack, BlockPos pos, BlockRenderType blockRenderType) {
		// Don't render this block if it's going to overlay a non-air/water block.
		BlockState targetBlock = world.getBlockState(pos);
		if (targetBlock.getMaterial() != Material.AIR && targetBlock.getMaterial() != Material.WATER) {
			return false;
		}

		StructureRenderHandler.doRenderComponent(buildBlock, pos, entityVertexConsumer, matrixStack, blockRenderType);

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

			BlockPos subBlockPos = subBlock.getStartingPosition().getRelativePosition(
					StructureRenderHandler.currentConfiguration.pos,
					StructureRenderHandler.currentStructure.getClearSpace().getShape().getDirection(),
					StructureRenderHandler.currentConfiguration.houseFacing);

			BlockRenderType subBlockRenderType = subBlock.getBlockState().getRenderType();

			return StructureRenderHandler.renderComponentInWorld(world, subBlock, entityVertexConsumer, matrixStack, subBlockPos, subBlockRenderType);
		}

		return true;
	}

	private static void doRenderComponent(BuildBlock buildBlock, BlockPos pos, IRenderTypeBuffer entityVertexConsumer, MatrixStack matrixStack, BlockRenderType blockRenderType) {
		BlockState state = buildBlock.getBlockState();
		StructureRenderHandler.renderBlock(matrixStack, new Vector3d(pos.getX(), pos.getY(), pos.getZ()), state, entityVertexConsumer, blockRenderType);
	}

	private static void renderBlock(MatrixStack matrixStack, Vector3d pos, BlockState state, IRenderTypeBuffer entityVertexConsumer, BlockRenderType blockRenderType) {
		Minecraft minecraft = Minecraft.getInstance();
		Vector3d projectedView = minecraft.getRenderManager().info.getProjectedView();
		double renderPosX = projectedView.getX();
		double renderPosY = projectedView.getY();
		double renderPosZ = projectedView.getZ();

		// push
		matrixStack.push();

		// Translate function.
		matrixStack.translate(-renderPosX, -renderPosY, -renderPosZ);

		BlockRendererDispatcher renderer = minecraft.getBlockRendererDispatcher();

		// Translate.
		matrixStack.translate(pos.getX(), pos.getY(), pos.getZ());

		ClientWorld world = Minecraft.getInstance().world;
		IModelData model = renderer.getModelForState(state).getModelData(world, new BlockPos(pos), state, ModelDataManager.getModelData(world, new BlockPos(pos)));
		IBakedModel bakedModel = renderer.getModelForState(state);

		if (blockRenderType == BlockRenderType.MODEL) {
			// getColor function.
			int color = minecraft.getBlockColors().getColor(state, null, null, 0);
			float r = (float) (color >> 16 & 255) / 255.0F;
			float g = (float) (color >> 8 & 255) / 255.0F;
			float b = (float) (color & 255) / 255.0F;

			renderer.getBlockModelRenderer().renderModelBrightnessColor(
					matrixStack.getLast(),
					// TODO: This used to be "getTranslucentBlockType"
					entityVertexConsumer.getBuffer(Atlases.func_239280_i_()),
					state,
					bakedModel,
					r,
					g,
					b,
					0xF000F0,
					OverlayTexture.NO_OVERLAY);
		} else if (blockRenderType == BlockRenderType.ENTITYBLOCK_ANIMATED) {
			renderer.renderBlock(
					state,
					matrixStack,
					entityVertexConsumer,
					0xF000F0,
					StructureRenderHandler.overlay,
					model);
		}

		// pop
		matrixStack.pop();
	}

	private static void renderModel(MatrixStack matrixStack, Vector3d pos, BlockState state, IRenderTypeBuffer entityVertexConsumer) {

	}


}
