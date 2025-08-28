package com.prefab.neoforge.events;

import com.prefab.ClientModRegistryBase;
import com.prefab.PrefabBase;
import com.prefab.network.ClientToServerTypes;
import com.prefab.structures.config.BasicStructureConfiguration;
import com.prefab.structures.gui.GuiStructure;
import com.prefab.structures.items.ItemBasicStructure;
import com.prefab.structures.items.StructureItem;
import com.prefab.structures.messages.StructureTagMessage;
import com.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = PrefabBase.MODID, value = Dist.CLIENT)
public class GameClientEvents {
    @SubscribeEvent
    public static void KeyInput(InputEvent.Key event) {
        for (KeyMapping binding : ModClientEvents.keyBindings) {
            if (binding.isDown()) {
                if (StructureRenderHandler.currentStructure != null) {
                    ItemStack mainHandStack = Minecraft.getInstance().player.getMainHandItem();
                    ItemStack offHandStack = Minecraft.getInstance().player.getOffhandItem();
                    boolean foundCorrectStructureItem = false;

                    if (mainHandStack != ItemStack.EMPTY || offHandStack != ItemStack.EMPTY) {
                        StructureTagMessage.EnumStructureConfiguration structureConfigurationEnum = StructureTagMessage.EnumStructureConfiguration.getByConfigurationInstance(StructureRenderHandler.currentConfiguration);

                        if (mainHandStack != ItemStack.EMPTY && mainHandStack.getItem() instanceof StructureItem) {
                            // Check main hand.
                            foundCorrectStructureItem = GameClientEvents.checkIfStackIsCorrectGui(structureConfigurationEnum, mainHandStack);
                        }

                        if (!foundCorrectStructureItem && offHandStack != ItemStack.EMPTY && offHandStack.getItem() instanceof StructureItem) {
                            // Main hand is not correct item; check off-hand
                            foundCorrectStructureItem = GameClientEvents.checkIfStackIsCorrectGui(structureConfigurationEnum, offHandStack);
                        }
                    }

                    if (foundCorrectStructureItem) {
                        // Send the configuration of the previewed structure from the client (player) to the server for building.
                        // It doesn't have to be sent to a particular player, the server just needs to handle the process.
                        StructureTagMessage message =
                                new StructureTagMessage(StructureRenderHandler.currentConfiguration.WriteToCompoundTag(),
                                        StructureTagMessage.EnumStructureConfiguration.getByConfigurationInstance(StructureRenderHandler.currentConfiguration));

                        PrefabBase.networkWrapper.sendToServer(ClientToServerTypes.STRUCTURE_BUILD, message);
                    }

                    StructureRenderHandler.currentStructure = null;
                }

                break;
            }
        }
    }

    /**
     * The player right-click block event. This is used to stop the structure rendering for the preview.
     *
     * @param event The event object.
     */
    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (StructureRenderHandler.currentStructure != null && event.getEntity() == Minecraft.getInstance().player) {
            StructureRenderHandler.setStructure(null, null);
            event.setCanceled(true);
        }
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
