package com.prefab.structures.items;

import com.prefab.ClientModRegistryBase;
import com.prefab.ModRegistryBase;
import com.prefab.structures.gui.GuiBulldozer;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;

/**
 * @author WuestMan
 */
public class ItemBulldozer extends StructureItem {

    private boolean creativePowered = false;

    /**
     * Initializes a new instance of the {@link ItemBulldozer} class.
     */
    public ItemBulldozer(Item.Properties properties) {
        super(properties
                .durability(4));
    }

    /**
     * Initializes a new instance of the {@link ItemBulldozer} class
     *
     * @param creativePowered - Set this to true to create an always powered bulldozer.
     */
    public ItemBulldozer(Item.Properties properties, boolean creativePowered) {
        super(properties);

        this.creativePowered = creativePowered;
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) {
            if (context.getClickedFace() == Direction.UP && this.getPoweredValue(context.getPlayer(), context.getHand())) {
                // Open the client side gui to determine the house options.
                ClientModRegistryBase.openGuiForItem(context);
                return InteractionResult.PASS;
            }
        }

        return InteractionResult.FAIL;
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        ModRegistryBase.guiRegistrations.add(x -> this.RegisterGui(GuiBulldozer.class));
    }

    private boolean getPoweredValue(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        return this.getPoweredValue(stack);
    }

    protected boolean getPoweredValue(ItemStack stack) {
        if (this.creativePowered) {
            return true;
        }

        if (stack.getItem() == ModRegistryBase.Bulldozer) {
            if (stack.getComponents().isEmpty()) {
                CompoundTag baseTag = new CompoundTag();
                CompoundTag prefabTag = new CompoundTag();
                baseTag.put("prefab", prefabTag);
                prefabTag.putBoolean("powered", false);

                CustomData customData = CustomData.of(baseTag);
                stack.set(DataComponents.CUSTOM_DATA, customData);
            } else {
                CustomData customData = stack.getComponents().get(DataComponents.CUSTOM_DATA);

                if (customData != null) {
                    CompoundTag tag = customData.copyTag();

                    if (tag.contains("prefab")) {
                        CompoundTag prefabTag = tag.getCompound("prefab");

                        if (prefabTag.contains("powered")) {
                            return prefabTag.getBoolean("powered");
                        }
                    }
                }
            }
        }

        return false;
    }

    public void setPoweredValue(ItemStack stack, boolean value) {
        CompoundTag baseTag = new CompoundTag();
        CompoundTag prefabTag = new CompoundTag();

        prefabTag.putBoolean("powered", value);
        baseTag.put("prefab", prefabTag);

        CustomData customData = CustomData.of(baseTag);
        stack.set(DataComponents.CUSTOM_DATA, customData);
    }
}