package com.wuest.prefab.Items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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
	 *
	 * @param name The name of the item to register.
	 */
	public ItemCompressedChest(String name) {
		super(new Item.Properties().group(ItemGroup.MATERIALS));

		ModRegistry.setItemName(this, name);
	}

	/**
	 * allows items to add custom lines of information to the mouse-over description
	 */
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new StringTextComponent("Used in the recipes for structures, not for direct storage"));
	}
}