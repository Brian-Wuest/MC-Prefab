package com.wuest.prefab.Structures.Predefined;

import com.wuest.prefab.Structures.Base.BuildBlock;
import com.wuest.prefab.Structures.Base.BuildClear;
import com.wuest.prefab.Structures.Base.Structure;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * @author WuestMan
 */
public class StructureChickenCoop extends Structure {
    public static final String ASSETLOCATION = "assets/prefab/structures/chickencoop.zip";
    private BlockPos chickenCoopBlockPos = null;

    public static void ScanStructure(World world, BlockPos originalPos, Direction playerFacing) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(Direction.SOUTH);
        clearedSpace.getShape().setHeight(7);
        clearedSpace.getShape().setLength(5);
        clearedSpace.getShape().setWidth(12);
        clearedSpace.getStartingPosition().setSouthOffset(1);
        clearedSpace.getStartingPosition().setEastOffset(9);

        Structure.ScanStructure(
                world,
                originalPos,
                originalPos.east(10).south(),
                originalPos.south(6).west(3).up(7),
                "..\\src\\main\\resources\\assets\\prefab\\structures\\chickencoop.zip",
                clearedSpace,
                playerFacing, false, false);
    }

    @Override
    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
                                                   Direction assumedNorth, Block foundBlock, BlockState blockState, PlayerEntity player) {
        if (foundBlock instanceof FenceGateBlock && this.chickenCoopBlockPos == null) {
            chickenCoopBlockPos = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing).offset(configuration.houseFacing.getOpposite(), 2);
        }

        return false;
    }

    /**
     * This method is used after the main building is build for any additional structures or modifications.
     *
     * @param configuration The structure configuration.
     * @param world         The current world.
     * @param originalPos   The original position clicked on.
     * @param assumedNorth  The assumed northern direction.
     * @param player        The player which initiated the construction.
     */
    @Override
    public void AfterBuilding(StructureConfiguration configuration, ServerWorld world, BlockPos originalPos, Direction assumedNorth, PlayerEntity player) {
        if (this.chickenCoopBlockPos != null) {
            // For the chicken coop, spawn 1 chicken above a hay bale.
            for (int i = 0; i < 1; i++) {
                ChickenEntity entity = new ChickenEntity(EntityType.CHICKEN, world);
                entity.setPosition(this.chickenCoopBlockPos.getX(), this.chickenCoopBlockPos.getY(), this.chickenCoopBlockPos.getZ());
                world.addEntity(entity);
            }

            this.chickenCoopBlockPos = null;
        }
    }
}