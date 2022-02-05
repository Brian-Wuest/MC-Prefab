package com.wuest.prefab.structures.base;

import com.google.gson.annotations.Expose;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.config.StructureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.material.FluidState;

import java.util.*;

/**
 * This class defines a single block and where it will be in the structure.
 *
 * @author WuestMan
 */
@SuppressWarnings({"SpellCheckingInspection", "UnusedAssignment", "unchecked", "unused", "WeakerAccess"})
public class BuildBlock {
    public BlockPos blockPos;
    @Expose
    private String blockDomain;
    @Expose
    private String blockName;
    @Expose
    private PositionOffset startingPosition;
    @Expose
    private ArrayList<BuildProperty> properties;
    @Expose
    private BuildBlock subBlock;
    @Expose
    private boolean hasFacing;
    @Expose
    private BlockState state;
    @Expose
    private String blockStateData;

    public BuildBlock() {
        this.Initialize();
    }

    public static BuildBlock SetBlockState(StructureConfiguration configuration, BlockPos originalPos, BuildBlock block, Block foundBlock,
                                           BlockState blockState, Direction structureDirection) {
        try {
            if (!block.blockStateData.equals("")) {
                return BuildBlock.SetBlockStateFromTagData(configuration, originalPos, block, foundBlock, blockState, structureDirection);
            }

            Direction vineFacing = BuildBlock.getVineFacing(configuration, foundBlock, block, structureDirection);
            Direction.Axis logFacing = BuildBlock.getBoneFacing(configuration, foundBlock, block, structureDirection);
            Direction.Axis boneFacing = BuildBlock.getBoneFacing(configuration, foundBlock, block, structureDirection);
            Direction leverOrientation = BuildBlock.getLeverOrientation(configuration, foundBlock, block, structureDirection);
            Map<Direction, Boolean> fourWayFacings = BuildBlock.getFourWayBlockFacings(configuration, foundBlock, block, structureDirection);
            Map<Direction, WallSide> wallShapes = BuildBlock.getWallFacings(configuration, foundBlock, block, structureDirection);

            // If this block has custom processing for block state just continue onto the next block. The sub-class is
            // expected to place the block.
            if (block.getProperties().size() > 0) {
                Collection<Property<?>> properties = blockState.getProperties();

                // Go through each property of this block and set it.
                // The state will be updated as the properties are
                // applied.
                for (Property<?> property : properties) {
                    BuildProperty buildProperty = block.getProperty(property.getName());

                    // Make sure that this property exists in our file. The only way it wouldn't be there would be if a
                    // mod (or sometimes vanilla) adds properties to vanilla blocks.
                    if (buildProperty != null) {
                        try {
                            Optional<?> propertyValue = property.getValue(buildProperty.getValue());

                            if (!propertyValue.isPresent()
                                    || propertyValue.getClass().getName().equals("com.google.common.base.Absent")) {
                                Prefab.LOGGER.warn(
                                        "Property value for property name [" + property.getName() + "] for block [" + block.getBlockName() + "] is considered Absent, figure out why.");
                                continue;
                            }

                            Comparable<?> comparable = property.getValueClass().cast(propertyValue.get());

                            comparable = BuildBlock.setComparable(comparable, foundBlock, property, configuration.houseFacing, block, propertyValue, vineFacing, logFacing,
                                    boneFacing, leverOrientation, structureDirection, fourWayFacings, wallShapes);

                            if (comparable == null) {
                                continue;
                            }

                            try {
                                if (blockState.getValue(property) != comparable) {
                                    blockState = BuildBlock.setProperty(blockState, property, comparable);
                                }
                            } catch (Exception ex) {
                                System.out.println("Error setting properly value for property name [" + property.getName() + "] property value [" + buildProperty.getValue()
                                        + "] for block [" + block.getBlockName() + "] The default value will be used.");
                            }
                        } catch (Exception ex) {
                            System.out.println("Error getting properly value for property name [" + property.getName() + "] property value [" + buildProperty.getValue()
                                    + "] for block [" + block.getBlockName() + "]");
                            throw ex;
                        }
                    }
                }
            }

            block.setBlockState(blockState);
            return block;
        } catch (Exception ex) {
            System.out.println("Error setting block state for block [" + block.getBlockName() + "] for structure configuration class [" + configuration.getClass().getName() + "]");
            throw ex;
        }
    }

    private static boolean neighborHaveWater(BlockPos originalPos, Level world) {
        boolean returnValue = false;

        for (Direction direction : Direction.values()) {
            if (direction == Direction.DOWN) {
                continue;
            }

            FluidState fluidState = world.getFluidState(originalPos.relative(direction));

            if (fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8) {
                returnValue = true;
                break;
            }
        }

        return returnValue;
    }

    public static Direction getHorizontalFacing(Direction currentFacing, Direction configurationFacing, Direction structureDirection) {
        if (currentFacing != null && currentFacing != Direction.UP && currentFacing != Direction.DOWN) {
            if (configurationFacing.getOpposite() == structureDirection.getClockWise()) {
                currentFacing = currentFacing.getClockWise();
            } else if (configurationFacing.getOpposite() == structureDirection.getOpposite()) {
                currentFacing = currentFacing.getOpposite();
            } else if (configurationFacing.getOpposite() == structureDirection.getCounterClockWise()) {
                currentFacing = currentFacing.getCounterClockWise();
            }
        }

        return currentFacing;
    }

    @SuppressWarnings({"OptionalGetWithoutIsPresent", "OptionalUsedAsFieldOrParameterType"})
    private static Comparable setComparable(Comparable<?> comparable, Block foundBlock, Property<?> property, Direction houseFacing, BuildBlock block,
                                            Optional<?> propertyValue, Direction vineFacing, Direction.Axis logFacing, Direction.Axis boneFacing, Direction leverOrientation,
                                            Direction structureDirection,
                                            Map<Direction, Boolean> fourWayFacings,
                                            Map<Direction, WallSide> wallShapes) {
        if (property.getName().equals("facing") && !(foundBlock instanceof FaceAttachedHorizontalDirectionalBlock)) {
            // Facing properties should be relative to the configuration facing.
            Direction facing = Direction.byName(propertyValue.get().toString());

            // Cannot rotate verticals.
            facing = BuildBlock.getHorizontalFacing(facing, houseFacing, structureDirection);

            comparable = facing;

            block.setHasFacing(true);
        } else if (property.getName().equals("facing") && foundBlock instanceof FaceAttachedHorizontalDirectionalBlock) {
            comparable = leverOrientation;
            block.setHasFacing(true);
        } else if (property.getName().equals("rotation")) {
            // 0 = South
            // 4 = West
            // 8 = North
            // 12 = East
            int rotation = (Integer) propertyValue.get();
            Direction facing = rotation == 0 ? Direction.SOUTH : rotation == 4 ? Direction.WEST : rotation == 8 ? Direction.NORTH : Direction.EAST;

            if (houseFacing.getOpposite() == structureDirection.getClockWise()) {
                facing = facing.getClockWise();
            } else if (houseFacing.getOpposite() == structureDirection.getOpposite()) {
                facing = facing.getOpposite();
            } else if (houseFacing.getOpposite() == structureDirection.getCounterClockWise()) {
                facing = facing.getCounterClockWise();
            }

            rotation = facing == Direction.SOUTH ? 0 : facing == Direction.WEST ? 4 : facing == Direction.NORTH ? 8 : 12;
            comparable = rotation;
            block.setHasFacing(true);
        } else if (foundBlock instanceof VineBlock) {
            // Vines have a special state. There is 1 property for each "facing".
            if (property.getName().equals(vineFacing.getName())) {
                comparable = true;
                block.setHasFacing(true);
            } else {
                comparable = false;
            }
        } else if (foundBlock instanceof CrossCollisionBlock && !property.getName().equals("waterlogged")) {
            for (Map.Entry<Direction, Boolean> entry : fourWayFacings.entrySet()) {
                if (property.getName().equals(entry.getKey().getName())) {
                    comparable = entry.getValue();
                }
            }
        } else if (foundBlock instanceof WallBlock && !property.getName().equals("waterlogged")) {
            for (Map.Entry<Direction, WallSide> entry : wallShapes.entrySet()) {
                if (property.getName().equals(entry.getKey().toString())) {
                    comparable = entry.getValue();
                }
            }
        } else if (foundBlock instanceof RotatedPillarBlock) {
            // bones have a special state. There is a property called axis and it only has 3 directions.
            if (property.getName().equals("axis")) {
                comparable = boneFacing;
            }
        }

        return comparable;
    }

    private static Direction getVineFacing(StructureConfiguration configuration, Block foundBlock, BuildBlock block, Direction assumedNorth) {
        Direction vineFacing = Direction.UP;

        // Vines have a special property for it's "facing"
        if (foundBlock instanceof VineBlock
                || foundBlock instanceof WallBlock) {
            if (block.getProperty("east").getValue().equals("true")) {
                vineFacing = Direction.EAST;
            } else if (block.getProperty("west").getValue().equals("true")) {
                vineFacing = Direction.WEST;
            } else if (block.getProperty("south").getValue().equals("true")) {
                vineFacing = Direction.SOUTH;
            } else if (block.getProperty("north").getValue().equals("true")) {
                vineFacing = Direction.NORTH;
            }

            if (vineFacing != Direction.UP) {
                if (configuration.houseFacing.getClockWise() == assumedNorth) {
                    vineFacing = vineFacing.getClockWise();
                } else if (configuration.houseFacing.getOpposite() == assumedNorth) {
                } else if (configuration.houseFacing.getCounterClockWise() == assumedNorth) {
                    vineFacing = vineFacing.getCounterClockWise();
                } else {
                    vineFacing = vineFacing.getOpposite();
                }
            }
        }

        return vineFacing;
    }

    private static Map<Direction, WallSide> getWallFacings(StructureConfiguration configuration, Block foundBlock, BuildBlock block, Direction assumedNorth) {
        Map<Direction, WallSide> facings = new HashMap<>();

        if (foundBlock instanceof WallBlock) {
            // Valid states can be any two directions at a time, not just opposites but adjacents as well (for corners).
            WallSide northValue = BuildBlock.getShapeByName(block.getProperty("north").getValue());
            WallSide eastValue = BuildBlock.getShapeByName(block.getProperty("east").getValue());
            WallSide westValue = BuildBlock.getShapeByName(block.getProperty("west").getValue());
            WallSide southValue = BuildBlock.getShapeByName(block.getProperty("south").getValue());
            WallSide originalNorth = northValue;
            WallSide originalEast = eastValue;
            WallSide originalWest = westValue;
            WallSide originalSouth = southValue;

            if (configuration.houseFacing.getClockWise() == assumedNorth) {
                northValue = originalWest;
                eastValue = originalNorth;
                southValue = originalEast;
                westValue = originalSouth;
            } else if (configuration.houseFacing == assumedNorth) {
                northValue = originalSouth;
                eastValue = originalWest;
                southValue = originalNorth;
                westValue = originalEast;
            } else if (configuration.houseFacing.getCounterClockWise() == assumedNorth) {
                northValue = originalEast;
                eastValue = originalSouth;
                southValue = originalWest;
                westValue = originalNorth;
            }

            facings.put(Direction.NORTH, northValue);
            facings.put(Direction.EAST, eastValue);
            facings.put(Direction.WEST, westValue);
            facings.put(Direction.SOUTH, southValue);
        }

        return facings;
    }

    private static Map<Direction, Boolean> getFourWayBlockFacings(StructureConfiguration configuration, Block foundBlock, BuildBlock block, Direction assumedNorth) {
        Map<Direction, Boolean> facings = new HashMap<>();

        if (foundBlock instanceof CrossCollisionBlock) {
            // Valid states can be any two directions at a time, not just opposites but adjacents as well (for corners).
            boolean northValue = Boolean.parseBoolean(block.getProperty("north").getValue());
            boolean eastValue = Boolean.parseBoolean(block.getProperty("east").getValue());
            boolean westValue = Boolean.parseBoolean(block.getProperty("west").getValue());
            boolean southValue = Boolean.parseBoolean(block.getProperty("south").getValue());
            boolean originalNorth = northValue;
            boolean originalEast = eastValue;
            boolean originalWest = westValue;
            boolean originalSouth = southValue;

            if (configuration.houseFacing.getClockWise() == assumedNorth) {
                northValue = originalWest;
                eastValue = originalNorth;
                southValue = originalEast;
                westValue = originalSouth;
            } else if (configuration.houseFacing == assumedNorth) {
                northValue = originalSouth;
                eastValue = originalWest;
                southValue = originalNorth;
                westValue = originalEast;
            } else if (configuration.houseFacing.getCounterClockWise() == assumedNorth) {
                northValue = originalEast;
                eastValue = originalSouth;
                southValue = originalWest;
                westValue = originalNorth;
            }

            facings.put(Direction.NORTH, northValue);
            facings.put(Direction.EAST, eastValue);
            facings.put(Direction.WEST, westValue);
            facings.put(Direction.SOUTH, southValue);
        }

        return facings;
    }

    private static Direction.Axis getBoneFacing(StructureConfiguration configuration, Block foundBlock, BuildBlock block, Direction assumedNorth) {
        Direction.Axis boneFacing = Direction.Axis.X;

        if (foundBlock instanceof RotatedPillarBlock) {
            BuildProperty property = block.getProperty("axis");

            if (property != null) {
                if (property.getValue().equals("x")) {
                } else if (property.getValue().equals("y")) {
                    boneFacing = Direction.Axis.Y;
                } else {
                    boneFacing = Direction.Axis.Z;
                }

                if (boneFacing != Direction.Axis.Y) {
                    boneFacing = configuration.houseFacing == assumedNorth || configuration.houseFacing == assumedNorth.getOpposite()
                            ? boneFacing
                            : boneFacing == Direction.Axis.X
                            ? Direction.Axis.Z
                            : Direction.Axis.X;
                }
            }
        }

        return boneFacing;
    }

    private static Direction getLeverOrientation(StructureConfiguration configuration, Block foundBlock, BuildBlock block, Direction assumedNorth) {
        Direction leverOrientation = Direction.NORTH;
        AttachFace attachedTo = AttachFace.FLOOR;

        if (foundBlock instanceof FaceAttachedHorizontalDirectionalBlock) {
            // Levers have a special facing.
            leverOrientation = LeverBlock.FACING.getValue(block.getProperty("facing").getValue()).get();
            attachedTo = LeverBlock.FACE.getValue(block.getProperty("face").getValue()).get();

            if (attachedTo == AttachFace.FLOOR
                    || attachedTo == AttachFace.CEILING) {
                if (attachedTo == AttachFace.FLOOR) {
                    leverOrientation = configuration.houseFacing == assumedNorth || configuration.houseFacing == assumedNorth.getOpposite()
                            ? leverOrientation
                            : leverOrientation == Direction.NORTH
                            ? Direction.EAST
                            : Direction.NORTH;
                } else {
                    leverOrientation = configuration.houseFacing == assumedNorth || configuration.houseFacing == assumedNorth.getOpposite()
                            ? leverOrientation
                            : leverOrientation == Direction.NORTH
                            ? Direction.EAST
                            : Direction.NORTH;
                }
            } else {
                Direction facing = leverOrientation;

                if (configuration.houseFacing.getClockWise() == assumedNorth) {
                    facing = facing.getClockWise();
                } else if (configuration.houseFacing.getOpposite() == assumedNorth) {
                } else if (configuration.houseFacing.getCounterClockWise() == assumedNorth) {
                    facing = facing.getCounterClockWise();
                } else {
                    facing = facing.getOpposite();
                }

                for (Direction tempOrientation : Direction.values()) {
                    if (tempOrientation == facing) {
                        leverOrientation = tempOrientation;
                        break;
                    }
                }
            }
        }

        return leverOrientation;
    }

    private static BlockState setProperty(BlockState state, Property property, Comparable comparable) {
        // This method is required since the properties and comparables have a <?> in them and it doesn't work properly
        // when that is there. There is a compilation error since it's not hard typed.
        return state.setValue(property, comparable);
    }

    private static BuildBlock SetBlockStateFromTagData(StructureConfiguration configuration, BlockPos originalPos, BuildBlock block,
                                                       Block foundBlock, BlockState blockState, Direction structureDirection) {
        BlockState tagState = block.getBlockStateFromDataTag();

        if (tagState != null) {
            block.setBlockState(block.getBlockStateFromDataTag());
        } else {
            block.setBlockStateData("");
            return BuildBlock.SetBlockState(configuration, originalPos, block, foundBlock, blockState, structureDirection);
        }

        return block;
    }

    public static Direction getDirectionByName(String name) {
        for (Direction direction : Direction.values()) {
            if (direction.name().equalsIgnoreCase(name)) {
                return direction;
            }
        }

        return Direction.NORTH;
    }

    public static WallSide getShapeByName(String name) {
        for (WallSide shape : WallSide.values()) {
            if (shape.toString().equalsIgnoreCase(name)) {
                return shape;
            }
        }

        return WallSide.NONE;
    }

    public String getBlockDomain() {
        return this.blockDomain;
    }

    public void setBlockDomain(String value) {
        this.blockDomain = value;
    }

    public String getBlockName() {
        return this.blockName;
    }

    public void setBlockName(String value) {
        this.blockName = value;
    }

    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(this.blockDomain, this.blockName);
    }

    public PositionOffset getStartingPosition() {
        return this.startingPosition;
    }

    public void setStartingPosition(PositionOffset value) {
        this.startingPosition = value;
    }

    public ArrayList<BuildProperty> getProperties() {
        return this.properties;
    }

    public void setProperties(ArrayList<BuildProperty> value) {
        this.properties = value;
    }

    public BuildProperty getProperty(String name) {
        for (BuildProperty property : this.getProperties()) {
            if (name.equals(property.getName())) {
                return property;
            }
        }

        return null;
    }

    public BuildBlock getSubBlock() {
        return this.subBlock;
    }

    public void setSubBlock(BuildBlock value) {
        this.subBlock = value;
    }

    public boolean getHasFacing() {
        return this.hasFacing;
    }

    public void setHasFacing(boolean value) {
        this.hasFacing = value;
    }

    public BlockState getBlockState() {
        return this.state;
    }

    public void setBlockState(BlockState value) {
        this.state = value;
    }

    public String getBlockStateData() {
        return this.blockStateData;
    }

    public void setBlockStateData(String value) {
        this.blockStateData = value;
    }

    public void setBlockStateData(CompoundTag tagCompound) {
        this.blockStateData = tagCompound.toString();
    }

    public CompoundTag getBlockStateDataTag() {
        CompoundTag tag = null;

        if (!this.blockStateData.equals("")) {
            try {
                tag = TagParser.parseTag(this.blockStateData);
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }

        return tag;
    }

    public BlockState getBlockStateFromDataTag() {
        BlockState state = null;

        if (!this.blockStateData.equals("")) {
            CompoundTag tag = this.getBlockStateDataTag();

            if (tag != null) {
                state = NbtUtils.readBlockState(tag.getCompound("tag"));
            }
        }

        return state;
    }

    public void Initialize() {
        this.blockDomain = "";
        this.blockName = "";
        this.properties = new ArrayList<>();
        this.hasFacing = false;
        this.state = null;
        this.subBlock = null;
        this.startingPosition = new PositionOffset();
        this.blockStateData = "";
    }
}