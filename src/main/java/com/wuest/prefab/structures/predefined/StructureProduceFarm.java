package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.structures.base.BuildBlock;
import com.wuest.prefab.structures.base.BuildClear;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.ProduceFarmConfiguration;
import com.wuest.prefab.structures.config.StructureConfiguration;
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
public class StructureProduceFarm extends Structure {
    public static final String ASSETLOCATION = "assets/prefab/structures/producefarm.zip";

    public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
        clearedSpace.getShape().setHeight(9);
        clearedSpace.getShape().setLength(32);
        clearedSpace.getShape().setWidth(32);
        clearedSpace.getStartingPosition().setSouthOffset(1);
        clearedSpace.getStartingPosition().setEastOffset(28);
        clearedSpace.getStartingPosition().setHeightOffset(-1);

        Structure.ScanStructure(
                world,
                originalPos,
                originalPos.east(28).south().down(1),
                originalPos.south(32).west(3).up(9),
                "..\\src\\main\\resources\\assets\\prefab\\structures\\producefarm.zip",
                clearedSpace,
                playerFacing, false, false);
    }

    @Override
    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos, EnumFacing assumedNorth,
                                                   Block foundBlock, IBlockState blockState, EntityPlayer player) {
        if (foundBlock.getRegistryName().getNamespace().equals(Blocks.STAINED_GLASS.getRegistryName().getNamespace())
                && foundBlock.getRegistryName().getPath().equals(Blocks.STAINED_GLASS.getRegistryName().getPath())) {
            ProduceFarmConfiguration wareHouseConfiguration = (ProduceFarmConfiguration) configuration;

            blockState = this.getStainedGlassBlock(wareHouseConfiguration.dyeColor);
            block.setBlockState(blockState);
            //this.placedBlocks.add(block);
            this.priorityOneBlocks.add(block);

            return true;
        }

        return false;
    }
}
