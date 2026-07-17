package com.prefab.fabric.events;

import com.prefab.ClientModRegistryBase;
import com.prefab.PrefabBase;
import com.prefab.network.ClientToServerTypes;
import com.prefab.structures.config.BasicStructureConfiguration;
import com.prefab.fabric.ClientModRegistry;
import com.prefab.structures.gui.GuiStructure;
import com.prefab.structures.items.ItemBasicStructure;
import com.prefab.structures.items.StructureItem;
import com.prefab.structures.messages.StructureTagMessage;
import com.prefab.structures.render.PreviewRenderer;
import com.prefab.structures.render.StructureRenderHandler;
import com.prefab.structures.render.UpdatedPreviewRenderer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class ClientEvents {
    /**
     * Determines how long a shader has been running.
     */
    public static int ticksInGame;

    public static void registerClientEvents() {
        StructureClientEventHandler.registerStructureClientSideEvents();

        ClientEvents.registerClientEndTick();
    }

    public static void registerClientEndTick() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ClientModRegistry.keyBinding.isDown()) {
                if (StructureRenderHandler.currentStructure != null) {
                    ItemStack mainHandStack = Minecraft.getInstance().player.getMainHandItem();
                    ItemStack offHandStack = Minecraft.getInstance().player.getOffhandItem();
                    boolean foundCorrectStructureItem = false;

                    if (mainHandStack != ItemStack.EMPTY || offHandStack != ItemStack.EMPTY) {
                        StructureTagMessage.EnumStructureConfiguration structureConfigurationEnum = StructureTagMessage.EnumStructureConfiguration.getByConfigurationInstance(StructureRenderHandler.currentConfiguration);

                        if (mainHandStack != ItemStack.EMPTY && mainHandStack.getItem() instanceof StructureItem) {
                            // Check main hand.
                            foundCorrectStructureItem = ClientEvents.checkIfStackIsCorrectGui(structureConfigurationEnum, mainHandStack);
                        }

                        if (!foundCorrectStructureItem && offHandStack != ItemStack.EMPTY && offHandStack.getItem() instanceof StructureItem) {
                            // Main hand is not correct item; check off-hand
                            foundCorrectStructureItem = ClientEvents.checkIfStackIsCorrectGui(structureConfigurationEnum, offHandStack);
                        }
                    }

                    if (foundCorrectStructureItem) {
                        StructureTagMessage message =
                        new StructureTagMessage(StructureRenderHandler.currentConfiguration.WriteToCompoundTag(),
                                StructureTagMessage.EnumStructureConfiguration.getByConfigurationInstance(StructureRenderHandler.currentConfiguration));

                        PrefabBase.networkWrapper.sendToServer(ClientToServerTypes.STRUCTURE_BUILD, message);
                    }

                    StructureRenderHandler.currentStructure = null;
                    PreviewRenderer.currentStructure = null;
                    UpdatedPreviewRenderer.setStructure(null, null);
                }
            }
        });
    }

    public static boolean checkIfStackIsCorrectGui(StructureTagMessage.EnumStructureConfiguration currentConfiguration, ItemStack stack) {
        GuiStructure mainHandGui = ClientModRegistryBase.ModGuis.get(stack.getItem());

        if (currentConfiguration == mainHandGui.structureConfiguration) {
            if (currentConfiguration == StructureTagMessage.EnumStructureConfiguration.Basic) {
                ItemBasicStructure item = (ItemBasicStructure) stack.getItem();
                BasicStructureConfiguration.EnumBasicStructureName basicStructureName = ((BasicStructureConfiguration)StructureRenderHandler.currentConfiguration).basicStructureName;

                return item.structureType == basicStructureName;
            } else {
                return true;
            }
        }

        return false;
    }
}
