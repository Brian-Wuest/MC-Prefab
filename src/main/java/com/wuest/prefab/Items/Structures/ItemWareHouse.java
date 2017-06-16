package com.wuest.prefab.Items.Structures;

import java.io.*;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.Structures.WareHouseConfiguration;
import com.wuest.prefab.Gui.Structures.GuiWareHouse;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.StructureGen.*;
import com.wuest.prefab.StructureGen.CustomStructures.StructureWarehouse;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * This class is used to build the warehouse structure.
 * @author WuestMan
 *
 */
public class ItemWareHouse extends StructureItem
{
	private WareHouseConfiguration currentConfiguration = null;

	public ItemWareHouse(String name)
	{
		super(name, ModRegistry.GuiWareHouse);
	}
}