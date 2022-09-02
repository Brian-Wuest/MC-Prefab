package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.structures.base.BuildBlock;
import com.wuest.prefab.structures.base.BuildClear;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.InstantBridgeConfiguration;
import com.wuest.prefab.structures.config.StructureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
public class StructureInstantBridge extends Structure {
    /*
     * Initializes a new instance of the StructureInstantBridge class.
     */
    public StructureInstantBridge() {
        super();
    }

    /**
     * Creates an instance of the structure after reading from a resource location and converting it from JSON.
     *
     * @return A new instance of this class.
     */
    public static StructureInstantBridge CreateInstance() {
        return new StructureInstantBridge();
    }

    /**
     * This is the main building method for this structure.
     *
     * @param configuration The configuration the user updated.
     * @param world         The current world.
     * @param originalPos   The block the user clicked on.
     * @param player        The player requesting the structure.
     * @return True if the build can occur, otherwise false.
     */
    @Override
    public boolean BuildStructure(StructureConfiguration configuration, ServerLevel world, BlockPos originalPos, Player player) {
        InstantBridgeConfiguration specificConfig = (InstantBridgeConfiguration) configuration;
        this.setupClearSpace(specificConfig);

        this.setupStructure(specificConfig, originalPos);

        return super.BuildStructure(specificConfig, world, originalPos, player);
    }

    public void setupStructure(InstantBridgeConfiguration configuration, BlockPos originalPos) {
        ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
        BlockState materialState = configuration.bridgeMaterial.getBlockType();
        BlockState wallBlockState = configuration.bridgeMaterial.getWallBlock();
        Direction facing = Direction.SOUTH;

        BlockState torchState = Blocks.TORCH.defaultBlockState();
        BlockState glassState = Blocks.GLASS_PANE.defaultBlockState();

        int interiorHeightOffSet = configuration.interiorHeight - 3;

        for (int i = 1; i <= configuration.bridgeLength; i++) {
            BlockPos currentPos = originalPos.relative(facing, i);

            // Place the floor
            buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.relative(facing.getCounterClockWise(), 2), originalPos));
            buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.relative(facing.getCounterClockWise()), originalPos));
            buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos, originalPos));
            buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.relative(facing.getClockWise()), originalPos));
            buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.relative(facing.getClockWise(), 2), originalPos));

            // Build the walls.
            buildingBlocks.add(Structure.createBuildBlockFromBlockState(wallBlockState, wallBlockState.getBlock(), currentPos.relative(facing.getCounterClockWise(), 2).above(), originalPos));
            buildingBlocks.add(Structure.createBuildBlockFromBlockState(wallBlockState, wallBlockState.getBlock(), currentPos.relative(facing.getClockWise(), 2).above(), originalPos));

            if (configuration.includeRoof) {
                // Build the roof.
                buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(),
                        currentPos.relative(facing.getCounterClockWise(), 2).above(3 + interiorHeightOffSet), originalPos));
                buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(),
                        currentPos.relative(facing.getCounterClockWise()).above(4 + interiorHeightOffSet), originalPos));
                buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.above(4 + interiorHeightOffSet), originalPos));
                buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(),
                        currentPos.relative(facing.getClockWise()).above(4 + interiorHeightOffSet), originalPos));
                buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(),
                        currentPos.relative(facing.getClockWise(), 2).above(3 + interiorHeightOffSet), originalPos));
            }

            for (int j = 0; j <= interiorHeightOffSet; j++) {
                if ((i == 1 || i % 6 == 0) && j == 0) {
                    // Place torches.
                    buildingBlocks.add(Structure.createBuildBlockFromBlockState(torchState, torchState.getBlock(), currentPos.relative(facing.getCounterClockWise(), 2).above(2), originalPos));
                    buildingBlocks.add(Structure.createBuildBlockFromBlockState(torchState, torchState.getBlock(), currentPos.relative(facing.getClockWise(), 2).above(2), originalPos));
                } else if (configuration.includeRoof) {
                    // Place Glass panes
                    buildingBlocks
                            .add(Structure.createBuildBlockFromBlockState(glassState, glassState.getBlock(), currentPos.relative(facing.getCounterClockWise(), 2).above(2 + j), originalPos));
                    buildingBlocks.add(Structure.createBuildBlockFromBlockState(glassState, glassState.getBlock(), currentPos.relative(facing.getClockWise(), 2).above(2 + j), originalPos));
                }
            }
        }

        this.setBlocks(buildingBlocks);
    }

    private void setupClearSpace(InstantBridgeConfiguration configuration) {
        int clearHeight = 3;

        if (configuration.includeRoof) {
            clearHeight = (configuration.interiorHeight - clearHeight) + clearHeight + 2;
        }

        BuildClear clear = new BuildClear();
        clear.getStartingPosition().setSouthOffset(1);
        clear.getStartingPosition().setEastOffset(2);
        clear.getShape().setDirection(Direction.SOUTH);
        clear.getShape().setHeight(clearHeight - 1);
        clear.getShape().setWidth(5);
        clear.getShape().setLength(configuration.bridgeLength);

        this.setClearSpace(clear);
    }
}