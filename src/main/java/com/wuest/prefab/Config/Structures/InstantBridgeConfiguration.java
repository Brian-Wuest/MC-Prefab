package com.wuest.prefab.Config.Structures;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Items.Structures.ItemInstantBridge;
import com.wuest.prefab.StructureGen.CustomStructures.StructureChickenCoop;
import com.wuest.prefab.StructureGen.CustomStructures.StructureInstantBridge;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class InstantBridgeConfiguration extends StructureConfiguration
{
	/**
	 * Determines how long this bridge is.
	 */
	public int bridgeLength;
	
	/**
	 * Determines the type of material to build the bridge with.
	 */
	public EnumBridgeMaterial bridgeMaterial;
	
	/**
	 * Determine is a roof is included.
	 */
	public boolean includeRoof;
	
	/**
	 * Initializes any properties for this class.
	 */
	@Override
	public void Initialize()
	{
		super.Initialize();
		this.bridgeLength = 25;
		this.bridgeMaterial = EnumBridgeMaterial.Cobblestone;
		this.includeRoof = true;
	}
	
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	@Override
	public InstantBridgeConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		InstantBridgeConfiguration config = new InstantBridgeConfiguration();
		
		return (InstantBridgeConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
	
	@Override
	protected void ConfigurationSpecificBuildStructure(EntityPlayer player, World world, BlockPos hitBlockPos)
	{
		StructureInstantBridge structure = StructureInstantBridge.CreateInstance();
		
		if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player))
		{
			ItemStack usedItemStack = player.getHeldItemMainhand();
			
			if (!(usedItemStack.getItem() instanceof ItemInstantBridge))
			{
				usedItemStack = player.getHeldItemOffhand();
			}
			
			usedItemStack.damageItem(1, player);
			player.inventoryContainer.detectAndSendChanges();
		}
	}
	
	/**
	 * Custom method which can be overridden to write custom properties to the tag.
	 * @param tag The NBTTagCompound to write the custom properties too.
	 * @return The updated tag.
	 */
	@Override
	protected NBTTagCompound CustomWriteToNBTTagCompound(NBTTagCompound tag)
	{
		tag.setInteger("bridgeLength", this.bridgeLength);
		tag.setInteger("bridgeMaterial", this.bridgeMaterial.getNumber());
		tag.setBoolean("includeRoof", this.includeRoof);
		return tag;
	}	
	
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @param config The configuration to read the settings into.
	 */
	@Override
	protected void CustomReadFromNBTTag(NBTTagCompound messageTag, StructureConfiguration config)
	{
		if (messageTag.hasKey("bridgeLength"))
		{
			((InstantBridgeConfiguration)config).bridgeLength = messageTag.getInteger("bridgeLength");
		}
		
		if (messageTag.hasKey("bridgeMaterial"))
		{
			((InstantBridgeConfiguration)config).bridgeMaterial = EnumBridgeMaterial.getMaterialByNumber(messageTag.getInteger("bridgeMaterial"));
		}
		
		if (messageTag.hasKey("includeRoof"))
		{
			((InstantBridgeConfiguration)config).includeRoof = messageTag.getBoolean("includeRoof");
		}
	}

	/**
	 * This enum is used to list the names of the bridge materials.
	 * @author WuestMan
	 *
	 */
	public enum EnumBridgeMaterial
	{
		Cobblestone("prefab.gui.material.cobble_stone", Blocks.COBBLESTONE.getDefaultState(), 0),
		Stone("prefab.gui.material.stone", Blocks.STONE.getDefaultState(), 1),
		StoneBrick("prefab.gui.material.stone_brick", Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.EnumType.DEFAULT.getMetadata()), 2),
		Brick("prefab.gui.material.brick", Blocks.BRICK_BLOCK.getDefaultState(), 3),
		ChiseledStone("prefab.gui.material.chiseled_stone", Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.EnumType.CHISELED.getMetadata()), 4),
		Granite("prefab.gui.material.granite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.GRANITE.getMetadata()), 5),
		SmoothGranite("prefab.gui.material.smooth_granite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.GRANITE_SMOOTH.getMetadata()), 6),
		Andesite("prefab.gui.material.andesite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.ANDESITE.getMetadata()), 7),
		SmoothAndesite("prefab.gui.material.smooth_andesite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata()), 8),
		Diorite("prefab.gui.material.diorite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.DIORITE.getMetadata()), 9),
		SmoothDiorite("prefab.gui.material.smooth_diorite", Blocks.STONE.getStateFromMeta(BlockStone.EnumType.DIORITE_SMOOTH.getMetadata()), 10),
		Oak("prefab.wall.block.type.oak", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.OAK.getMetadata()), 11),
		Spruce("prefab.wall.block.type.spruce", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), 12),
		Birch("prefab.wall.block.type.birch", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.BIRCH.getMetadata()), 13),
		Jungle("prefab.wall.block.type.jungle", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.JUNGLE.getMetadata()), 14),
		Acacia("prefab.wall.block.type.acacia", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.ACACIA.getMetadata()), 15),
		DarkOak("prefab.wall.block.type.darkoak", Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.DARK_OAK.getMetadata()), 16),;
		
		private String name;
		private IBlockState blockType;
		private int number;
		
		private EnumBridgeMaterial(String name, IBlockState blockType, int number)
		{
			this.name = name;
			this.blockType = blockType;
			this.number = number;
		}
		
		public String getName()
		{
			return this.name;
		}
		
		public IBlockState getBlockType()
		{
			return this.blockType;
		}
		
		public int getNumber()
		{
			return this.number;
		}
		
		public String getTranslatedName()
		{
			return GuiLangKeys.translateString(this.name);
		}
		
		public static EnumBridgeMaterial getMaterialByNumber(int number)
		{
			for (EnumBridgeMaterial material : EnumBridgeMaterial.values())
			{
				if (material.getNumber() == number)
				{
					return material;
				}
			}
			
			return EnumBridgeMaterial.Cobblestone;
		}
	}
}
