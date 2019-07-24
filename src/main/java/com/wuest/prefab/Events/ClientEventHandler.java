package com.wuest.prefab.Events;

import com.mojang.blaze3d.platform.GlStateManager;
import com.wuest.prefab.Config.EntityPlayerConfiguration;
import com.wuest.prefab.Items.ItemBogus;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import org.lwjgl.opengl.GL11;

/**
 * @author WuestMan
 */
@Mod.EventBusSubscriber(modid = Prefab.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public final class ClientEventHandler {
    public static ClientEventHandler instance = new ClientEventHandler();

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
    public void onWorldRenderLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null && mc.objectMouseOver != null && mc.objectMouseOver.hitInfo != null && (!mc.player.isSneaking())) {
            StructureRenderHandler.renderPlayerLook(mc.player, mc.objectMouseOver);
        }

        if (ItemBogus.renderTest) {
            ClientEventHandler.instance.RenderTest(mc.world, mc.player);
        }
    }

    /**
     * This is used to clear out the server configuration on the client side.
     *
     * @param event The event object.
     */
    @SubscribeEvent
    public void EntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote && event.getEntity() instanceof PlayerEntity) {
            // When the player logs out, make sure to re-set the server configuration.
            // This is so a new configuration can be successfully loaded when they switch servers or worlds (on single
            // player.
            ((ClientProxy) Prefab.proxy).serverConfiguration = null;
            ClientEventHandler.playerConfig.clearNonPersistedObjects();
        }
    }

    /**
     * This is used to increment the ticks in game value.
     *
     * @param event The event object.
     */
    @SubscribeEvent
    public void ClientTickEnd(ClientTickEvent event) {
        if (event.phase == Phase.END) {
            Screen gui = Minecraft.getInstance().currentScreen;

            if (gui == null || !gui.isPauseScreen()) {
                // Reset the ticks in game if we are getting close to the maximum value of an integer.
                if (Integer.MAX_VALUE - 100 == ClientEventHandler.ticksInGame) {
                    ClientEventHandler.ticksInGame = 1;
                }

                ClientEventHandler.ticksInGame++;
            }
        }
    }

    private void RenderTest(World worldIn, PlayerEntity playerIn) {
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        PlayerEntity entityplayer = playerIn;
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

        GlStateManager.disableTexture();
        GlStateManager.disableBlend();

        GlStateManager.lineWidth(3.0F);
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
        GlStateManager.enableTexture();
    }

    private void drawLineWithGL(Vec3d blockA, Vec3d blockB) {
        GL11.glColor4f(1F, 0F, 1F, 0F); // change color an set alpha

        GL11.glBegin(GL11.GL_LINE_STRIP);

        GL11.glVertex3d(blockA.x, blockA.y, blockA.z);
        GL11.glVertex3d(blockB.x, blockB.y, blockB.z);

        GL11.glEnd();
    }
}
