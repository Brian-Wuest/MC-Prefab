package com.wuest.prefab.structures.gui;

import com.wuest.prefab.structures.base.BuildBlock;
import com.wuest.prefab.structures.base.BuildClear;
import com.wuest.prefab.structures.base.BuildShape;
import com.wuest.prefab.structures.config.StructureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class StructureGuiWorld implements BlockAndTintGetter {
    private BuildClear clearShape;
    private ArrayList<BuildBlock> blocks;
    private HashMap<Long, BlockState> blocksByPosition;
    private BuildShape buildShape;
    private StructureConfiguration structureConfiguration;

    /**
     * Initializes a new instance of the {@link StructureGuiWorld} class.
     */
    public StructureGuiWorld() {
        this.resetStructure();
    }

    /**
     * Resets the structure information to their initial values.
     */
    public void resetStructure() {
        this.clearShape = null;
        this.blocks = null;
        this.blocksByPosition = new HashMap<>();
        this.buildShape = null;
        this.structureConfiguration = null;
    }

    /**
     * Gets the clear shape for this class.
     *
     * @return The build clear object.
     */
    public BuildClear getClearShape() {
        return this.clearShape;
    }

    /**
     * Sets the clear shape.
     *
     * @param clearShape The clear shape to set for the class.
     * @return The updated instance of this class.
     */
    public StructureGuiWorld setClearShape(BuildClear clearShape) {
        this.clearShape = clearShape;
        this.buildShape = clearShape.getShape();

        return this;
    }

    /**
     * Gets the blocks assigned to the class.
     *
     * @return The collection of blocks to render.
     */
    public ArrayList<BuildBlock> getBlocks() {
        return this.blocks;
    }

    /**
     * Sets the blocks for this class.
     *
     * @param blocks The blocks.
     * @return The updated instance of this class.
     */
    public StructureGuiWorld setBlocks(ArrayList<BuildBlock> blocks) {
        this.blocks = blocks;

        return this;
    }

    /**
     * Sets the structure configuration for this class.
     *
     * @param configuration The configuration object.
     * @return The updated instance of this class.
     */
    public StructureGuiWorld setStructureConfiguration(StructureConfiguration configuration) {
        this.structureConfiguration = configuration;

        return this;
    }

    /**
     * Determines if this class has blocks set up to render.
     *
     * @return True if there are blocks to render; otherwise false.
     */
    public boolean hasBlocksToRender() {
        return this.blocksByPosition != null && this.blocksByPosition.size() > 0;
    }

    /**
     * Gets the blocks by position collection.
     *
     * @return The collection of blocks and their positions.
     */
    public HashMap<Long, BlockState> getBlocksByPosition() {
        return this.blocksByPosition;
    }

    /**
     * Call this function after setting the clear shape and the blocks for the class.
     */
    public void setupBlocks() {
        if (this.buildShape != null && this.blocks != null && this.blocks.size() > 0) {
            BlockPos startPosition = new BlockPos(0, 0, 0);
            this.blocksByPosition.clear();
            HashMap<Long, BlockState> blocksByPosition = new HashMap<>();

            for (BuildBlock buildBlock : this.blocks) {
                Block foundBlock = Registry.BLOCK.get(buildBlock.getResourceLocation());

                if (foundBlock != null) {
                    // In order to get the proper relative position I also need the structure's original facing.
                    BlockPos pos = buildBlock.getStartingPosition().getRelativePosition(
                            startPosition,
                            this.buildShape.getDirection(),
                            structureConfiguration.houseFacing);

                    // Get the unique block state for this block.
                    BlockState blockState = foundBlock.defaultBlockState();
                    BuildBlock updatedBuildBlock = BuildBlock.SetBlockState(
                            this.structureConfiguration,
                            startPosition,
                            buildBlock,
                            foundBlock,
                            blockState,
                            this.buildShape.getDirection());

                    blocksByPosition.put(pos.asLong(), updatedBuildBlock.getBlockState());

                    // If there is a sub block (like for doors), do that now.
                    BuildBlock subBlock = buildBlock.getSubBlock();

                    if (subBlock != null) {
                        // In order to get the proper relative position I also need the structure's original facing.
                        BlockPos subPos = subBlock.getStartingPosition().getRelativePosition(
                                startPosition,
                                this.buildShape.getDirection(),
                                structureConfiguration.houseFacing);

                        // Get the unique block state for this block.
                        BlockState subBlockState = foundBlock.defaultBlockState();
                        BuildBlock updatedSubBuildBlock = BuildBlock.SetBlockState(
                                this.structureConfiguration,
                                startPosition,
                                subBlock,
                                foundBlock,
                                subBlockState,
                                this.buildShape.getDirection());

                        blocksByPosition.put(subPos.asLong(), updatedSubBuildBlock.getBlockState());
                    }
                }
            }

            // Don't do this until the end when we're ready to render the blocks.
            this.blocksByPosition = blocksByPosition;
        }
    }

    @Override
    public float getShade(Direction direction, boolean shaded) {
        return 1.0F;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return null;
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver color) {
        return color.getColor(ForgeRegistries.BIOMES.getValue(Biomes.PLAINS.registry()), pos.getX(), pos.getZ());
    }

    @Override
    public int getBrightness(LightLayer type, BlockPos pos) {
        return 15;
    }

    @Override
    public int getRawBrightness(BlockPos pos, int ambientDarkening) {
        return 15 - ambientDarkening;
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return null;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if (x < 0 || y < 0 || z < 0 || x >= this.buildShape.getWidth() || y >= this.buildShape.getHeight() || z >= this.buildShape.getLength()) {
            return Blocks.AIR.defaultBlockState();
        }

        return this.blocksByPosition.getOrDefault(pos.asLong(), Blocks.AIR.defaultBlockState());
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public int getHeight() {
        return 255;
    }

    @Override
    public int getMinBuildHeight() {
        return 0;
    }
}
