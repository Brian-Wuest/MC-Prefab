package com.wuest.prefab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.wuest.prefab.Blocks.BlockCompressedStone;
import com.wuest.prefab.Blocks.IMetaBlock;
import com.wuest.prefab.Config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.Items.ItemBasicStructure;
import com.wuest.prefab.Render.PrefabModelMesher;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

/**
 * This class is used to assist in registering blocks/items for rendering.
 * @author WuestMan
 *
 */
public final class ItemRenderRegister 
{
	/**
	 * Registers the blocks and items from the main class in the item renderer.
	 */
	public static void registerItemRenderer()
	{
		// Blocks.
		for(Block currentBlock : ModRegistry.ModBlocks)
		{
			ItemRenderRegister.regBlock(currentBlock);
		}

		// Items.
		for (Item currentItem : ModRegistry.ModItems)
		{
			ItemRenderRegister.regItem(currentItem);
		}
	}

	/**
	 * Registers an item to be rendered. This is needed for textures.
	 * @param item The item to register.
	 */
	public static void regItem(Item item) 
	{
		ItemRenderRegister.regItem(item, 0, item.getUnlocalizedName().substring(5));
	}
	
	/**
	 * Registers an item to be rendered. This is needed for textures.
	 * @param item The item to register.
	 * @param metaData The meta data for the item to register.
	 * @param blockName the name of the block.
	 */
	public static void regItem(Item item, int metaData, String blockName)
	{
		ModelResourceLocation location = new ModelResourceLocation(blockName, "inventory");
		//System.out.println("Registering Item: " + location.getResourceDomain() + "[" + location.getResourcePath() + "]");
		
		if (!(item instanceof ItemBasicStructure))
		{
			ModelLoader.setCustomModelResourceLocation(item, metaData, location);
		}
		else
		{
			ArrayList<ResourceLocation> names = new ArrayList<ResourceLocation>();
			
			for (EnumBasicStructureName value : EnumBasicStructureName.values())
			{
				if (value.getResourceLocation() != null)
				{
					names.add(value.getResourceLocation());
				}
			}
			
			ResourceLocation[] resources = new ResourceLocation[names.size()];
			resources = names.toArray(resources);
			
			ModelLoader.registerItemVariants(item, resources);
		}
	}

	/**
	 * Registers a block to be rendered. This is needed for textures.
	 * @param block The block to register.
	 */
	public static void regBlock(Block block)
	{
		NonNullList<ItemStack> stacks = NonNullList.create();
		
		Item itemBlock = Item.getItemFromBlock(block);
		
		// If there are sub-blocks for this block, register each of them.
		block.getSubBlocks(itemBlock, null, stacks);
		
		if (itemBlock != null)
		{
			if (stacks.size() > 0)
			{
				for (ItemStack stack : stacks)
				{
					Block subBlock = block.getStateFromMeta(stack.getMetadata()).getBlock();
					String name = "";
					
					if (block instanceof IMetaBlock)
					{
						name = "prefab:" + ((IMetaBlock)block).getMetaDataUnLocalizedName(stack.getMetadata());
					}
					else
					{
						name = subBlock.getRegistryName().toString();
					}
					
					ItemRenderRegister.regItem(stack.getItem(), stack.getMetadata(), name);
				}
			}
			else
			{
				ItemRenderRegister.regItem(itemBlock);
			}
		}
	}
}
