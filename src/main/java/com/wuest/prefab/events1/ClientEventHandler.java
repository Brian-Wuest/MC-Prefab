package com.wuest.prefab.events;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.blocks.IMetaBlock;
import com.wuest.prefab.config.EntityPlayerConfiguration;
import com.wuest.prefab.items.ItemBogus;
import com.wuest.prefab.proxy.ClientProxy;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

/**
 * @author WuestMan
 */
@EventBusSubscriber(value =
        {Side.CLIENT})
public class ClientEventHandler {
    /**
     * Determines how long a shader has been running.
     */
    public static int ticksInGame;

    /**
     * This client event handler is used to store player specific data.
     */
    public static EntityPlayerConfiguration playerConfig = new EntityPlayerConfiguration();

    /**
     * The world render last event. This is used for structure rendering.
     *
     * @param event The event object.
     */
    @SubscribeEvent
    public static void onWorldRenderLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player != null && mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && (!mc.player.isSneaking())) {
            StructureRenderHandler.renderPlayerLook(mc.player, mc.objectMouseOver);
        }

        if (ItemBogus.renderTest) {
            ClientEventHandler.RenderTest(mc.world, mc.player);
        }
    }

    /**
     * This is used to clear out the server configuration on the client side.
     *
     * @param event The event object.
     */
    @SubscribeEvent
    public static void OnClientDisconnectEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        // When the player logs out, make sure to re-set the server configuration.
        // This is so a new configuration can be successfully loaded when they switch servers or worlds (on single
        // player.
        ((ClientProxy) Prefab.proxy).serverConfiguration = null;
        ClientEventHandler.playerConfig.clearNonPersistedObjects();
    }

    /**
     * This is used to increment the ticks in game value.
     *
     * @param event The event object.
     */
    @SubscribeEvent
    public static void ClientTickEnd(ClientTickEvent event) {
        if (event.phase == Phase.END) {
            GuiScreen gui = Minecraft.getMinecraft().currentScreen;

            if (gui == null || !gui.doesGuiPauseGame()) {
                // Reset the ticks in game if we are getting close to the maximum value of an integer.
                if (Integer.MAX_VALUE - 100 == ClientEventHandler.ticksInGame) {
                    ClientEventHandler.ticksInGame = 1;
                }

                ClientEventHandler.ticksInGame++;
            }
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Block block : ModRegistry.ModBlocks) {
            ClientEventHandler.regBlock(block);
        }

        for (Item item : ModRegistry.ModItems) {
            ClientEventHandler.regItem(item);
        }
    }

    /**
     * Registers an item to be rendered. This is needed for textures.
     *
     * @param item The item to register.
     */
    public static void regItem(Item item) {
        ClientEventHandler.regItem(item, 0, item.getTranslationKey().substring(5));
    }

    /**
     * Registers an item to be rendered. This is needed for textures.
     *
     * @param item      The item to register.
     * @param metaData  The meta data for the item to register.
     * @param blockName the name of the block.
     */
    public static void regItem(Item item, int metaData, String blockName) {
        ModelResourceLocation location = new ModelResourceLocation(blockName, "inventory");
        // System.out.println("Registering Item: " + location.getResourceDomain() + "[" + location.getResourcePath() +
        // "]");
        ModelLoader.setCustomModelResourceLocation(item, metaData, location);
    }

    /**
     * Registers a block to be rendered. This is needed for textures.
     *
     * @param block The block to register.
     */
    public static void regBlock(Block block) {
        NonNullList<ItemStack> stacks = NonNullList.create();

        Item itemBlock = Item.getItemFromBlock(block);

        // If there are sub-blocks for this block, register each of them.
        block.getSubBlocks(null, stacks);

        if (itemBlock != null) {
            if (stacks.size() > 0) {
                for (ItemStack stack : stacks) {
                    Block subBlock = block.getStateFromMeta(stack.getMetadata()).getBlock();
                    String name = "";

                    if (block instanceof IMetaBlock) {
                        name = "prefab:" + ((IMetaBlock) block).getMetaDataUnLocalizedName(stack.getMetadata());
                    } else {
                        name = subBlock.getRegistryName().toString();
                    }

                    ClientEventHandler.regItem(stack.getItem(), stack.getMetadata(), name);
                }
            } else {
                ClientEventHandler.regItem(itemBlock);
            }
        }
    }

    private static void RenderTest(World worldIn, EntityPlayer playerIn) {
        float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        EntityPlayer entityplayer = playerIn;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        BlockPos playerPosition = new BlockPos(entityplayer.posX, entityplayer.posY, entityplayer.posZ);
        BlockPos blockPos = playerPosition.offset(entityplayer.getHorizontalFacing().getOpposite());

        double playerVertical = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double) partialTicks;

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
        for (int k = 1; k < 2; k += 1) {
            // Green
            vertexbuffer.pos(blockXOffset, playerLevelYCoordinate, blocZOffset).color(0.6F, 1.0F, 0.0F, 0.0F).endVertex();
            vertexbuffer.pos(blockXOffset, playerLevelUpOneYCoordinate, blocZOffset).color(0.6F, 1.0F, 0.0F, 1.0F).endVertex();

            // Orange
            vertexbuffer.pos(blockXOffset + (double) k, playerLevelYCoordinate, blocZOffset).color(1.0F, 0.6F, 0.0F, 0.0F).endVertex();
            vertexbuffer.pos(blockXOffset + (double) k, playerLevelUpOneYCoordinate, blocZOffset).color(1.0F, 0.6F, 0.0F, 1.0F).endVertex();

            vertexbuffer.pos(blockXOffset, playerLevelYCoordinate, blocZOffset + (double) k).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            vertexbuffer.pos(blockXOffset, playerLevelUpOneYCoordinate, blocZOffset + (double) k).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

            vertexbuffer.pos(blockXOffset + 1.0D, playerLevelYCoordinate, blocZOffset + (double) k).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            vertexbuffer.pos(blockXOffset + 1.0D, playerLevelUpOneYCoordinate, blocZOffset + (double) k).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        }

        // All horizontals.
        for (int i1 = playerPosition.getY(); i1 <= playerPosition.getY() + 1; i1 += 1) {
            double d7 = i1 - playerVertical;
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

    private static void drawLineWithGL(Vec3d blockA, Vec3d blockB) {
        GL11.glColor4f(1F, 0F, 1F, 0F); // change color an set alpha

        GL11.glBegin(GL11.GL_LINE_STRIP);

        GL11.glVertex3d(blockA.x, blockA.y, blockA.z);
        GL11.glVertex3d(blockB.x, blockB.y, blockB.z);

        GL11.glEnd();
    }
}
