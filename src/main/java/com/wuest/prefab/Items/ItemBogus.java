package com.wuest.prefab.Items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemBogus extends Item {
    public static boolean renderTest = false;

    public ItemBogus(String name) {
        super(new Item.Properties().group(ItemGroup.MATERIALS));

        ModRegistry.setItemName(this, name);
    }

    /**
     * Called when the equipped item is right clicked.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (worldIn.isRemote) {
            ItemBogus.renderTest = !ItemBogus.renderTest;
        }

        return new ActionResult(ActionResultType.PASS, this);
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity && worldIn.isRemote) {
            PlayerEntity entityPlayer = (PlayerEntity) entityIn;

            ItemStack mainHand = entityPlayer.getHeldItemMainhand();
            ItemStack offHand = entityPlayer.getHeldItemOffhand();

            // Check to see if this type of item is in one of the player's hands.
            boolean selected = (mainHand != null && mainHand.getItem() instanceof ItemBogus) || (offHand != null && offHand.getItem() instanceof ItemBogus);

            // this.RenderTest(worldIn, entityPlayer);
            ItemBogus.renderTest = selected;
        }
    }
}
