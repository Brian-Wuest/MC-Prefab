package com.wuest.prefab.Events;

import com.wuest.prefab.Config.EntityPlayerConfiguration;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Structures.Messages.StructureTagMessage;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Prefab.MODID, value = {Dist.CLIENT})
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
	 * Contains the keybindings registered.
	 */
	public static ArrayList<KeyBinding> keyBindings = new ArrayList<KeyBinding>();

	/**
	 * The world render last event. This is used for structure rendering.
	 *
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.player != null && (!mc.player.isCrouching())) {
			StructureRenderHandler.renderPlayerLook(mc.player, mc.hitResult, event.getMatrixStack());
		}
	}

	/**
	 * This is used to clear out the server configuration on the client side.
	 *
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void EntityJoinWorldEvent(EntityJoinWorldEvent event) {
		if (event.getWorld().isClientSide && event.getEntity() instanceof PlayerEntity) {
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
	public static void ClientTickEnd(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			Screen gui = Minecraft.getInstance().screen;

			if (gui == null || !gui.isPauseScreen()) {
				// Reset the ticks in game if we are getting close to the maximum value of an integer.
				if (Integer.MAX_VALUE - 100 == ClientEventHandler.ticksInGame) {
					ClientEventHandler.ticksInGame = 1;
				}

				ClientEventHandler.ticksInGame++;
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	@OnlyIn(Dist.CLIENT)
	public static void KeyInput(InputEvent.KeyInputEvent event) {
		for (KeyBinding binding : ClientEventHandler.keyBindings) {
			if (binding.isDown()) {
				if (StructureRenderHandler.currentStructure != null)
				{
					Prefab.network.sendToServer(new StructureTagMessage(
							StructureRenderHandler.currentConfiguration.WriteToCompoundNBT(),
							StructureTagMessage.EnumStructureConfiguration.getByConfigurationInstance(StructureRenderHandler.currentConfiguration)));

					StructureRenderHandler.currentStructure = null;
				}

				break;
			}
		}
	}

	private static void RenderTest(World worldIn, PlayerEntity playerIn) {
/*        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        BlockPos playerPosition = new BlockPos(playerIn.posX, playerIn.posY, playerIn.posZ);
        BlockPos blockPos = playerPosition.offset(playerIn.getHorizontalFacing().getOpposite());

        double playerVertical = playerIn.lastTickPosY + (playerIn.posY - playerIn.lastTickPosY) * (double) partialTicks;

        double playerLevelYCoordinate = blockPos.getY() - Math.abs(playerPosition.getY()) + (playerPosition.getY() - playerIn.posY);
        double playerLevelUpOneYCoordinate = blockPos.getY() - Math.abs(playerPosition.getY()) + 1 + (playerPosition.getY() - playerIn.posY);

        // This makes the block north and in-line with the player's line of sight.
        double blockXOffset = (playerPosition.getX() - blockPos.getX()) + (playerPosition.getX() - playerIn.posX);
        double blocZOffset = (playerPosition.getZ() - blockPos.getZ()) + (playerPosition.getZ() - playerIn.posZ);

        GlStateManager.disableTexture();
        GlStateManager.disableBlend();

        GlStateManager.lineWidth(3.0F);
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);

        // Draw the verticals of the box.
        for (int k = 1; k < 2; k += 1) {
            // Green
            vertexBuffer.pos(blockXOffset, playerLevelYCoordinate, blocZOffset).color(0.6F, 1.0F, 0.0F, 0.0F).endVertex();
            vertexBuffer.pos(blockXOffset, playerLevelUpOneYCoordinate, blocZOffset).color(0.6F, 1.0F, 0.0F, 1.0F).endVertex();

            // Orange
            vertexBuffer.pos(blockXOffset + (double) k, playerLevelYCoordinate, blocZOffset).color(1.0F, 0.6F, 0.0F, 0.0F).endVertex();
            vertexBuffer.pos(blockXOffset + (double) k, playerLevelUpOneYCoordinate, blocZOffset).color(1.0F, 0.6F, 0.0F, 1.0F).endVertex();

            vertexBuffer.pos(blockXOffset, playerLevelYCoordinate, blocZOffset + (double) k).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            vertexBuffer.pos(blockXOffset, playerLevelUpOneYCoordinate, blocZOffset + (double) k).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();

            vertexBuffer.pos(blockXOffset + 1.0D, playerLevelYCoordinate, blocZOffset + (double) k).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            vertexBuffer.pos(blockXOffset + 1.0D, playerLevelUpOneYCoordinate, blocZOffset + (double) k).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        }

        // All horizontals.
        for (int i1 = playerPosition.getY(); i1 <= playerPosition.getY() + 1; i1 += 1) {
            double d7 = i1 - playerVertical;
            vertexBuffer.pos(blockXOffset, d7, blocZOffset).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            vertexBuffer.pos(blockXOffset, d7, blocZOffset + 1.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexBuffer.pos(blockXOffset + 1.0D, d7, blocZOffset + 1.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexBuffer.pos(blockXOffset + 1.0D, d7, blocZOffset).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
            vertexBuffer.pos(blockXOffset, d7, blocZOffset).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableBlend();
        GlStateManager.enableTexture();*/
	}

	private static void drawLineWithGL(Vector3d blockA, Vector3d blockB) {
		GL11.glColor4f(1F, 0F, 1F, 0F); // change color an set alpha

		GL11.glBegin(GL11.GL_LINE_STRIP);

		GL11.glVertex3d(blockA.x, blockA.y, blockA.z);
		GL11.glVertex3d(blockB.x, blockB.y, blockB.z);

		GL11.glEnd();
	}
}
