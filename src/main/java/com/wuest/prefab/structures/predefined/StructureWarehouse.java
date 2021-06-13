package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.structures.base.BuildBlock;
import com.wuest.prefab.structures.base.BuildClear;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.StructureConfiguration;
import com.wuest.prefab.structures.config.WareHouseConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
public class StructureWarehouse extends Structure {
    public static final String ASSETLOCATION = "assets/prefab/structures/warehouse.zip";
    public static final String ADVANCED_ASSET_LOCATION = "assets/prefab/structures/advanced_warehouse.zip";

    public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing, boolean advanced) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
        clearedSpace.getShape().setHeight(16);
        clearedSpace.getShape().setLength(16);
        clearedSpace.getShape().setWidth(16);
        clearedSpace.getStartingPosition().setEastOffset(7);
        clearedSpace.getStartingPosition().setHeightOffset(-5);
        clearedSpace.getStartingPosition().setSouthOffset(1);

        String fileLocation = "..\\src\\main\\resources\\assets\\prefab\\structures\\warehouse.zip";

        if (advanced) {
            fileLocation = "..\\src\\main\\resources\\assets\\prefab\\structures\\advanced_warehouse.zip";
        }

        Structure.ScanStructure(
                world,
                originalPos,
                originalPos.east(7).south(1).down(5),
                originalPos.west(8).south(16).up(10),
                fileLocation,
                clearedSpace,
                playerFacing, false, false);
    }

    @Override
    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos, EnumFacing assumedNorth,
                                                   Block foundBlock, IBlockState blockState, EntityPlayer player) {
        if (foundBlock.getRegistryName().getNamespace().equals(Blocks.STAINED_GLASS.getRegistryName().getNamespace())
                && foundBlock.getRegistryName().getPath().equals(Blocks.STAINED_GLASS.getRegistryName().getPath())) {
            WareHouseConfiguration wareHouseConfiguration = (WareHouseConfiguration) configuration;

            blockState = this.getStainedGlassBlock(wareHouseConfiguration.dyeColor);
            block.setBlockState(blockState);
            this.priorityOneBlocks.add(block);

            return true;
        }

        return false;
    }
}