package com.wuest.prefab.Structures.Predefined;

import java.util.ArrayList;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Base.BuildBlock;
import com.wuest.prefab.Structures.Base.BuildClear;
import com.wuest.prefab.Structures.Base.Structure;
import com.wuest.prefab.Structures.Config.MonsterMasherConfiguration;
import com.wuest.prefab.Structures.Config.StructureConfiguration;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class StructureMonsterMasher extends Structure
{
	public static final String ASSETLOCATION = "assets/prefab/structures/monster_masher.zip";

	private ArrayList<BlockPos> mobSpawnerPos = new ArrayList<BlockPos>();
	private BlockPos signPosition = null;
	
	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(18);
		clearedSpace.getShape().setLength(15);
		clearedSpace.getShape().setWidth(13);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(6);
		
		Structure.ScanStructure(
				world, 
				originalPos, 
				originalPos.east(6).south(), 
				originalPos.south(15).west(6).up(18),
				"..\\src\\main\\resources\\assets\\prefab\\structures\\monster_masher.zip",
				clearedSpace,
				playerFacing, false, false);	
	}
	
	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos, EnumFacing assumedNorth,
			Block foundBlock, IBlockState blockState, EntityPlayer player)
	{
		if (foundBlock.getRegistryName().getResourceDomain().equals(Blocks.STAINED_GLASS.getRegistryName().getResourceDomain())
				&& foundBlock.getRegistryName().getResourcePath().equals(Blocks.STAINED_GLASS.getRegistryName().getResourcePath()))
		{
			MonsterMasherConfiguration wareHouseConfiguration = (MonsterMasherConfiguration)configuration;
			
			blockState = blockState.withProperty(BlockStainedGlass.COLOR, wareHouseConfiguration.dyeColor);
			block.setBlockState(blockState);
			this.priorityOneBlocks.add(block);
			
			return true;
		}
		else if (foundBlock.getRegistryName().getResourceDomain().equals(Blocks.MOB_SPAWNER.getRegistryName().getResourceDomain())
				&& foundBlock.getRegistryName().getResourcePath().equals(Blocks.MOB_SPAWNER.getRegistryName().getResourcePath()))
		{
			if (Prefab.proxy.proxyConfiguration.includeSpawnersInMasher)
			{
				this.mobSpawnerPos.add(block.getStartingPosition().getRelativePosition(
						originalPos, 
						this.getClearSpace().getShape().getDirection(),
						configuration.houseFacing));
			}
			else
			{
				return true;
			}
		}
		else if (foundBlock instanceof BlockSign)
		{
			this.signPosition = block.getStartingPosition().getRelativePosition(
					originalPos, 
					this.getClearSpace().getShape().getDirection(),
					configuration.houseFacing);
		}
		
		return false;
	}
	
	/**
	 * This method is used after the main building is build for any additional structures or modifications.
	 * @param configuration The structure configuration.
	 * @param world The current world.
	 * @param originalPos The original position clicked on.
	 * @param assumedNorth The assumed northern direction.
	 * @param player The player which initiated the construction.
	 */
	@Override
	public void AfterBuilding(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player)
	{
		int monstersPlaced = 0;
		
		// Set the spawner.
		for (BlockPos pos : this.mobSpawnerPos)
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			
			if (tileEntity != null && tileEntity instanceof TileEntityMobSpawner)
			{
				TileEntityMobSpawner spawner = (TileEntityMobSpawner)tileEntity;
				
				switch (monstersPlaced)
				{
					case 0:
					{
						// Zombie.
						spawner.getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntityZombie.class));
						break;
					}
					
					case 1:
					{
						// Skeleton.
						spawner.getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntitySkeleton.class));
						break;
					}
					
					case 2:
					{
						// Spider.
						spawner.getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntitySpider.class));
						break;
					}
					
					default:
					{
						// Creeper.
						spawner.getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntityCreeper.class));
						break;
					}
				}
				
				monstersPlaced++;
			}
		}
		
		if (this.signPosition != null)
		{
			TileEntity tileEntity = world.getTileEntity(this.signPosition);

			if (tileEntity instanceof TileEntitySign)
			{
				TileEntitySign signTile = (TileEntitySign) tileEntity;
				signTile.signText[0] = new TextComponentString("Lamp On=Mobs");

				signTile.signText[2] = new TextComponentString("Lamp Off=No Mobs");
			}
		}
	}
}
