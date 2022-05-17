package com.wuest.prefab.structures.items;

import com.google.common.base.Strings;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.proxy.ClientProxy;
import com.wuest.prefab.structures.custom.base.CustomStructureInfo;
import com.wuest.prefab.structures.gui.GuiCustomStructure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemBlueprint extends StructureItem {
    public ItemBlueprint(Properties properties) {
        super(properties);
    }

    public static final String StructureTag = "structure_id";

    @Override
    public Component getName(@NotNull ItemStack itemStack) {
        CustomStructureInfo clientStructure = ItemBlueprint.getCustomStructureFromStack(itemStack);
        if (clientStructure != null) {
            return new TranslatableComponent(clientStructure.displayName);
        }
        return new TranslatableComponent(this.getDescriptionId(itemStack));
    }

    public static CustomStructureInfo getCustomStructureInHand(Player player) {
        ItemStack stack = player.getOffhandItem();
        // Get off-hand first since that is the right-click hand if there is something in there.
        if (!(stack.getItem() instanceof ItemBlueprint)) {
            if (player.getMainHandItem().getItem() instanceof ItemBlueprint) {
                stack = player.getMainHandItem();
            } else {
                return null;
            }
        }
        return ItemBlueprint.getCustomStructureFromStack(stack);
    }

    public static ItemStack getCustomStructureStackInHand(Player player) {
        ItemStack stack = player.getOffhandItem();
        // Get off-hand first since that is the right-click hand if there is something in there.
        if (!(stack.getItem() instanceof ItemBlueprint)) {
            if (player.getMainHandItem().getItem() instanceof ItemBlueprint) {
                stack = player.getMainHandItem();
            } else {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    public static CustomStructureInfo getCustomStructureFromStack(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(ItemBlueprint.StructureTag)) {
            String structureId = tag.getString(ItemBlueprint.StructureTag);
            if (!Strings.isNullOrEmpty(structureId)) {
                for (CustomStructureInfo clientStructure : ClientProxy.ServerRegisteredStructures) {
                    if (clientStructure.displayName.equalsIgnoreCase(structureId)) {
                        return clientStructure;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        ModRegistry.guiRegistrations.add(x -> this.RegisterGui(GuiCustomStructure.class));
    }
}
