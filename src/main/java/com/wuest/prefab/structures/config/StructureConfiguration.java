package com.wuest.prefab.structures.config;

import com.wuest.prefab.structures.items.StructureItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * This is the base configuration class used by all structures.
 *
 * @author WuestMan
 */
@SuppressWarnings("WeakerAccess")
public class StructureConfiguration {
    public static String houseFacingName = "House Facing";

    private static String hitXTag = "hitX";
    private static String hitYTag = "hitY";
    private static String hitZTag = "hitZ";
    private static String houseFacingTag = "wareHouseFacing";

    /**
     * The structure facing property.
     */
    public Direction houseFacing;

    /**
     * The position of the structure.
     */
    public BlockPos pos;

    /**
     * Initializes a new instance of the StructureConfiguration class.
     */
    public StructureConfiguration() {
        this.Initialize();
    }

    /**
     * Initializes any properties for this class.
     */
    public void Initialize() {
        this.houseFacing = Direction.NORTH;
    }

    /**
     * Writes the properties to an CompoundTag.
     *
     * @return An CompoundTag with the updated properties.
     */
    public CompoundTag WriteToCompoundTag() {
        CompoundTag tag = new CompoundTag();

        if (this.pos != null) {
            tag.putInt(StructureConfiguration.hitXTag, this.pos.getX());
            tag.putInt(StructureConfiguration.hitYTag, this.pos.getY());
            tag.putInt(StructureConfiguration.hitZTag, this.pos.getZ());
        }

        tag.putString(StructureConfiguration.houseFacingTag, this.houseFacing.getSerializedName());

        tag = this.CustomWriteToCompoundTag(tag);

        return tag;
    }

    /**
     * Reads CompoundTag to create a StructureConfiguration object from.
     *
     * @param messageTag The CompoundTag to read the properties from.
     * @return The updated StructureConfiguration instance.
     */
    public StructureConfiguration ReadFromCompoundTag(CompoundTag messageTag) {
        return null;
    }

    /**
     * Reads CompoundTag to create a StructureConfiguration object from.
     *
     * @param messageTag The CompoundTag to read the properties from.
     * @param config     The existing StructureConfiguration instance to fill the properties in for.
     * @return The updated StructureConfiguration instance.
     */
    public StructureConfiguration ReadFromCompoundTag(CompoundTag messageTag, StructureConfiguration config) {
        if (messageTag != null) {
            if (messageTag.contains(StructureConfiguration.hitXTag)) {
                config.pos = new BlockPos(
                        messageTag.getInt(StructureConfiguration.hitXTag),
                        messageTag.getInt(StructureConfiguration.hitYTag),
                        messageTag.getInt(StructureConfiguration.hitZTag));
            }

            if (messageTag.contains(StructureConfiguration.houseFacingTag)) {
                config.houseFacing = Direction.byName(messageTag.getString(StructureConfiguration.houseFacingTag));
            }

            this.CustomReadFromNBTTag(messageTag, config);
        }

        return config;
    }

    /**
     * Generic method to start the building of the structure.
     *
     * @param player The player which requested the build.
     * @param world  The world instance where the build will occur.
     */
    public void BuildStructure(Player player, ServerLevel world) {
        // This is always on the server.
        BlockPos hitBlockPos = this.pos;

        this.ConfigurationSpecificBuildStructure(player, world, hitBlockPos);
    }

    /**
     * This is used to actually build the structure as it creates the structure instance and calls build structure.
     *
     * @param player      The player which requested the build.
     * @param world       The world instance where the build will occur.
     * @param hitBlockPos This hit block position.
     */
    protected void ConfigurationSpecificBuildStructure(Player player, ServerLevel world, BlockPos hitBlockPos) {
    }

    /**
     * Custom method which can be overridden to write custom properties to the tag.
     *
     * @param tag The CompoundTag to write the custom properties too.
     * @return The updated tag.
     */
    protected CompoundTag CustomWriteToCompoundTag(CompoundTag tag) {
        return tag;
    }

    /**
     * Custom method to read the CompoundTag message.
     *
     * @param messageTag The message to create the configuration from.
     * @param config     The configuration to read the settings into.
     */
    protected void CustomReadFromNBTTag(CompoundTag messageTag, StructureConfiguration config) {
    }

    /**
     * This method will remove 1 structure item from the player's inventory, it is expected that the item is in the
     * player's hand.
     *
     * @param player The player to remove the item from.
     * @param item   the structure item to find.
     */
    protected void RemoveStructureItemFromPlayer(Player player, StructureItem item) {
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() != item) {
            stack = player.getOffhandItem();

            stack.shrink(1);

            if (stack.isEmpty()) {
                player.getInventory().offhand.set(0, ItemStack.EMPTY);
            }
        } else {
            int slot = this.getSlotFor(player.getInventory(), stack);

            if (slot != -1) {
                stack.shrink(1);

                if (stack.isEmpty()) {
                    player.getInventory().setItem(slot, ItemStack.EMPTY);
                }
            }
        }

        player.containerMenu.broadcastChanges();
    }

    protected void DamageHeldItem(Player player, Item item) {
        ItemStack stack = player.getMainHandItem().getItem() == item ? player.getMainHandItem() : player.getOffhandItem();
        InteractionHand hand = player.getMainHandItem().getItem() == item ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        ItemStack copy = stack.copy();

        stack.hurtAndBreak(1, player, (player1) ->
        {
            player1.broadcastBreakEvent(hand);
        });

        if (stack.isEmpty()) {
            net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copy, hand);
            EquipmentSlot slotType = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;

            player.setItemSlot(slotType, ItemStack.EMPTY);
        }

        player.containerMenu.broadcastChanges();
    }

    /**
     * Checks item, NBT, and meta if the item is not damageable
     */
    private boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.tagMatches(stack1, stack2);
    }

    /**
     * Get's the first slot which contains the item in the supplied item stack in the player's main inventory.
     * This method was copied directly from teh player inventory class since it was needed server side.
     *
     * @param playerInventory The player's inventory to try and find a slot.
     * @param stack           The stack to find an associated slot.
     * @return The slot index or -1 if the item wasn't found.
     */
    public int getSlotFor(Inventory playerInventory, ItemStack stack) {
        for (int i = 0; i < playerInventory.items.size(); ++i) {
            if (!playerInventory.items.get(i).isEmpty() && this.stackEqualExact(stack, playerInventory.items.get(i))) {
                return i;
            }
        }

        return -1;
    }
}
