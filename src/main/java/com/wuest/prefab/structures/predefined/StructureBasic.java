package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.structures.base.BuildBlock;
import com.wuest.prefab.structures.base.BuildingMethods;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.BasicStructureConfiguration;
import com.wuest.prefab.structures.config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.structures.config.StructureConfiguration;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * This is the basic structure to be used for structures which don't need a lot of configuration or a custom player created structures.
 *
 * @author WuestMan
 */
public class StructureBasic extends Structure {
    private final ArrayList<Tuple<BlockPos, BlockPos>> bedPositions = new ArrayList<>();
    private final ArrayList<BlockPos> mobSpawnerPos = new ArrayList<>();
    BlockPos customBlockPos = null;
    private BlockPos signPosition = null;

    @Override
    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
                                                   EnumFacing assumedNorth, Block foundBlock, IBlockState blockState, EntityPlayer player) {
        BasicStructureConfiguration config = (BasicStructureConfiguration) configuration;
        EnumBasicStructureName enumName = config.basicStructureName;

        if (foundBlock instanceof BlockHopper && enumName.getName().equals(EnumBasicStructureName.AdvancedCoop.getName())) {
            customBlockPos = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock instanceof BlockTrapDoor && enumName.getName().equals(EnumBasicStructureName.MineshaftEntrance.getName())) {
            customBlockPos = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock instanceof BlockBed && config.chosenOption.getHasBedColor()) {
            // Even if a structure has a bed; we may want to keep a specific color to match what the design of the structure is.
            BlockPos bedHeadPosition = block.getStartingPosition().getRelativePosition(originalPos, this.getClearSpace().getShape().getDirection(), configuration.houseFacing);
            BlockPos bedFootPosition = block.getSubBlock().getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);

            this.bedPositions.add(new Tuple<>(bedHeadPosition, bedFootPosition));

            return true;
        } else if (foundBlock instanceof BlockMobSpawner && enumName.getName().equals(EnumBasicStructureName.MonsterMasher.getName())
                && Prefab.proxy.getServerConfiguration().includeSpawnersInMasher) {
            this.mobSpawnerPos.add(block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing));
        } else if (foundBlock instanceof BlockSign && enumName.getName().equals(EnumBasicStructureName.MonsterMasher.getName())) {
            this.signPosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        }

        ResourceLocation blockIdentifier = foundBlock.getRegistryName();
        ResourceLocation glassIdentifier = Blocks.STAINED_GLASS.getRegistryName();
        ResourceLocation glassPaneIdentifier = Blocks.STAINED_GLASS_PANE.getRegistryName();

        if (blockIdentifier.getNamespace().equals(glassIdentifier.getNamespace())
                && blockIdentifier.getPath().endsWith("glass")
                && config.chosenOption.getHasGlassColor()) {
            blockState = this.getStainedGlassBlock(config.glassColor);
            block.setBlockState(blockState);
            this.priorityOneBlocks.add(block);

            return true;
        } else if (blockIdentifier.getNamespace().equals(glassPaneIdentifier.getNamespace())
                && blockIdentifier.getPath().endsWith("glass_pane")
                && config.chosenOption.getHasGlassColor()) {
            blockState = this.getStainedGlassPaneBlock(config.glassColor);

            BuildBlock.SetBlockState(
                    configuration,
                    world,
                    originalPos,
                    assumedNorth,
                    block,
                    foundBlock,
                    blockState,
                    this);

            this.priorityOneBlocks.add(block);
            return true;
        }

        return false;
    }

    /**
     * This method is used after the main building is build for any additional
     * structures or modifications.
     *
     * @param configuration The structure configuration.
     * @param world         The current world.
     * @param originalPos   The original position clicked on.
     * @param assumedNorth  The assumed northern direction.
     * @param player        The player which initiated the construction.
     */
    @Override
    public void AfterBuilding(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player) {
        BasicStructureConfiguration config = (BasicStructureConfiguration) configuration;
        EnumBasicStructureName enumName = config.basicStructureName;

        if (this.customBlockPos != null) {
            if (enumName.getName().equals(EnumBasicStructureName.AdvancedCoop.getName())) {
                // For the advanced chicken coop, spawn 4 chickens above the hopper.
                for (int i = 0; i < 4; i++) {
                    EntityChicken entity = new EntityChicken(world);
                    entity.setPosition(this.customBlockPos.getX(), this.customBlockPos.up().getY(), this.customBlockPos.getZ());
                    world.spawnEntity(entity);
                }
            } else if (enumName.getName().equals(EnumBasicStructureName.ChickenCoop.getName())) {
                // For the chicken coop, spawn 4 chickens above the hopper.
                EntityChicken entity = new EntityChicken(world);
                entity.setPosition(this.customBlockPos.getX(), this.customBlockPos.down().getY(), this.customBlockPos.getZ());
                world.spawnEntity(entity);
            } else if (enumName.getName().equals(EnumBasicStructureName.MineshaftEntrance.getName())) {
                // Build the mineshaft where the trap door exists.
                StructureAlternateStart.PlaceMineShaft(world, this.customBlockPos.down(), configuration.houseFacing, true);
            }

            this.customBlockPos = null;
        }

        if (enumName.getName().equals(EnumBasicStructureName.MonsterMasher.getName())) {
            int monstersPlaced = 0;

            // Set the spawner.
            for (BlockPos pos : this.mobSpawnerPos) {
                TileEntity tileEntity = world.getTileEntity(pos);

                if (tileEntity instanceof TileEntityMobSpawner) {
                    TileEntityMobSpawner spawner = (TileEntityMobSpawner) tileEntity;

                    switch (monstersPlaced) {
                        case 0: {
                            // Zombie.
                            spawner.getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntityZombie.class));
                            break;
                        }

                        case 1: {
                            // Skeleton.
                            spawner.getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntitySkeleton.class));
                            break;
                        }

                        case 2: {
                            // Spider.
                            spawner.getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntitySpider.class));
                            break;
                        }

                        default: {
                            // Creeper.
                            spawner.getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntityCreeper.class));
                            break;
                        }
                    }

                    monstersPlaced++;
                }
            }

            if (this.signPosition != null) {
                TileEntity tileEntity = world.getTileEntity(this.signPosition);

                if (tileEntity instanceof TileEntitySign) {
                    TileEntitySign signTile = (TileEntitySign) tileEntity;
                    signTile.signText[0] = new TextComponentString("Lamp On=Mobs");

                    signTile.signText[2] = new TextComponentString("Lamp Off=No Mobs");
                }
            }
        }

        if (this.bedPositions.size() > 0) {
            for (Tuple<BlockPos, BlockPos> bedPosition : this.bedPositions) {
                BuildingMethods.PlaceColoredBed(world, bedPosition.getFirst(), bedPosition.getSecond(), config.bedColor);
            }
        }

        if (config.basicStructureName.getName().equals(EnumBasicStructureName.AquaBase.getName())) {
            // Replace the entrance area with air blocks.
            BlockPos airPos = originalPos.up(4).offset(configuration.houseFacing.getOpposite(), 1);

            // This is the first wall.
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
            world.setBlockToAir(airPos);
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));

            airPos = airPos.down();
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
            world.setBlockToAir(airPos);
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));

            airPos = airPos.down();
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
            world.setBlockToAir(airPos);
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));

            airPos = airPos.down();
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
            world.setBlockToAir(airPos);
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));

            // Second part of the wall.
            airPos = airPos.offset(configuration.houseFacing.getOpposite()).up();
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
            world.setBlockToAir(airPos);
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));

            airPos = airPos.up();
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
            world.setBlockToAir(airPos);
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));

            airPos = airPos.up();
            world.setBlockToAir(airPos);
        }
    }
}
