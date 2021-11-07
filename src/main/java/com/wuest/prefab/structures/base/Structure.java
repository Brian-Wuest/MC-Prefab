package com.wuest.prefab.structures.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.wuest.prefab.*;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.structures.config.StructureConfiguration;
import com.wuest.prefab.structures.events.StructureEventHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Each structure represents a building which is pre-defined in a JSON file.
 *
 * @author WuestMan
 */
@SuppressWarnings({"unchecked", "WeakerAccess", "ConstantConditions"})
public class Structure {
    public ArrayList<BlockPos> allBlockPositions = new ArrayList<>();
    public ArrayList<BlockPos> clearedBlockPos = new ArrayList<>();
    public ArrayList<BuildBlock> priorityOneBlocks = new ArrayList<>();
    public ArrayList<BuildBlock> airBlocks = new ArrayList<>();
    public StructureConfiguration configuration;
    public ServerLevel world;
    public BlockPos originalPos;
    public Direction assumedNorth;
    public boolean hasAirBlocks = false;
    public boolean entitiesRemoved = false;

    @Expose
    public ArrayList<BuildTileEntity> tileEntities = new ArrayList<>();
    @Expose
    public ArrayList<BuildEntity> entities = new ArrayList<>();
    @Expose
    private String name;
    @Expose
    private BuildClear clearSpace;
    @Expose
    private ArrayList<BuildBlock> blocks;

    public Structure() {
        this.Initialize();
    }

    /**
     * Creates an instance of the structure after reading from a resource location and converting it from JSON.
     *
     * @param <T>              The type which extends Structure.
     * @param resourceLocation The location of the JSON file to load. Example: "assets/prefab/structures/warehouse.json"
     * @param child            The child class which extends Structure.
     * @return Null if the resource wasn't found or the JSON could not be parsed, otherwise the de-serialized object.
     */
    public static <T extends Structure> T CreateInstance(String resourceLocation, Class<? extends Structure> child) {
        T structure;

        Gson file = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        structure = (T) file.fromJson(ZipUtil.decompressResource(resourceLocation), child);

        return structure;
    }

    public static void CreateStructureFile(Structure structure, String fileLocation) {
        try {
            Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            StringWriter stringWriter = new StringWriter();
            converter.toJson(structure, stringWriter);

            ZipUtil.zipStringToFile(stringWriter.toString(), fileLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ScanStructure(Level world, BlockPos originalPos, BlockPos cornerPos1, BlockPos cornerPos2, String fileLocation, BuildClear clearedSpace,
                                     Direction playerFacing, boolean includeAir, boolean excludeWater) {
        Structure scannedStructure = new Structure();
        scannedStructure.setClearSpace(clearedSpace);

        for (BlockPos currentPos : BlockPos.betweenClosed(cornerPos1, cornerPos2)) {
            if (world.isEmptyBlock(currentPos) && !includeAir) {
                continue;
            }

            BlockState currentState = world.getBlockState(currentPos);
            Block currentBlock = currentState.getBlock();

            if (currentState.getMaterial() == Material.WATER && excludeWater) {
                continue;
            }

            BuildBlock buildBlock = Structure.createBuildBlockFromBlockState(currentState, currentBlock, currentPos, originalPos);

            if (currentBlock instanceof DoorBlock) {
                DoubleBlockHalf blockHalf = currentState.getValue(DoorBlock.HALF);

                if (blockHalf == DoubleBlockHalf.LOWER) {
                    BlockState upperHalfState = world.getBlockState(currentPos.above());

                    if (upperHalfState.getBlock() instanceof DoorBlock) {
                        Block upperBlock = upperHalfState.getBlock();
                        BuildBlock upperHalf = Structure.createBuildBlockFromBlockState(upperHalfState, upperBlock, currentPos.above(), originalPos);

                        buildBlock.setSubBlock(upperHalf);
                    }
                } else {
                    // Don't process upper door halves. These were already done.
                    continue;
                }
            } else if (currentBlock instanceof BedBlock) {
                BedPart bedPart = currentState.getValue(BedBlock.PART);

                if (bedPart == BedPart.HEAD) {
                    BlockState bedFoot = null;
                    boolean foundFoot = false;
                    Direction facing = Direction.NORTH;

                    while (!foundFoot) {
                        bedFoot = world.getBlockState(currentPos.relative(facing));

                        if (bedFoot.getBlock() instanceof BedBlock && bedFoot.getValue(BedBlock.PART) == BedPart.FOOT) {
                            foundFoot = true;
                            break;
                        }

                        facing = facing.getClockWise();

                        if (facing == Direction.NORTH) {
                            // Got back to north, break out to avoid infinite loop.
                            break;
                        }
                    }

                    if (foundFoot) {
                        Block footBedBlock = bedFoot.getBlock();
                        BuildBlock bed = Structure.createBuildBlockFromBlockState(bedFoot, footBedBlock, currentPos.relative(facing), originalPos);
                        buildBlock.setSubBlock(bed);
                    }
                } else {
                    // Don't process foot of bed, it was already done.
                    continue;
                }
            }

            scannedStructure.getBlocks().add(buildBlock);

            BlockEntity tileEntity = world.getBlockEntity(currentPos);

            if (tileEntity != null) {
                // Don't write data for empty tile entities.
                if ((tileEntity instanceof ChestBlockEntity && ((ChestBlockEntity) tileEntity).isEmpty())
                        || (tileEntity instanceof FurnaceBlockEntity && ((FurnaceBlockEntity) tileEntity).isEmpty())) {
                    continue;
                }

                ResourceLocation resourceLocation = ForgeRegistries.BLOCK_ENTITIES.getKey(tileEntity.getType());
                CompoundTag tagCompound = new CompoundTag();
                tileEntity.save(tagCompound);

                BuildTileEntity buildTileEntity = new BuildTileEntity();
                assert resourceLocation != null;
                buildTileEntity.setEntityDomain(resourceLocation.getNamespace());
                buildTileEntity.setEntityName(resourceLocation.getPath());
                buildTileEntity.setStartingPosition(Structure.getStartingPositionFromOriginalAndCurrentPosition(currentPos, originalPos));
                buildTileEntity.setEntityNBTData(tagCompound);
                scannedStructure.tileEntities.add(buildTileEntity);
            }
        }

        int x_radiusRangeBegin = Math.min(cornerPos1.getX(), cornerPos2.getX());
        int x_radiusRangeEnd = Math.max(cornerPos1.getX(), cornerPos2.getX());
        int y_radiusRangeBegin = Math.min(cornerPos1.getY(), cornerPos2.getY());
        int y_radiusRangeEnd = Math.max(cornerPos1.getY(), cornerPos2.getY());
        int z_radiusRangeBegin = Math.min(cornerPos1.getZ(), cornerPos2.getZ());
        int z_radiusRangeEnd = Math.max(cornerPos1.getZ(), cornerPos2.getZ());

        AABB axis = new AABB(cornerPos1, cornerPos2);

        for (Entity entity : world.getEntities(null, axis)) {
            // TODO: This was the "getPosition" method.
            BlockPos entityPos = entity.blockPosition();

            if (entity instanceof HangingEntity) {
                // Use the HangingEntity getPos function instead since it is more accurate for itemframes and paintings.
                entityPos = ((HangingEntity) entity).getPos();
            }

            if (entityPos.getX() >= x_radiusRangeBegin && entityPos.getX() <= x_radiusRangeEnd
                    && entityPos.getZ() >= z_radiusRangeBegin && entityPos.getZ() <= z_radiusRangeEnd
                    && entityPos.getY() >= y_radiusRangeBegin && entityPos.getY() <= y_radiusRangeEnd) {
                BuildEntity buildEntity = new BuildEntity();
                buildEntity.setEntityResourceString(ForgeRegistries.ENTITIES.getKey(entity.getType()));
                buildEntity.setStartingPosition(Structure.getStartingPositionFromOriginalAndCurrentPosition(entityPos, originalPos));

                // The function calls below get the following fields from the "entity" class. posX, posY, posZ.
                // This will probably have to change when the mappings get updated.
                buildEntity.entityXAxisOffset = entityPos.getX() - entity.getX();
                buildEntity.entityYAxisOffset = entityPos.getY() - entity.getY();
                buildEntity.entityZAxisOffset = entityPos.getZ() - entity.getZ();

                if (entity instanceof ItemFrame) {
                    buildEntity.entityYAxisOffset = buildEntity.entityYAxisOffset * -1;
                }

                if (entity instanceof HangingEntity) {
                    buildEntity.entityFacing = entity.getDirection();
                }

                CompoundTag entityTagCompound = new CompoundTag();
                entity.saveAsPassenger(entityTagCompound);
                buildEntity.setEntityNBTData(entityTagCompound);
                scannedStructure.entities.add(buildEntity);
            }
        }

        Structure.CreateStructureFile(scannedStructure, fileLocation);
    }

    /**
     * Creates a build block from the current block state.
     *
     * @param currentState The block state.
     * @param currentBlock The current block.
     * @param currentPos   The current position.
     * @return A new Build block object.
     */
    public static BuildBlock createBuildBlockFromBlockState(BlockState currentState, Block currentBlock, BlockPos currentPos, BlockPos originalPos) {
        BuildBlock buildBlock = new BuildBlock();
        buildBlock.setBlockDomain(currentBlock.getRegistryName().getNamespace());
        buildBlock.setBlockName(currentBlock.getRegistryName().getPath());
        buildBlock.setStartingPosition(Structure.getStartingPositionFromOriginalAndCurrentPosition(currentPos, originalPos));
        buildBlock.blockPos = currentPos;

        Collection<Property<?>> properties = currentState.getProperties();

        for (Property<?> entry : properties) {
            BuildProperty property = new BuildProperty();

            property.setName(entry.getName());

            Comparable<?> value = currentState.getValue(entry);

            try {
                if (currentBlock instanceof RotatedPillarBlock && property.getName().equals("axis")) {
                    property.setValue(((Direction.Axis) value).getSerializedName());
                } else if (currentBlock instanceof CarpetBlock && property.getName().equals("color")) {
                    DyeColor dyeColor = (DyeColor) value;
                    property.setValue(dyeColor.getSerializedName());
                } else if (value instanceof StringRepresentable) {
                    StringRepresentable stringSerializable = (StringRepresentable) value;
                    property.setValue(stringSerializable.getSerializedName());
                } else {
                    property.setValue(value.toString());
                }
            } catch (Exception ex) {
                Prefab.LOGGER.error("Unable to set property [" + property.getName() + "] to value [" + value + "] for Block [" + buildBlock.getBlockDomain() + ":" + buildBlock.getBlockName() + "].");
                throw ex;
            }

            buildBlock.getProperties().add(property);
        }

        return buildBlock;
    }

    public static PositionOffset getStartingPositionFromOriginalAndCurrentPosition(BlockPos currentPos, BlockPos originalPos) {
        // if (currentPos.getX() > originalPos.getX()). currentPos is "East"
        // of hitBlock
        // if (currentPos.getZ() > originalPos.getZ()). currentPos is
        // "South" of hitBlock
        PositionOffset positionOffSet = new PositionOffset();

        if (currentPos.getX() > originalPos.getX()) {
            positionOffSet.setEastOffset(currentPos.getX() - originalPos.getX());
        } else {
            positionOffSet.setWestOffset(originalPos.getX() - currentPos.getX());
        }

        if (currentPos.getZ() > originalPos.getZ()) {
            positionOffSet.setSouthOffset(currentPos.getZ() - originalPos.getZ());
        } else {
            positionOffSet.setNorthOffset(originalPos.getZ() - currentPos.getZ());
        }

        positionOffSet.setHeightOffset(currentPos.getY() - originalPos.getY());

        return positionOffSet;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public BuildClear getClearSpace() {
        return this.clearSpace;
    }

    public void setClearSpace(BuildClear value) {
        this.clearSpace = value;
    }

    public ArrayList<BuildBlock> getBlocks() {
        return this.blocks;
    }

    public void setBlocks(ArrayList<BuildBlock> value) {
        this.blocks = value;
    }

    public void Initialize() {
        this.name = "";
        this.clearSpace = new BuildClear();
        this.blocks = new ArrayList<>();
    }

    /**
     * This is the main building method for this structure.
     *
     * @param configuration The configuration the user updated.
     * @param world         The current world.
     * @param originalPos   The block the user clicked on.
     * @param assumedNorth  This should always be "NORTH" when the file is based on a scan.
     * @param player        The player requesting the structure.
     * @return True if the build can occur, otherwise false.
     */
    public boolean BuildStructure(StructureConfiguration configuration, ServerLevel world, BlockPos originalPos, Direction assumedNorth, Player player) {
        BlockPos startBlockPos = this.clearSpace.getStartingPosition().getRelativePosition(originalPos, this.clearSpace.getShape().getDirection(), configuration.houseFacing);
        BlockPos endBlockPos = startBlockPos
                .relative(configuration.houseFacing.getCounterClockWise(), this.clearSpace.getShape().getWidth() - 1)
                .relative(configuration.houseFacing.getOpposite(), this.clearSpace.getShape().getLength() - 1)
                .relative(Direction.UP, this.clearSpace.getShape().getHeight());

        // Make sure this structure can be placed here.
        Triple<Boolean, BlockState, BlockPos> checkResult = BuildingMethods.CheckBuildSpaceForAllowedBlockReplacement(world, startBlockPos, endBlockPos, player);

        if (!checkResult.getFirst()) {
            // Send a message to the player saying that the structure could not
            // be built.
            TranslatableComponent message = new TranslatableComponent(
                    GuiLangKeys.GUI_STRUCTURE_NOBUILD,
                    checkResult.getSecond().getBlock().getRegistryName().toString(),
                    checkResult.getThird().getX(),
                    checkResult.getThird().getY(),
                    checkResult.getThird().getZ());

            message.setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));
            player.sendMessage(message, player.getUUID());
            return false;
        }

        // Play the building sound.
        world.playSound(null, originalPos, ModRegistry.BuildingBlueprint.get(), SoundCategory.NEUTRAL, 0.8f, 0.8f);

        if (!this.BeforeBuilding(configuration, world, originalPos, assumedNorth, player)) {
            try {
                // First, clear the area where the structure will be built.
                this.ClearSpace(configuration, world, startBlockPos, endBlockPos);

                boolean blockPlacedWithCobbleStoneInstead = false;
                ArrayList<Tuple<BlockState, BlockPos>> laterBlocks = new ArrayList<>();

                // Now place all the blocks.
                for (BuildBlock block : this.getBlocks()) {
                    Block foundBlock = ForgeRegistries.BLOCKS.getValue(block.getResourceLocation());

                    if (foundBlock != null) {
                        BlockState blockState = foundBlock.defaultBlockState();
                        BuildBlock subBlock = null;

                        // Check if water should be replaced with cobble.
                        if (!this.WaterReplacedWithCobbleStone(configuration, block, world, originalPos, assumedNorth, foundBlock, blockState, player)
                                && !this.CustomBlockProcessingHandled(configuration, block, world, originalPos, assumedNorth, foundBlock, blockState, player)) {

                            // Set the glass color if this structure can have the glass configured.
                            if (!this.processedGlassBlock(configuration, block, world, originalPos, foundBlock)) {
                                block = BuildBlock.SetBlockState(configuration, world, originalPos, assumedNorth, block, foundBlock, blockState, this);
                            }

                            if (block.getSubBlock() != null) {
                                foundBlock = ForgeRegistries.BLOCKS.getValue(block.getSubBlock().getResourceLocation());
                                blockState = foundBlock.defaultBlockState();

                                subBlock = BuildBlock.SetBlockState(configuration, world, originalPos, assumedNorth, block.getSubBlock(), foundBlock, blockState, this);
                            }

                            BlockPos setBlockPos = block.getStartingPosition().getRelativePosition(originalPos,
                                    this.getClearSpace().getShape().getDirection(), configuration.houseFacing);

                            Block blockToPlace = block.getBlockState().getBlock();

                            // Some blocks need to happen later because they attach to solid blocks and have no collision logic.
                            // Fluid blocks may not have collision but they should always be placed.
                            if (!blockToPlace.hasCollision && !(blockToPlace instanceof FlowingFluidBlock)) {
                                laterBlocks.add(new Tuple<>(block.getBlockState(), setBlockPos));
                            } else {
                                world.setBlock(setBlockPos, block.getBlockState(), Constants.BlockFlags.DEFAULT);
                            }

                            if (subBlock != null) {
                                BlockPos subBlockPos = subBlock.getStartingPosition().getRelativePosition(originalPos,
                                        this.getClearSpace().getShape().getDirection(), configuration.houseFacing);

                                world.setBlock(subBlockPos, subBlock.getBlockState(), Constants.BlockFlags.DEFAULT);
                            }
                        }
                    } else {
                        // Cannot find this block in the registry. This can happen if a structure file has a mod block that
                        // no longer exists.
                        // In this case, print an informational message and replace it with cobblestone.
                        String blockTypeNotFound = block.getResourceLocation().toString();
                        block = BuildBlock.SetBlockState(configuration, world, originalPos, assumedNorth, block, Blocks.COBBLESTONE, Blocks.COBBLESTONE.defaultBlockState(), this);

                        BlockPos setBlockPos = block.getStartingPosition().getRelativePosition(originalPos,
                                this.getClearSpace().getShape().getDirection(), configuration.houseFacing);

                        world.setBlock(setBlockPos, block.getBlockState(), Constants.BlockFlags.DEFAULT);

                        if (!blockPlacedWithCobbleStoneInstead) {
                            blockPlacedWithCobbleStoneInstead = true;
                            Prefab.LOGGER
                                    .warn("A Block was in the structure, but it is not registered. This block was replaced with vanilla cobblestone instead. Block type not found: ["
                                            + blockTypeNotFound + "]");
                        }
                    }
                }

                for (Tuple<BlockState, BlockPos> block : laterBlocks) {
                    world.setBlock(block.getSecond(), block.getFirst(), Constants.BlockFlags.DEFAULT);
                }

                this.configuration = configuration;
                this.world = world;
                this.assumedNorth = assumedNorth;
                this.originalPos = originalPos;

                this.AfterBuilding(this.configuration, this.world, this.originalPos, this.assumedNorth, player);
            } catch (Exception ex) {
                Prefab.LOGGER.error(ex);
            }

            for (BlockPos pos : BlockPos.betweenClosed(startBlockPos, endBlockPos)) {
                Block block = world.getBlockState(pos).getBlock();

                world.blockUpdated(pos, block);
            }

            if (StructureEventHandler.structuresToBuild.containsKey(player)) {
                StructureEventHandler.structuresToBuild.get(player).add(this);
            } else {
                ArrayList<Structure> structures = new ArrayList<>();
                structures.add(this);
                StructureEventHandler.structuresToBuild.put(player, structures);
            }
        }

        return true;
    }

    /**
     * This method is to process before a clear space block is set to air.
     *
     * @param pos The block position being processed.
     */
    public void BeforeClearSpaceBlockReplaced(BlockPos pos) {
    }

    /**
     * Processes before the hanging entity is removed.
     * @param hangingEntity The entity to process.
     */
    public void BeforeHangingEntityRemoved(HangingEntity hangingEntity) {
    }

    /**
     * This method is used before any building occurs to check for things or possibly pre-build locations. Note: This is
     * even done before blocks are cleared.
     *
     * @param configuration The structure configuration.
     * @param world         The current world.
     * @param originalPos   The original position clicked on.
     * @param assumedNorth  The assumed northern direction.
     * @param player        The player which initiated the construction.
     * @return False if processing should continue, otherwise true to cancel processing.
     */
    protected boolean BeforeBuilding(StructureConfiguration configuration, Level world, BlockPos originalPos, Direction assumedNorth, Player player) {
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
    public void AfterBuilding(StructureConfiguration configuration, ServerLevel world, BlockPos originalPos, Direction assumedNorth, Player player) {
    }

    protected void ClearSpace(StructureConfiguration configuration, Level world, BlockPos startBlockPos, BlockPos endBlockPos) {
        if (this.clearSpace.getShape().getWidth() > 0
                && this.clearSpace.getShape().getLength() > 0) {

            this.clearedBlockPos = new ArrayList<>();

            for (BlockPos pos : BlockPos.betweenClosed(startBlockPos, endBlockPos)) {
                if (this.BlockShouldBeClearedDuringConstruction(configuration, world, originalPos, assumedNorth, pos)) {
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                }
            }
        } else {
            this.clearedBlockPos = new ArrayList<>();
        }
    }

    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, Level world, BlockPos originalPos,
                                                   Direction assumedNorth, Block foundBlock, BlockState blockState, Player player) {
        return false;
    }

    protected Boolean BlockShouldBeClearedDuringConstruction(StructureConfiguration configuration, Level world, BlockPos originalPos, Direction assumedNorth, BlockPos blockPos) {
        return true;
    }

    /**
     * Determines if a water block was replaced with cobblestone because this structure was built in the nether or the
     * end.
     *
     * @param configuration The structure configuration.
     * @param block         The build block object.
     * @param world         The workd object.
     * @param originalPos   The original block position this structure was built on.
     * @param assumedNorth  The assumed north direction (typically north).
     * @param foundBlock    The actual block found at the current location.
     * @param blockState    The block state to set for the current block.
     * @param player        The player requesting this build.
     * @return Returns true if the water block was replaced by cobblestone, otherwise false.
     */
    protected Boolean WaterReplacedWithCobbleStone(StructureConfiguration configuration, BuildBlock block, Level world, BlockPos originalPos,
                                                   Direction assumedNorth, Block foundBlock, BlockState blockState, Player player) {
        // Replace water blocks and waterlogged blocks with cobblestone when this is not an ultra warm world type.
        // Also check a configuration value to determine if water blocks are allowed in non-over-world dimensions.
        boolean isOverWorld = Level.OVERWORLD.compareTo(world.dimension()) == 0;

        if (world.dimensionType().ultraWarm()
                || (!isOverWorld && Prefab.proxy.getServerConfiguration().allowWaterInNonOverworldDimensions)) {
            boolean foundWaterLikeBlock = (foundBlock instanceof FlowingFluidBlock && blockState.getMaterial() == Material.WATER)
                    || foundBlock instanceof SeaGrassBlock;

            if (!foundWaterLikeBlock) {
                for (BuildProperty property : block.getProperties()) {
                    if (property.getName().equalsIgnoreCase(BlockStateProperties.WATERLOGGED.getName())
                            && property.getValue().equalsIgnoreCase(BlockStateProperties.WATERLOGGED.getName(true))) {
                        // Found a waterlogged block. Replace with cobblestone.
                        foundWaterLikeBlock = true;
                        break;
                    }
                }
            }

            if (foundWaterLikeBlock) {
                ResourceLocation cobbleIdentifier = Registry.BLOCK.getKey(Blocks.COBBLESTONE);
                block.setBlockDomain(cobbleIdentifier.getNamespace());
                block.setBlockName(cobbleIdentifier.getPath());
                block.setBlockState(Blocks.COBBLESTONE.defaultBlockState());

                BlockPos setBlockPos = block.getStartingPosition().getRelativePosition(originalPos,
                        this.getClearSpace().getShape().getDirection(), configuration.houseFacing);

                world.setBlock(setBlockPos, block.getBlockState(), Constants.BlockFlags.DEFAULT);

                return true;
            }
        }

        return false;
    }

    protected boolean processedGlassBlock(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos, Block foundBlock) {
        if (!this.hasGlassColor(configuration)) {
            return false;
        }

        if (foundBlock.getRegistryName().getNamespace().equals(Blocks.WHITE_STAINED_GLASS.getRegistryName().getNamespace())
                && foundBlock.getRegistryName().getPath().endsWith("glass")) {
            BlockState blockState = BuildingMethods.getStainedGlassBlock(this.getGlassColor(configuration));

            block.setBlockState(blockState);

            return true;
        } else if (foundBlock.getRegistryName().getNamespace().equals(Blocks.WHITE_STAINED_GLASS_PANE.getRegistryName().getNamespace())
                && foundBlock.getRegistryName().getPath().endsWith("glass_pane")) {
            BlockState blockState = BuildingMethods.getStainedGlassPaneBlock(this.getGlassColor(configuration));

            BuildBlock.SetBlockState(
                    configuration,
                    world,
                    originalPos,
                    assumedNorth,
                    block,
                    foundBlock,
                    blockState,
                    this);

            return true;
        }

        return false;
    }

    protected boolean hasGlassColor(StructureConfiguration configuration) {
        return false;
    }

    protected FullDyeColor getGlassColor(StructureConfiguration configuration) {
        return FullDyeColor.CLEAR;
    }
}