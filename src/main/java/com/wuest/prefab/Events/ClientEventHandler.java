package com.wuest.prefab.Events;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Blocks.IMetaBlock;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.Items.ItemBogus;
import com.wuest.prefab.Items.Structures.ItemBasicStructure;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Render.StructureRenderHandler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 
 * @author WuestMan
 *
 */
@EventBusSubscriber(value = { Side.CLIENT })
public class ClientEventHandler
{
	/**
	 * Determines how long a shader has been running.
	 */
	public static int ticksInGame;

	/**
	 * The world render last event. This is used for structure rendering.
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();

		if (mc.thePlayer != null && mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && (!mc.thePlayer.isSneaking()))
		{
			StructureRenderHandler.renderPlayerLook(mc.thePlayer, mc.objectMouseOver);
		}
		
		if (ItemBogus.renderTest)
		{
			ClientEventHandler.RenderTest(mc.theWorld, mc.thePlayer);
		}
	}

	/**
	 * The player right-click block event. This is used to stop the structure rendering for the preview.
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event)
	{
		if (event.getWorld().isRemote)
		{
			if (StructureRenderHandler.currentStructure != null && event.getEntityPlayer() == Minecraft.getMinecraft().thePlayer)
			{
				StructureRenderHandler.setStructure(null, EnumFacing.NORTH, null);
				event.setCanceled(true);
			}
		}
	}
	
	/**
	 * This is used to clear out the server configuration on the client side.
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void OnClientDisconnectEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
	{
		// When the player logs out, make sure to re-set the server configuration. 
	 	// This is so a new configuration can be successfully loaded when they switch servers or worlds (on single player.
	 	((ClientProxy)Prefab.proxy).serverConfiguration = null;
	}
	
	/**
	 * This is used to increment the ticks in game value.
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void ClientTickEnd(ClientTickEvent event)
	{
		if (event.phase == Phase.END)
		{
			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			
			if (gui == null || !gui.doesGuiPauseGame()) 
			{
				// Reset the ticks in game if we are getting close to the maximum value of an integer.
				if (Integer.MAX_VALUE - 100 == ClientEventHandler.ticksInGame)
				{
					ClientEventHandler.ticksInGame = 1;
				}
				
				ClientEventHandler.ticksInGame++;
			}
		}
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		for (Block block: ModRegistry.ModBlocks)
		{
			ClientEventHandler.regBlock(block);
		}
		
		for (Item item: ModRegistry.ModItems)
		{
			ClientEventHandler.regItem(item);
		}
	}
	
	/**
	 * Registers an item to be rendered. This is needed for textures.
	 * @param item The item to register.
	 */
	public static void regItem(Item item) 
	{
		ClientEventHandler.regItem(item, 0, item.getUnlocalizedName().substring(5));
	}
	
	/**
	 * Registers an item to be rendered. This is needed for textures.
	 * @param item The item to register.
	 * @param metaData The meta data for the item to register.
	 * @param blockName the name of the block.
	 */
	public static void regItem(Item item, int metaData, String blockName)
	{
		ModelResourceLocation location = new ModelResourceLocation(blockName, "inventory");
		//System.out.println("Registering Item: " + location.getResourceDomain() + "[" + location.getResourcePath() + "]");
		
		if (!(item instanceof ItemBasicStructure))
		{
			ModelLoader.setCustomModelResourceLocation(item, metaData, location);
		}
		else
		{
			ArrayList<ResourceLocation> names = new ArrayList<ResourceLocation>();
			
			for (EnumBasicStructureName value : EnumBasicStructureName.values())
			{
				if (value.getResourceLocation() != null)
				{
					names.add(value.getResourceLocation());
				}
			}
			
			ResourceLocation[] resources = new ResourceLocation[names.size()];
			resources = names.toArray(resources);
			
			ModelLoader.registerItemVariants(item, resources);
		}
	}

	/**
	 * Registers a block to be rendered. This is needed for textures.
	 * @param block The block to register.
	 */
	public static void regBlock(Block block)
	{
		List<ItemStack> stacks = Lists.<ItemStack>newArrayList();
		
		Item itemBlock = Item.getItemFromBlock(block);
		
		// If there are sub-blocks for this block, register each of them.
		block.getSubBlocks(itemBlock, null, stacks);
		
		if (itemBlock != null)
		{
			if (stacks.size() > 0)
			{
				for (ItemStack stack : stacks)
				{
					Block subBlock = block.getStateFromMeta(stack.getMetadata()).getBlock();
					String name = "";
					
					if (block instanceof IMetaBlock)
					{
						name = "prefab:" + ((IMetaBlock)block).getMetaDataUnLocalizedName(stack.getMetadata());
					}
					else
					{
						name = subBlock.getRegistryName().toString();
					}
					
					ClientEventHandler.regItem(stack.getItem(), stack.getMetadata(), name);
				}
			}
			else
			{
				ClientEventHandler.regItem(itemBlock);
			}
		}
	}

    private static void RenderTest(World worldIn, EntityPlayer playerIn)
    {
    	float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        EntityPlayer entityplayer = playerIn;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        BlockPos playerPosition = new BlockPos(entityplayer.posX, entityplayer.posY, entityplayer.posZ);
        BlockPos blockPos = playerPosition.offset(entityplayer.getHorizontalFacing().getOpposite());
        
        double playerVertical = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)partialTicks;
        
        double playerLevelYCoordinate = blockPos.getY() - Math.abs(playerPosition.getY()) + (playerPosition.getY() - entityplayer.posY);
        double playerLevelUpOneYCoordinate = blockPos.getY() - Math.abs(playerPosition.getY()) + 1 + (playerPosition.getY() - entityplayer.posY);
        
        // This makes the block north and in-line with the player's line of sight.
        double blockXOffset = (playerPosition.getX() - blockPos.getX()) + (playerPosition.getX() - entityplayer.posX);
        double blocZOffset = (playerPosition.getZ() - blockPos.getZ()) + (playerPosition.getZ() - entityplayer.posZ);
        
        GlStateManager.disableTexture2D();
        GlStateManager.disableBlend();
        
        GlStateManager.glLineWidth(3.0F);
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);

        // Draw the verticals of the box.
        for (int k = 1; k < 2; k += 1)
        {
        	// Green
        	vertexbuffer.pos(blockXOffset, playerLevelYCoordinate, blocZOffset).color(0.6F, 1.0F, 0.0F, 0.0F).endVertex();
            vertexbuffer.pos(blockXOffset, playerLevelUpOneYCoordinate, blocZOffset).color(0.6F, 1.0F, 0.0F, 1.0F).endVertex();
        	
            // Orange
            vertexbuffer.pos(blockXOffset + (double)k, playerLevelYCoordinate, blocZOffset).color(1.0F, 0.6F, 0.0F, 0.0F).endVertex();
            vertexbuffer.pos(blockXOffset + (double)k, playerLevelUpOneYCoordinate, blocZOffset).color(1.0F, 0.6F, 0.0F, 1.0F).endVertex();
            
            vertexbuffer.pos(blockXOffset, playerLevelYCoordinate, blocZOffset + (double)k).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            vertexbuffer.pos(blockXOffset, playerLevelUpOneYCoordinate, blocZOffset + (double)k).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            
            vertexbuffer.pos(blockXOffset + 1.0D, playerLevelYCoordinate, blocZOffset + (double)k).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            vertexbuffer.pos(blockXOffset + 1.0D, playerLevelUpOneYCoordinate, blocZOffset + (double)k).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        }
        
        // All horizontals.
        for (int i1 = playerPosition.getY(); i1 <= playerPosition.getY() + 1; i1 += 1)
        {
            double d7 = i1- playerVertical;
            vertexbuffer.pos(blockXOffset, d7, blocZOffset).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            vertexbuffer.pos(blockXOffset, d7, blocZOffset + 1.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexbuffer.pos(blockXOffset + 1.0D, d7, blocZOffset + 1.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexbuffer.pos(blockXOffset + 1.0D, d7, blocZOffset).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexbuffer.pos(blockXOffset, d7, blocZOffset).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
    }
    
    private static void drawLineWithGL(Vec3d blockA, Vec3d blockB) 
    {
    	GL11.glColor4f(1F, 0F, 1F, 0F);  // change color an set alpha

    	GL11.glBegin(GL11.GL_LINE_STRIP);

    	GL11.glVertex3d(blockA.xCoord, blockA.yCoord, blockA.zCoord);
    	GL11.glVertex3d(blockB.xCoord, blockB.yCoord, blockB.zCoord);

    	GL11.glEnd();
    }
}
