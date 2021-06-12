package com.wuest.prefab.Items;

import com.wuest.prefab.Utils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This is a condensed chest used in the construction of the warehouse.
 *
 * @author WuestMan
 */
public class ItemCompressedChest extends Item {
    /**
     * Initializes a new instance of the ItemCondensedChest class.
     */
    public ItemCompressedChest() {
        super(new Item.Properties().tab(ItemGroup.TAB_MATERIALS));

    }

    /**
     * allows items to add custom lines of information to the mouse-over description
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(Utils.createTextComponent("Used in the recipes for structures, not for direct storage"));
    }
}