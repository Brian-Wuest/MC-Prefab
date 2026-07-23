package com.prefab.structures.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.mojang.logging.LogUtils;
import com.prefab.*;
import com.prefab.blocks.BlockFlags;
import com.prefab.blocks.FullDyeColor;
import com.prefab.gui.GuiLangKeys;
import com.prefab.structures.config.StructureConfiguration;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.StringRepresentable;
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
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

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

            if (currentState.getBlock() == Blocks.WATER && excludeWater) {
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

                ResourceLocation resourceLocation = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(tileEntity.getType());
                CompoundTag tagCompound = tileEntity.saveWithFullMetadata(world.registryAccess());

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

        var pos1 = new Vec3(cornerPos1.getX(), cornerPos1.getY(), cornerPos1.getZ());
        var pos2 = new Vec3(cornerPos2.getX(), cornerPos2.getY(), cornerPos2.getZ());

        AABB axis = new AABB(pos1, pos2);

        for (Entity entity : world.getEntities(null, axis)) {
            BlockPos entityPos = entity.blockPosition();

            if (entity instanceof HangingEntity) {
                // Use the AbstractDecorationEntity getDecorationBlockPos function instead since it is more accurate for itemframes and paintings.
                entityPos = ((HangingEntity) entity).getPos();
            }

            if (entityPos.getX() >= x_radiusRangeBegin && entityPos.getX() <= x_radiusRangeEnd
                    && entityPos.getZ() >= z_radiusRangeBegin && entityPos.getZ() <= z_radiusRangeEnd
                    && entityPos.getY() >= y_radiusRangeBegin && entityPos.getY() <= y_radiusRangeEnd) {
                BuildEntity buildEntity = new BuildEntity();
                buildEntity.setEntityResourceString(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()));
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

                try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(entity.problemPath(), LogUtils.getLogger())) {
                    TagValueOutput tagValueOutput = TagValueOutput.createWithContext(scopedCollector, entity.registryAccess());
                    entity.saveAsPassenger(tagValueOutput);
                    CompoundTag entityTagCompound = tagValueOutput.buildResult();
                    buildEntity.setEntityNBTData(entityTagCompound);
                    scannedStructure.entities.add(buildEntity);
                }
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
        ResourceLocation blockIdentifier = BuiltInRegistries.BLOCK.getKey(currentBlock);
        buildBlock.setBlockDomain(blockIdentifier.getNamespace());
        buildBlock.setBlockName(blockIdentifier.getPath());
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
                PrefabBase.logger.error("Unable to set property [" + property.getName() + "] to value [" + value + "] for Block [" + buildBlock.getBlockDomain() + ":" + buildBlock.getBlockName() + "].");
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
     * @param player        The player requesting the structure.
     * @return True if the build can occur, otherwise false.
     */
    public boolean BuildStructure(StructureConfiguration configuration, ServerLevel world, BlockPos originalPos, ServerPlayer player) {
        BlockPos startBlockPos = this.clearSpace.getStartingPosition().getRelativePosition(originalPos, this.clearSpace.getShape().getDirection(), configuration.houseFacing);
        BlockPos endBlockPos = startBlockPos
                .relative(configuration.houseFacing.getCounterClockWise(), this.clearSpace.getShape().getWidth() - 1)
                .relative(configuration.houseFacing.getOpposite(), this.clearSpace.getShape().getLength() - 1)
                .relative(Direction.UP, this.clearSpace.getShape().getHeight());

        // Make sure this structure can be placed here.
        AllowedBlockReplacementResult checkResult = BuildingMethods.CheckBuildSpaceForAllowedBlockReplacement(world, startBlockPos, endBlockPos, player);

        if (checkResult.allowedToReplace != ReplacementResultType.ALLOWED) {
            String langKey =
                    switch (checkResult.allowedToReplace) {
                        case ALLOWED -> null;
                        case NOT_ALLOWED_SPAWN_PROTECTION ->
                                GuiLangKeys.GUI_STRUCTURE_NOBUILD_SPAWN_PROTECTION;
                        case NOT_ALLOWED_MOD_PROTECTED ->
                                GuiLangKeys.GUI_STRUCTURE_NOBUILD_MOD_PROTECTION;
                        case ReplacementResultType.NOT_ALLOWED_UNBREAKABLE_BLOCK ->
                                GuiLangKeys.GUI_STRUCTURE_NOBUILD_UNBREAKABLE;
                        case NOT_ALLOWED_STRICT_BUILDING_MODE ->
                                GuiLangKeys.GUI_STRUCTURE_NOBUILD_STRICT_BUILDING_MODE;
                    };

            // Send a message to the player saying that the structure could not
            // be built.
            if (langKey != null) {
                MutableComponent message = Structure.createCannotBuildMessage(checkResult, langKey);
                player.sendSystemMessage(message);
                return false;
            }
        }

        if (PrefabBase.serverConfiguration.playBuildingSound) {
            // Play the building sound.
            world.playSound(null, originalPos, ModRegistryBase.BuildingBlueprint, SoundSource.NEUTRAL, 0.8f, 0.8f);
        }

        if (!this.BeforeBuilding(configuration, world, originalPos, player)) {
            try {
                // First, clear the area where the structure will be built.
                this.ClearSpace(configuration, world, startBlockPos, endBlockPos);

                ArrayList<Tuple<BlockState, BlockPos>> laterBlocks = new ArrayList<>();
                boolean blockPlacedWithCobbleStoneInstead = false;

                // Now place all of the blocks.
                for (BuildBlock block : this.getBlocks()) {
                    Block foundBlock = BuiltInRegistries.BLOCK.getValue(block.getResourceLocation());

                    if (foundBlock != null) {
                        BlockState blockState = foundBlock.defaultBlockState();
                        BuildBlock subBlock = null;

                        // Check if water should be replaced with cobble.
                        if (!this.WaterReplacedWithCobbleStone(configuration, block, world, originalPos, foundBlock, blockState, player)
                                && !this.CustomBlockProcessingHandled(configuration, block, world, originalPos, foundBlock, blockState, player)) {
                            // Set the glass color if this structure can have the glass configured.
                            if (!this.processedGlassBlock(configuration, block, world, originalPos, foundBlock)) {
                                block = BuildBlock.SetBlockState(configuration, world, originalPos, block, foundBlock, blockState, this);
                            }

                            if (block.getSubBlock() != null) {
                                foundBlock = BuiltInRegistries.BLOCK.getValue(block.getSubBlock().getResourceLocation());
                                blockState = foundBlock.defaultBlockState();

                                subBlock = BuildBlock.SetBlockState(configuration, world, originalPos, block.getSubBlock(), foundBlock, blockState, this);
                            }

                            BlockPos setBlockPos = block.getStartingPosition().getRelativePosition(originalPos,
                                    this.getClearSpace().getShape().getDirection(), configuration.houseFacing);

                            Block blockToPlace = block.getBlockState().getBlock();

                            // Some blocks need to happen later because they attach to solid blocks and have no collision logic.
                            // Fluid blocks may not have collision; but they should always be placed.
                            if ((!blockToPlace.hasCollision && !(blockToPlace instanceof LiquidBlock))
                                    || (blockToPlace instanceof CarpetBlock)) {
                                laterBlocks.add(new Tuple<>(block.getBlockState(), setBlockPos));
                            } else {
                                world.setBlock(setBlockPos, block.getBlockState(), BlockFlags.DEFAULT);
                            }

                            if (subBlock != null) {
                                BlockPos subBlockPos = subBlock.getStartingPosition().getRelativePosition(originalPos,
                                        this.getClearSpace().getShape().getDirection(), configuration.houseFacing);

                                world.setBlock(subBlockPos, subBlock.getBlockState(), BlockFlags.DEFAULT);
                            }
                        }
                    } else {
                        // Cannot find this block in the registry. This can happen if a structure file has a mod block that
                        // no longer exists.
                        // In this case, print an informational message and replace it with cobblestone.
                        String blockTypeNotFound = block.getResourceLocation().toString();
                        block = BuildBlock.SetBlockState(configuration, world, originalPos, block, Blocks.COBBLESTONE, Blocks.COBBLESTONE.defaultBlockState(), this);
                        this.priorityOneBlocks.add(block);

                        if (!blockPlacedWithCobbleStoneInstead) {
                            blockPlacedWithCobbleStoneInstead = true;
                            PrefabBase.logger
                                    .warn("A Block was in the structure, but it is not registered. This block was replaced with vanilla cobblestone instead. Block type not found: ["
                                            + blockTypeNotFound + "]");
                        }
                    }
                }

                for (Tuple<BlockState, BlockPos> block : laterBlocks) {
                    world.setBlock(block.getSecond(), block.getFirst(), BlockFlags.DEFAULT);
                }

                this.configuration = configuration;
                this.world = world;
                this.originalPos = originalPos;

                // Set all the tile entities here.
                this.setBlockEntities();

                this.AfterBuilding(this.configuration, this.world, this.originalPos, player);
            } catch (Exception ex) {
                PrefabBase.logger.error(ex);
            }

            for (BlockPos pos : BlockPos.betweenClosed(startBlockPos, endBlockPos)) {
                Block block = world.getBlockState(pos).getBlock();
                world.updateNeighborsAt(pos, block);
            }

            if (PrefabBase.structuresToBuild.containsKey(player)) {
                PrefabBase.structuresToBuild.get(player).add(this);
            } else {
                ArrayList<Structure> structures = new ArrayList<>();
                structures.add(this);
                PrefabBase.structuresToBuild.put(player, structures);
            }

        }

        return true;
    }

    private static @NotNull MutableComponent createCannotBuildMessage(AllowedBlockReplacementResult checkResult, String langKey) {
        MutableComponent blockName = Component.translatable(checkResult.protectedBlock.getBlock().getDescriptionId());
        MutableComponent message = Component.translatable(
                langKey,
                blockName,
                checkResult.protectedBlockPos.getX(),
                checkResult.protectedBlockPos.getY(),
                checkResult.protectedBlockPos.getZ());

        message.setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW));

        return message;
    }

    /**
     * This method is to process before a clear space block is set to air.
     *
     * @param pos The block position being processed.
     */
    public void BeforeClearSpaceBlockReplaced(BlockPos pos) {
    }

    public void BeforeHangingEntityRemoved(HangingEntity hangingEntity) {
    }

    /**
     * This method is used before any building occurs to check for things or possibly pre-build locations. Note: This is
     * even done before blocks are cleared.
     *
     * @param configuration The structure configuration.
     * @param world         The current world.
     * @param originalPos   The original position clicked on.
     * @param player        The player which initiated the construction.
     * @return False if processing should continue, otherwise true to cancel processing.
     */
    protected boolean BeforeBuilding(StructureConfiguration configuration, Level world, BlockPos originalPos, Player player) {
        return false;
    }

    /**
     * This method is used after the main building is build for any additional structures or modifications.
     *
     * @param configuration The structure configuration.
     * @param world         The current world.
     * @param originalPos   The original position clicked on.
     * @param player        The player which initiated the construction.
     */
    public void AfterBuilding(StructureConfiguration configuration, ServerLevel world, BlockPos originalPos, Player player) {
    }

    protected void ClearSpace(StructureConfiguration configuration, Level world, BlockPos startBlockPos, BlockPos endBlockPos) {
        if (this.clearSpace.getShape().getWidth() > 0
                && this.clearSpace.getShape().getLength() > 0) {

            this.clearedBlockPos = new ArrayList<>();

            for (BlockPos pos : BlockPos.betweenClosed(startBlockPos, endBlockPos)) {
                if (this.BlockShouldBeClearedDuringConstruction(configuration, world, originalPos, pos)) {
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                }
            }
        } else {
            this.clearedBlockPos = new ArrayList<>();
        }
    }

    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, Level world, BlockPos originalPos,
                                                   Block foundBlock, BlockState blockState, Player player) {
        return false;
    }

    protected Boolean BlockShouldBeClearedDuringConstruction(StructureConfiguration configuration, Level world, BlockPos originalPos, BlockPos blockPos) {
        return true;
    }

    /**
     * Determines if a water block was replaced with cobblestone because this structure was built in the nether or the
     * end.
     *
     * @param configuration The structure configuration.
     * @param block         The build block object.
     * @param world         The world object.
     * @param originalPos   The original block position this structure was built on.
     * @param foundBlock    The actual block found at the current location.
     * @param blockState    The block state to set for the current block.
     * @param player        The player requesting this build.
     * @return Returns true if the water block was replaced by cobblestone, otherwise false.
     */
    protected Boolean WaterReplacedWithCobbleStone(StructureConfiguration configuration, BuildBlock block, Level world, BlockPos originalPos,
                                                   Block foundBlock, BlockState blockState, Player player) {
        // Replace water blocks and waterlogged blocks with cobblestone when this is not an ultra warm world type.
        // Also check a configuration value to determine if water blocks are allowed in other non-overworld dimensions such as The End.
        boolean isOverworld = Level.OVERWORLD.location().toString().equals(world.dimension().location().toString());

        if (world.dimensionType().ultraWarm()
                || (!isOverworld && PrefabBase.serverConfiguration.allowWaterInNonOverworldDimensions)) {
            boolean foundWaterLikeBlock = (foundBlock instanceof LiquidBlock && blockState.getBlock() == Blocks.WATER)
                    || foundBlock instanceof SeagrassBlock;

            if (!foundWaterLikeBlock) {
                // This is not a direct water block; check if it is waterlogged.
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
                ResourceLocation cobbleIdentifier = BuiltInRegistries.BLOCK.getKey(Blocks.COBBLESTONE);
                block.setBlockDomain(cobbleIdentifier.getNamespace());
                block.setBlockName(cobbleIdentifier.getPath());
                block.setBlockState(Blocks.COBBLESTONE.defaultBlockState());

                BlockPos setBlockPos = block.getStartingPosition().getRelativePosition(originalPos,
                        this.getClearSpace().getShape().getDirection(), configuration.houseFacing);

                world.setBlock(setBlockPos, block.getBlockState(), BlockFlags.DEFAULT);
                return true;
            }
        }

        return false;
    }

    protected boolean processedGlassBlock(StructureConfiguration configuration, BuildBlock block, Level world, BlockPos originalPos, Block foundBlock) {
        if (!this.hasGlassColor(configuration)) {
            return false;
        }

        ResourceLocation blockIdentifier = BuiltInRegistries.BLOCK.getKey(foundBlock);
        ResourceLocation glassIdentifier = BuiltInRegistries.BLOCK.getKey(Blocks.WHITE_STAINED_GLASS);
        ResourceLocation glassPaneIdentifier = BuiltInRegistries.BLOCK.getKey(Blocks.WHITE_STAINED_GLASS_PANE);

        if (blockIdentifier.getNamespace().equals(glassIdentifier.getNamespace())
                && blockIdentifier.getPath().endsWith("glass")) {
            BlockState blockState = BuildingMethods.getStainedGlassBlock(this.getGlassColor(configuration));

            block.setBlockState(blockState);

            return true;
        } else if (blockIdentifier.getNamespace().equals(glassPaneIdentifier.getNamespace())
                && blockIdentifier.getPath().endsWith("glass_pane")) {
            BlockState blockState = BuildingMethods.getStainedGlassPaneBlock(this.getGlassColor(configuration));

            BuildBlock.SetBlockState(
                    configuration,
                    world,
                    originalPos,
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

    protected void setBlockEntities() {
        for (BuildTileEntity buildTileEntity : this.tileEntities) {
            try {
                // Beds are processed separately.
                if (buildTileEntity.getEntityName().equals("bed")) {
                    continue;
                }

                BlockPos tileEntityPos = buildTileEntity.getStartingPosition().getRelativePosition(this.originalPos,
                        this.getClearSpace().getShape().getDirection(), this.configuration.houseFacing);
                BlockEntity tileEntity = this.world.getBlockEntity(tileEntityPos);
                BlockState tileBlock = this.world.getBlockState(tileEntityPos);

                if (tileEntity != null) {
                    this.world.removeBlockEntity(tileEntityPos);
                }

                if (tileBlock.isAir()) {
                    // The original block was never set so we cannot place this tile entity.
                    continue;
                }

                tileEntity = BlockEntity.loadStatic(tileEntityPos, tileBlock, buildTileEntity.getEntityDataTag(), this.world.registryAccess());

                if (tileEntity == null) {
                    continue;
                }

                this.world.setBlockEntity(tileEntity);
                this.world.getChunk(tileEntityPos).markUnsaved();
                tileEntity.setChanged();
                Packet<ClientGamePacketListener> packet = tileEntity.getUpdatePacket();

                if (packet != null) {
                    this.world.getServer().getPlayerList().broadcastAll(packet);
                }
            } catch (Exception ex) {
                PrefabBase.logger.error(ex);
            }
        }
    }
}