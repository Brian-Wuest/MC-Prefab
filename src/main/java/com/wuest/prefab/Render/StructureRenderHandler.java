package com.wuest.prefab.Render;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Throwables;
import com.wuest.prefab.Config.StructureConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.StructureGen.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * @author WuestMan
 * This class was derived from Botania's MultiBlockRenderer.
 * Most changes are for extra comments for myself as well as to use my blocks class structure.
 * http://botaniamod.net/license.php
 *
 */
public class StructureRenderHandler
{
	// All of this is on client side so we don't have to worry about multiple
	// player's overlapping on structures and other things.
	public static StructureConfiguration currentConfiguration;
	public static Structure currentStructure;
	public static EnumFacing assumedNorth;
	public static boolean rendering = false;
	public static boolean showedMessage = false;
	private static final ChestRenderer chestRenderer = new ChestRenderer();
	private static int dimension;

	private static final String[] RENDERPOSX =
	{ "renderPosX", "field_78725_b", "o" };
	
	private static final String[] RENDERPOSY =
	{ "renderPosY", "field_78726_c", "p" };
	
	private static final String[] RENDERPOSZ =
	{ "renderPosZ", "field_78723_d", "q" };

	// RenderManager
	private static final MethodHandle renderPosX_getter, renderPosY_getter, renderPosZ_getter;

	static
	{
		try
		{
			// This is to make the renderPosX, renderPosY and renderPosZ fields available for usage.
			Field f = ReflectionHelper.findField(RenderManager.class, StructureRenderHandler.RENDERPOSX);
			f.setAccessible(true);
			renderPosX_getter = MethodHandles.publicLookup().unreflectGetter(f);

			f = ReflectionHelper.findField(RenderManager.class, StructureRenderHandler.RENDERPOSY);
			f.setAccessible(true);
			renderPosY_getter = MethodHandles.publicLookup().unreflectGetter(f);

			f = ReflectionHelper.findField(RenderManager.class, StructureRenderHandler.RENDERPOSZ);
			f.setAccessible(true);
			renderPosZ_getter = MethodHandles.publicLookup().unreflectGetter(f);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Resets the structure to show in the world.
	 * @param structure The structure to show in the world, pass null to clear out the client.
	 * @param assumedNorth The assumed norther facing for this structure.
	 * @param configuration The configuration for this structure.
	 */
	public static void setStructure(Structure structure, EnumFacing assumedNorth, StructureConfiguration configuration)
	{
		StructureRenderHandler.currentStructure = structure;
		StructureRenderHandler.assumedNorth = assumedNorth;
		StructureRenderHandler.currentConfiguration = configuration;
		StructureRenderHandler.showedMessage = false;

		Minecraft mc = Minecraft.getMinecraft();

		if (mc.theWorld != null)
		{
			StructureRenderHandler.dimension = mc.theWorld.provider.getDimension();
		}
	}

	/**
	 * This is to render the currently bound structure.
	 * @param player The player to render the structure for.
	 * @param src The ray trace for where the player is currently looking.
	 */
	public static void renderPlayerLook(EntityPlayer player, RayTraceResult src)
	{
		if (StructureRenderHandler.currentStructure != null && StructureRenderHandler.dimension == player.worldObj.provider.getDimension() && StructureRenderHandler.currentConfiguration != null)
		{
			GlStateManager.pushMatrix();
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableLighting();
			rendering = true;
			boolean didAny = false;

			// Use a unique shader for these blocks so the player can tell them apart from the rest of the world. 
			ShaderHelper.useShader(ShaderHelper.alphaShader);
			
			for (BuildBlock buildBlock : StructureRenderHandler.currentStructure.getBlocks())
			{
				Block foundBlock = Block.REGISTRY.getObject(buildBlock.getResourceLocation());
				
				if (foundBlock != null)
				{
					// Get the unique block state for this block.
					IBlockState blockState = foundBlock.getDefaultState();
					buildBlock = BuildBlock.SetBlockState(StructureRenderHandler.currentConfiguration, player.worldObj, StructureRenderHandler.currentConfiguration.pos, StructureRenderHandler.assumedNorth, buildBlock, foundBlock, blockState);
					
					if (StructureRenderHandler.renderComponentInWorld(player.worldObj, buildBlock))
					{
						didAny = true;
					}
				}
			}
			
			// Release the shader so the whole world isn't using this shader.
			ShaderHelper.releaseShader();

			rendering = false;
			GL11.glPopAttrib();
			GlStateManager.popMatrix();

			if (!didAny)
			{
				// Nothing was generated, tell the user this through a chat message and re-set the structure information.
				StructureRenderHandler.setStructure(null, EnumFacing.NORTH, null);
				player.addChatComponentMessage(
						new TextComponentTranslation(GuiLangKeys.GUI_PREVIEW_COMPLETE)
						.setStyle(new Style().setColor(TextFormatting.GREEN)));
			}
			else if (!StructureRenderHandler.showedMessage)
			{
				player.addChatComponentMessage(new TextComponentTranslation(GuiLangKeys.GUI_PREVIEW_NOTICE).setStyle(new Style().setColor(TextFormatting.GREEN)));
				StructureRenderHandler.showedMessage = true;
			}
		}
	}
	
	private static boolean renderComponentInWorld(World world, BuildBlock buildBlock)
	{
		double renderPosX, renderPosY, renderPosZ;

		try
		{
			renderPosX = (double) StructureRenderHandler.renderPosX_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
			renderPosY = (double) StructureRenderHandler.renderPosY_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
			renderPosZ = (double) StructureRenderHandler.renderPosZ_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
		}
		catch (Throwable t)
		{
			return true;
		}
		
		BlockPos pos = buildBlock.getStartingPosition().getRelativePosition(StructureRenderHandler.currentConfiguration.pos, StructureRenderHandler.currentConfiguration.houseFacing);

		// Don't render this block if it's going to overlay a non-air block.
		if (!world.isAirBlock(pos))
		{
			return false;
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(-renderPosX, -renderPosY, -renderPosZ);
		GlStateManager.disableDepth();
		StructureRenderHandler.doRenderComponent(buildBlock, pos);
		GlStateManager.popMatrix();
		
		if (buildBlock.getSubBlock() != null)
		{
			Block foundBlock = Block.REGISTRY.getObject(buildBlock.getSubBlock().getResourceLocation());
			IBlockState blockState = foundBlock.getDefaultState();
			
			BuildBlock subBlock = BuildBlock.SetBlockState(
					StructureRenderHandler.currentConfiguration, 
					world, StructureRenderHandler.currentConfiguration.pos, 
					assumedNorth, 
					buildBlock.getSubBlock(), 
					foundBlock, 
					blockState);
			
			return StructureRenderHandler.renderComponentInWorld(world, subBlock);
		}
		
		return true;
	}

	private static void doRenderComponent(BuildBlock buildBlock, BlockPos pos)
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		IBlockState state = buildBlock.getBlockState();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		if (state == null)
		{
			GlStateManager.popMatrix();
			return;
		}
		
		GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ() + 1);
		GlStateManager.color(1, 1, 1, 1);
		StructureRenderHandler.renderBlockBrightness(state, 1.0F);

		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}
	
    public static void renderBlockBrightness(IBlockState state, float brightness)
    {
    	BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
    	
        EnumBlockRenderType enumblockrendertype = state.getRenderType();

        if (enumblockrendertype != EnumBlockRenderType.INVISIBLE)
        {
            switch (enumblockrendertype)
            {
                case MODEL:
                case ENTITYBLOCK_ANIMATED:
                {
                	// Only use the chest renderer if this is actually an instance of a chest.
                	if (enumblockrendertype == EnumBlockRenderType.ENTITYBLOCK_ANIMATED
                			&& state.getBlock() instanceof BlockChest)
                	{
                		StructureRenderHandler.chestRenderer.renderChestBrightness(state.getBlock(), brightness);
                		break;
                	}
                	
                    IBakedModel ibakedmodel = brd.getModelForState(state);
                    BlockModelRenderer renderer = brd.getBlockModelRenderer();
                    renderer.renderModelBrightness(ibakedmodel, state, brightness, true);
                    break;
                }
				default:
				{
					break;
				}
            }
        }
    }

}
