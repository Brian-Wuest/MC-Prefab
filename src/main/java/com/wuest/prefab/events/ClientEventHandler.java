package com.wuest.prefab.events;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.config.EntityPlayerConfiguration;
import com.wuest.prefab.proxy.ClientProxy;
import com.wuest.prefab.structures.config.BasicStructureConfiguration;
import com.wuest.prefab.structures.gui.GuiStructure;
import com.wuest.prefab.structures.items.ItemBasicStructure;
import com.wuest.prefab.structures.items.StructureItem;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
    public static ArrayList<KeyMapping> keyBindings = new ArrayList<KeyMapping>();

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
        if (event.getWorld().isClientSide && event.getEntity() instanceof Player) {
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
        for (KeyMapping binding : ClientEventHandler.keyBindings) {
            if (binding.isDown()) {
                if (StructureRenderHandler.currentStructure != null) {
                    ItemStack mainHandStack = Minecraft.getInstance().player.getMainHandItem();
                    ItemStack offHandStack = Minecraft.getInstance().player.getOffhandItem();
                    boolean foundCorrectStructureItem = false;

                    if (mainHandStack != ItemStack.EMPTY || offHandStack != ItemStack.EMPTY) {
                        StructureTagMessage.EnumStructureConfiguration structureConfigurationEnum = StructureTagMessage.EnumStructureConfiguration.getByConfigurationInstance(StructureRenderHandler.currentConfiguration);

                        if (mainHandStack != ItemStack.EMPTY && mainHandStack.getItem() instanceof StructureItem) {
                            // Check main hand.
                            foundCorrectStructureItem = ClientEventHandler.checkIfStackIsCorrectGui(structureConfigurationEnum, mainHandStack);
                        }

                        if (!foundCorrectStructureItem && offHandStack != ItemStack.EMPTY && offHandStack.getItem() instanceof StructureItem) {
                            // Main hand is not correct item; check off-hand
                            foundCorrectStructureItem = ClientEventHandler.checkIfStackIsCorrectGui(structureConfigurationEnum, offHandStack);
                        }
                    }

                    if (foundCorrectStructureItem) {
                        Prefab.network.sendToServer(new StructureTagMessage(
                                StructureRenderHandler.currentConfiguration.WriteToCompoundTag(),
                                StructureTagMessage.EnumStructureConfiguration.getByConfigurationInstance(StructureRenderHandler.currentConfiguration)));
                    }

                    StructureRenderHandler.currentStructure = null;
                }

                break;
            }
        }
    }

    public static boolean checkIfStackIsCorrectGui(StructureTagMessage.EnumStructureConfiguration currentConfiguration, ItemStack stack) {
        GuiStructure mainHandGui = ClientProxy.ModGuis.get(stack.getItem());
        mainHandGui.init();

        if (currentConfiguration == mainHandGui.structureConfiguration) {
            if (currentConfiguration == StructureTagMessage.EnumStructureConfiguration.Basic) {
                ItemBasicStructure item = (ItemBasicStructure) stack.getItem();
                BasicStructureConfiguration.EnumBasicStructureName basicStructureName = ((BasicStructureConfiguration) StructureRenderHandler.currentConfiguration).basicStructureName;

                return item.structureType == basicStructureName;
            } else {
                return true;
            }
        }

        return false;
    }
}
