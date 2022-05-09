package com.wuest.prefab.gui.screens.menus;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.items.ItemBlueprint;
import com.wuest.prefab.proxy.messages.DraftingTableSyncMessage;
import com.wuest.prefab.structures.custom.base.CustomStructureInfo;
import com.wuest.prefab.structures.custom.base.ItemInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DraftingTableMenu extends AbstractContainerMenu {
    public final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private CustomStructureInfo selectedStructureInfo;
    private boolean isTakingStructure;
    private final Player player;

    private IStructureMaterialLoader parent;

    public static @NotNull DraftingTableMenu RegisteredMenuType(int i, Inventory inventory) {
        return new DraftingTableMenu(ModRegistry.DraftingTableMenuType.get(), i, inventory, ContainerLevelAccess.NULL);
    }

    public DraftingTableMenu(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        this(ModRegistry.DraftingTableMenuType.get(), i, inventory, containerLevelAccess);
    }

    public DraftingTableMenu(MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i);
        this.player = inventory.player;
        this.access = containerLevelAccess;
        int k = 18;
        int l;
        int m;

        this.addSlot(new Slot(this.resultSlots, 2, 152, 130) {
            public boolean mayPlace(@NotNull ItemStack itemStack) {
                return false;
            }

            public boolean mayPickup(@NotNull Player player) {
                return DraftingTableMenu.this.mayPickup(player, this.hasItem());
            }

            public void onTake(@NotNull Player player, @NotNull ItemStack itemStack) {
                super.onTake(player, itemStack);
                DraftingTableMenu.this.onTake(player);
            }

            @Override
            public void setChanged() {
                super.setChanged();

                DraftingTableMenu.this.resultSlotChanged();
            }
        });

        // Create the player hot-bar slots
        for (l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, 194 + k) {
                @Override
                public void setChanged() {
                    super.setChanged();

                    DraftingTableMenu.this.triggerSlotChanged();
                }
            });
        }

        // Create the player inventory slots.
        for (l = 0; l < 3; ++l) {
            for (m = 0; m < 9; ++m) {
                this.addSlot(new Slot(inventory, m + l * 9 + 9, 8 + m * 18, 136 + l * 18 + k) {
                    @Override
                    public void setChanged() {
                        super.setChanged();

                        DraftingTableMenu.this.triggerSlotChanged();
                    }
                });
            }
        }
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.clearContainer(player, this.resultSlots);
        });
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.access.evaluate((level, blockPos) ->
                        this.isValidBlock(level.getBlockState(blockPos))
                                && player.distanceToSqr((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D) <= 64.0D,
                true);
    }

    public void setSelectedStructureInfo(CustomStructureInfo selectedStructureInfo) {
        this.selectedStructureInfo = selectedStructureInfo;

        this.setResultSlot();
    }

    public void setParent(IStructureMaterialLoader parent) {
        this.parent = parent;
    }

    protected void setResultSlot() {
        // Note: This is only called from client-side.
        if (this.selectedStructureInfo != null) {
            if (this.player.level.isClientSide) {
                // This way the server can update the result slot with the new item when an item is taken.
                CompoundTag structureTag = new CompoundTag();
                this.selectedStructureInfo.writeToTag(structureTag);

                DraftingTableSyncMessage messagePacket = new DraftingTableSyncMessage(structureTag);

                Prefab.network.sendToServer(messagePacket);
            } else {
                ItemStack stack = this.createBluePrintStack();

                // Server-side, just set the result slot.
                this.resultSlots.setItem(0, stack);
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        int slot = player.getInventory().getFreeSlot();

        // Make sure that there are free slots for this operation.
        if (slot != -1 && this.selectedStructureInfo != null) {
            ItemStack stack = null;
            boolean runProcessAgainForNextStack = false;

            while (true) {
                // Check if the player has enough items, if they do remove the items and then set or create the blueprint stack.
                if (this.inventoryHasRequiredItems(player)) {
                    this.removeRequiredItemsFromPlayerInventory(player);

                    if (stack == null) {
                        stack = this.createBluePrintStack();
                    } else {
                        stack.grow(1);

                        if (stack.getCount() == stack.getMaxStackSize() && this.inventoryHasRequiredItems(player)) {
                            // The stack is full but there are still more materials to take.
                            runProcessAgainForNextStack = true;
                            break;
                        }
                    }
                } else {
                    break;
                }
            }

            if (stack != null) {
                player.getInventory().setItem(slot, stack);
            }

            // This is needed for situations where the designer made the recipe really cheap and the player can
            // Generate more than 64 items in a single go.
            if (runProcessAgainForNextStack) {
                this.quickMoveStack(player, i);
            }
        }

        return ItemStack.EMPTY;
    }

    protected boolean isValidBlock(BlockState blockState) {
        return blockState.getBlock() == ModRegistry.DraftingTable.get();
    }

    protected boolean mayPickup(Player player, boolean ignoredBl) {
        return this.inventoryHasRequiredItems(player);
    }

    protected void onTake(Player player) {
        this.isTakingStructure = true;

        this.removeRequiredItemsFromPlayerInventory(player);

        this.isTakingStructure = false;

        // Make sure to re-trigger slot changed to update the GUI.
        // This may also allow the player to take another item from the slot.
        this.setResultSlot();
    }

    protected void triggerSlotChanged() {
        // Make sure to not try to trigger the GUI re-load when the player is currently taking the item or the parent hasn't been set yet.
        if (this.parent != null && !this.isTakingStructure) {
            // This will update the UI to show the potentially updated needed/has values for materials.
            this.parent.loadMaterialEntries();
        }
    }

    protected void resultSlotChanged() {
        if (!this.player.level.isClientSide) {
            this.setResultSlot();
        }
    }

    protected ItemStack createBluePrintStack() {
        if (this.selectedStructureInfo != null) {
            ItemStack stack = new ItemStack(ModRegistry.BlankBlueprint.get());
            CompoundTag tag = stack.getOrCreateTag();
            tag.putString(ItemBlueprint.StructureTag, this.selectedStructureInfo.displayName);
            return stack;
        }

        return ItemStack.EMPTY;
    }

    protected boolean inventoryHasRequiredItems(Player player) {
        boolean returnValue = true;

        if (this.selectedStructureInfo == null) {
            returnValue = false;
        } else {
            for (ItemInfo info : this.selectedStructureInfo.requiredItems) {
                int hasCount = player.getInventory().countItem(info.registeredItem);

                if (info.count > hasCount) {
                    returnValue = false;
                    break;
                }
            }
        }

        return returnValue;
    }

    protected void removeRequiredItemsFromPlayerInventory(Player player) {
        if (this.selectedStructureInfo != null) {
            Inventory playerInventory = player.getInventory();

            for (ItemInfo info : this.selectedStructureInfo.requiredItems) {
                ItemStack itemInfoStack = new ItemStack(info.registeredItem);
                int countRemaining = info.count;

                while (countRemaining > 0) {
                    int slot = playerInventory.findSlotMatchingItem(itemInfoStack);

                    if (slot == -1) {
                        break;
                    }

                    ItemStack slotStack = playerInventory.getItem(slot);
                    int slotCount = slotStack.getCount();

                    if (slotCount == 0) {
                        break;
                    }

                    if (slotCount > countRemaining) {
                        slotStack.setCount(slotCount - countRemaining);
                        countRemaining = 0;
                    } else {
                        playerInventory.setItem(slot, ItemStack.EMPTY);
                        countRemaining -= slotCount;
                    }

                    playerInventory.setChanged();
                }
            }
        }
    }

    /**
     * This interface is used to trigger a re-load of the item-list control on the GUI.
     */
    public interface IStructureMaterialLoader {
        /**
         * Re-loads the item list on the UI.
         */
        void loadMaterialEntries();
    }
}
