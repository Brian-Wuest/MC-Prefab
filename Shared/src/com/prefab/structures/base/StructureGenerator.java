package com.prefab.structures.base;

import com.mojang.logging.LogUtils;
import com.prefab.PrefabBase;
import com.prefab.Tuple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.*;

public class StructureGenerator {
    public static ArrayList<Tuple<Structure, BuildEntity>> entitiesToGenerate = new ArrayList<>();

    public static int ticksSinceLastEntitiesGenerated = 0;

    public static void CheckForStructureToBuildAndBuildIt() {
        ArrayList<Player> playersToRemove = new ArrayList<Player>();

        StructureGenerator.ticksSinceLastEntitiesGenerated++;

        if (!StructureGenerator.entitiesToGenerate.isEmpty()) {
            StructureGenerator.ticksSinceLastEntitiesGenerated++;

            if (StructureGenerator.ticksSinceLastEntitiesGenerated > 40) {
                // Process any entities.
                StructureGenerator.processStructureEntities();

                StructureGenerator.ticksSinceLastEntitiesGenerated = 0;
            }
        }

        if (!PrefabBase.structuresToBuild.isEmpty()) {
            for (Map.Entry<Player, ArrayList<Structure>> entry : PrefabBase.structuresToBuild.entrySet()) {
                ArrayList<Structure> structuresToRemove = new ArrayList<>();

                // Build the first 100 blocks of each structure for this player.
                for (Structure structure : entry.getValue()) {
                    if (!structure.entitiesRemoved) {
                        // Go through each block and find any entities there. If there are any; kill them if they aren't players.
                        // If there is a player there...they will probably die anyways.....
                        for (BlockPos clearedPos : structure.clearedBlockPos) {
                            AABB axisPos = Shapes.block().bounds().move(clearedPos);

                            List<Entity> list = structure.world.getEntities(null, axisPos);

                            if (!list.isEmpty()) {
                                for (Entity entity : list) {
                                    // Don't kill living entities.
                                    if (!(entity instanceof LivingEntity)) {
                                        if (entity instanceof HangingEntity) {
                                            structure.BeforeHangingEntityRemoved((HangingEntity) entity);
                                        }

                                        entity.remove(Entity.RemovalReason.DISCARDED);
                                    }
                                }
                            }
                        }

                        structure.entitiesRemoved = true;
                    }

                    if (!structure.airBlocks.isEmpty()) {
                        structure.hasAirBlocks = true;
                    }

                    for (int i = 0; i < 100; i++) {
                        i = StructureGenerator.setBlock(i, structure, structuresToRemove);
                    }

                    // After building the blocks for this tick, find waterlogged blocks and remove them.
                    StructureGenerator.removeWaterLogging(structure);
                }

                // Update the list of structures to remove this structure since it's done building.
                StructureGenerator.removeStructuresFromList(structuresToRemove, entry);

                if (entry.getValue().isEmpty()) {
                    playersToRemove.add(entry.getKey());
                }
            }
        }

        // Remove each player that has their structure's built.
        for (Player player : playersToRemove) {
            PrefabBase.structuresToBuild.remove(player);
        }
    }

    private static int setBlock(int i, Structure structure, ArrayList<Structure> structuresToRemove) {
        // Structure clearing happens before anything else.
        // Don't bother clearing the area for water-based structures
        // Anything which should be air will be air
        if (!structure.clearedBlockPos.isEmpty() && !structure.hasAirBlocks) {
            BlockPos currentPos = structure.clearedBlockPos.getFirst();
            structure.clearedBlockPos.removeFirst();

            BlockState clearBlockState = structure.world.getBlockState(currentPos);

            // If this block is not specifically air then set it to air.
            // This will also break other mod's logic blocks but they would probably be broken due to structure
            // generation anyways.
            if (clearBlockState.getBlock() != Blocks.AIR) {
                structure.BeforeClearSpaceBlockReplaced(currentPos);

                for (Direction adjacentBlock : Direction.values()) {
                    BlockPos tempPos = currentPos.relative(adjacentBlock);
                    BlockState foundState = structure.world.getBlockState(tempPos);
                    Block foundBlock = foundState.getBlock();

                    // Check if this block is one that is attached to a facing, if it is, remove it first.
                    if (foundBlock instanceof TorchBlock
                            || foundBlock instanceof SignBlock
                            || foundBlock instanceof LeverBlock
                            || foundBlock instanceof ButtonBlock
                            || foundBlock instanceof BedBlock
                            || foundBlock instanceof CarpetBlock
                            || foundBlock instanceof FlowerPotBlock
                            || foundBlock instanceof SugarCaneBlock
                            || foundBlock instanceof BasePressurePlateBlock
                            || foundBlock instanceof DoorBlock
                            || foundBlock instanceof LadderBlock
                            || foundBlock instanceof VineBlock
                            || foundBlock instanceof RedStoneWireBlock
                            || foundBlock instanceof DiodeBlock
                            || foundBlock instanceof AbstractBannerBlock
                            || foundBlock instanceof LanternBlock
                            || foundBlock instanceof BaseRailBlock) {
                        structure.BeforeClearSpaceBlockReplaced(currentPos);

                        if (foundBlock instanceof DoorBlock) {
                            // Make sure to remove both parts before going on.
                            DoubleBlockHalf currentHalf = foundState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);

                            BlockPos otherHalfPos = currentHalf == DoubleBlockHalf.LOWER
                                    ? tempPos.above() : tempPos.below();

                            structure.world.setBlock(tempPos, Blocks.AIR.defaultBlockState(), 35);
                            structure.world.setBlock(otherHalfPos, Blocks.AIR.defaultBlockState(), 35);

                        } else if (!(foundBlock instanceof BedBlock)) {
                            structure.world.removeBlock(tempPos, false);
                        } else {
                            // Found a bed, try to find the other part of the bed and remove it.
                            for (Direction currentDirection : Direction.values()) {
                                BlockPos bedPos = tempPos.relative(currentDirection);
                                BlockState bedState = structure.world.getBlockState(bedPos);

                                if (bedState.getBlock() instanceof BedBlock) {
                                    // found the other part of the bed. Remove the current block and this one.
                                    structure.world.setBlock(tempPos, Blocks.AIR.defaultBlockState(), 35);
                                    structure.world.setBlock(bedPos, Blocks.AIR.defaultBlockState(), 35);
                                    break;
                                }
                            }
                        }
                    }
                }

                structure.world.removeBlock(currentPos, false);
            } else {
                // This is just an air block, move onto the next block don't need to wait for the next tick.
                i--;
            }

            return i;
        }

        BuildBlock currentBlock = null;

        if (!structure.priorityOneBlocks.isEmpty()) {
            currentBlock = structure.priorityOneBlocks.getFirst();
            structure.priorityOneBlocks.removeFirst();
        } else {
            // There are no more blocks to set.
            structuresToRemove.add(structure);
            return 999;
        }

        BlockState state = currentBlock.getBlockState();

        BlockPos setBlockPos = currentBlock.getStartingPosition().getRelativePosition(structure.originalPos,
                structure.getClearSpace().getShape().getDirection(), structure.configuration.houseFacing);

        BuildingMethods.ReplaceBlock(structure.world, setBlockPos, state, 2);

        // After placing the initial block, set the sub-block. This needs to happen as the list isn't always in the
        // correct order.
        if (currentBlock.getSubBlock() != null) {
            BuildBlock subBlock = currentBlock.getSubBlock();

            BuildingMethods.ReplaceBlock(structure.world, subBlock.getStartingPosition().getRelativePosition(structure.originalPos,
                    structure.getClearSpace().getShape().getDirection(), structure.configuration.houseFacing), subBlock.getBlockState());
        }

        return i;
    }

    private static void removeStructuresFromList(ArrayList<Structure> structuresToRemove, Map.Entry<Player, ArrayList<Structure>> entry) {
        for (Structure structure : structuresToRemove) {
            StructureGenerator.removeWaterLogging(structure);

            for (BuildEntity buildEntity : structure.entities) {
                String entityResourceString = buildEntity.getEntityResourceString();

                if (entityResourceString != null
                        && !entityResourceString.isBlank()) {
                    Optional<EntityType<?>> entityType = EntityType.byString(entityResourceString);

                    if (entityType.isPresent()) {
                        StructureGenerator.entitiesToGenerate.add(new Tuple<>(structure, buildEntity));
                    }
                }
            }

            // This structure is done building. Do any post-building operations.
            entry.getValue().remove(structure);
        }
    }

    private static void processStructureEntities() {
        for (Tuple<Structure, BuildEntity> entityRecords : StructureGenerator.entitiesToGenerate) {
            BuildEntity buildEntity = entityRecords.second;
            Structure structure = entityRecords.first;

            Optional<EntityType<?>> entityType = EntityType.byString(buildEntity.getEntityResourceString());

            if (entityType.isPresent()) {
                Entity entity = entityType.get().create(structure.world, EntitySpawnReason.STRUCTURE);

                if (entity != null) {
                    CompoundTag tagCompound = buildEntity.getEntityDataTag();
                    BlockPos entityPos = buildEntity.getStartingPosition().getRelativePosition(structure.originalPos,
                            structure.getClearSpace().getShape().getDirection(), structure.configuration.houseFacing);

                    if (tagCompound != null) {
                        if (tagCompound.contains("UUID")) {
                            tagCompound.put("UUID", new IntArrayTag(UUIDUtil.uuidToIntArray(UUID.randomUUID())));
                        }

                        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(entity.problemPath(), LogUtils.getLogger())) {
                            tagCompound = StructureGenerator.updateTagDueToVersionUpdate(entity, tagCompound);

                            ListTag nbttaglist = new ListTag();
                            nbttaglist.add(DoubleTag.valueOf(entityPos.getX()));
                            nbttaglist.add(DoubleTag.valueOf(entityPos.getY()));
                            nbttaglist.add(DoubleTag.valueOf(entityPos.getZ()));
                            tagCompound.put("Pos", nbttaglist);

                            TagValueInput tagValue = (TagValueInput) TagValueInput.create(scopedCollector, entity.registryAccess(), tagCompound);

                            entity.load(tagValue);
                        }
                    }

                    // Set item frame facing and rotation here.
                    switch (entity) {
                        case ItemFrame itemFrame ->
                                entity = StructureGenerator.setItemFrameFacingAndRotation(itemFrame, buildEntity, entityPos, structure);
                        case Painting painting ->
                                entity = StructureGenerator.setPaintingFacingAndRotation(painting, buildEntity, entityPos, structure);
                        case AbstractMinecart abstractMinecart -> {
                            // Minecarts need to be slightly higher to account for the rails; otherwise they will fall through the rail and the block below the rail.
                            buildEntity.entityYAxisOffset = buildEntity.entityYAxisOffset + .2;
                            entity = StructureGenerator.setEntityFacingAndRotation(entity, buildEntity, entityPos, structure);
                        }
                        default ->
                            // All other entities
                                entity = StructureGenerator.setEntityFacingAndRotation(entity, buildEntity, entityPos, structure);
                    }

                    structure.world.addFreshEntity(entity);
                }
            }
        }

        // All entities generated; clear out the list.
        StructureGenerator.entitiesToGenerate.clear();
    }

    private static void removeWaterLogging(Structure structure) {
        if (structure.hasAirBlocks) {
            for (BlockPos currentPos : structure.allBlockPositions) {
                BlockState currentState = structure.world.getBlockState(currentPos);

                if (currentState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    // This is a water loggable block and there were air blocks, make sure that it's no longer water logged.
                    currentState = currentState.setValue((BlockStateProperties.WATERLOGGED), false);
                    structure.world.setBlock(currentPos, currentState, 3);
                } else if (currentState.getBlock() == Blocks.WATER) {
                    structure.world.setBlock(currentPos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    private static Entity setPaintingFacingAndRotation(Painting entity, BuildEntity buildEntity, BlockPos entityPos, Structure structure) {
        float yaw = entity.getYRot();
        Rotation rotation = Rotation.NONE;
        double x_axis_offset = 0;
        double z_axis_offset = 0;
        Direction facing = entity.getDirection();
        double y_axis_offset = buildEntity.entityYAxisOffset * -1;

        Direction structureDirection = structure.getClearSpace().getShape().getDirection();
        Direction configurationDirection = structure.configuration.houseFacing.getOpposite();

        if (configurationDirection == structureDirection.getOpposite()) {
            rotation = Rotation.CLOCKWISE_180;
            facing = facing.getOpposite();
        } else if (configurationDirection == structureDirection.getClockWise()) {
            rotation = Rotation.CLOCKWISE_90;
            facing = facing.getClockWise();
        } else if (configurationDirection == structureDirection.getCounterClockWise()) {
            rotation = Rotation.COUNTERCLOCKWISE_90;
            facing = facing.getCounterClockWise();
        }

        // Get the painting variant from the entity.
        // This way we can get the width/height from it in blocks, so we know how to place it appropriately.
        // This is needed as differently sized paintings need to be adjusted.
        PaintingVariant paintingVariant = entity.getVariant().value();

        int paintingBlockWidth = paintingVariant.width();
        int paintingBlockHeight = paintingVariant.height();

        if ((paintingBlockHeight > paintingBlockWidth || paintingBlockHeight > 1)
                && !(paintingBlockWidth == 4 && paintingBlockHeight == 3)) {
            y_axis_offset--;
        }

        yaw = entity.rotate(rotation);

        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(entity.problemPath(), LogUtils.getLogger())) {
            TagValueOutput valueOutput = TagValueOutput.createWithoutContext(scopedCollector);

            entity.saveWithoutId(valueOutput);

            // Even though the entity saves the facing that it has, it's not going to be correct
            // Update the facing using the value we have after doing our rotations.
            valueOutput.putByte("facing", (byte) facing.get2DDataValue());

            CompoundTag compoundTag = valueOutput.buildResult();

            TagValueInput valueInput = (TagValueInput) TagValueInput.create(scopedCollector, entity.registryAccess(), compoundTag);

            entity.load(valueInput);
            StructureGenerator.updateEntityHangingBoundingBox(entity);
        }

        entity.snapTo(entityPos.getX() + x_axis_offset,
                entityPos.getY() + y_axis_offset, entityPos.getZ() + z_axis_offset, yaw,
                entity.getXRot());

        StructureGenerator.updateEntityHangingBoundingBox(entity);

        ChunkAccess chunk = structure.world.getChunkAt(entityPos);

        chunk.markUnsaved();

        return entity;
    }

    private static Entity setItemFrameFacingAndRotation(ItemFrame frame, BuildEntity buildEntity, BlockPos entityPos, Structure structure) {
        float yaw = frame.getYRot();
        Rotation rotation = Rotation.NONE;
        double x_axis_offset = buildEntity.entityXAxisOffset;
        double z_axis_offset = buildEntity.entityZAxisOffset;
        Direction facing = frame.getDirection();
        double y_axis_offset = buildEntity.entityYAxisOffset;
        x_axis_offset = x_axis_offset * -1;
        z_axis_offset = z_axis_offset * -1;

        Direction structureDirection = structure.getClearSpace().getShape().getDirection();
        Direction configurationDirection = structure.configuration.houseFacing.getOpposite();

        if (facing != Direction.UP && facing != Direction.DOWN) {
            if (configurationDirection == structureDirection.getOpposite()) {
                rotation = Rotation.CLOCKWISE_180;
                facing = facing.getOpposite();
            } else if (configurationDirection == structureDirection.getClockWise()) {
                rotation = Rotation.CLOCKWISE_90;
                facing = facing.getClockWise();
            } else if (configurationDirection == structureDirection.getCounterClockWise()) {
                rotation = Rotation.COUNTERCLOCKWISE_90;
                facing = facing.getCounterClockWise();
            } else {
                x_axis_offset = 0;
                z_axis_offset = 0;
            }
        }

        yaw = frame.rotate(rotation);

        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(frame.problemPath(),
                LogUtils.getLogger())) {
            TagValueOutput valueOutput = TagValueOutput.createWithoutContext(scopedCollector);

            frame.saveWithoutId(valueOutput);

            // Even though the entity saves the facing that it has, it's not going to be correct
            // Update the facing using the value we have after doing our rotations.
            valueOutput.putByte("Facing", (byte) facing.get3DDataValue());

            CompoundTag compoundTag = valueOutput.buildResult();

            TagValueInput valueInput = (TagValueInput) TagValueInput.create(scopedCollector,
                    frame.registryAccess(), compoundTag);

            frame.load(valueInput);
            StructureGenerator.updateEntityHangingBoundingBox(frame);
        }

        StructureGenerator.updateEntityHangingBoundingBox(frame);

        frame.snapTo(entityPos.getX() + x_axis_offset, entityPos.getY() + y_axis_offset,
                entityPos.getZ() + z_axis_offset, yaw,
                frame.getXRot());

        StructureGenerator.updateEntityHangingBoundingBox(frame);
        ChunkAccess chunk = structure.world.getChunkAt(entityPos);

        chunk.markUnsaved();

        return frame;
    }

    private static Entity setEntityFacingAndRotation(Entity entity, BuildEntity buildEntity, BlockPos entityPos, Structure structure) {
        float yaw = entity.getYRot();
        Rotation rotation = Rotation.NONE;
        double x_axis_offset = buildEntity.entityXAxisOffset;
        double z_axis_offset = buildEntity.entityZAxisOffset;
        Direction facing = structure.getClearSpace().getShape().getDirection();
        double y_axis_offset = buildEntity.entityYAxisOffset;
        Direction configurationDirection = structure.configuration.houseFacing.getOpposite();

        if (configurationDirection == facing.getOpposite()) {
            rotation = Rotation.CLOCKWISE_180;
            x_axis_offset = x_axis_offset * -1;
            z_axis_offset = z_axis_offset * -1;
        } else if (configurationDirection == facing.getClockWise()) {
            rotation = Rotation.CLOCKWISE_90;
            x_axis_offset = x_axis_offset * -1;
            z_axis_offset = z_axis_offset * -1;
        } else if (configurationDirection == facing.getCounterClockWise()) {
            rotation = Rotation.COUNTERCLOCKWISE_90;
            x_axis_offset = x_axis_offset * -1;
            z_axis_offset = z_axis_offset * -1;
        } else {
            x_axis_offset = 0;
            z_axis_offset = 0;
        }

        yaw = entity.rotate(rotation);

        entity.snapTo(entityPos.getX() + x_axis_offset, entityPos.getY() + y_axis_offset,
                entityPos.getZ() + z_axis_offset, yaw,
                entity.getXRot());

        return entity;
    }

    private static void updateEntityHangingBoundingBox(HangingEntity entity) {
        double d0 = (double) entity.getPos().getX() + 0.5D;
        double d1 = (double) entity.getPos().getY() + 0.5D;
        double d2 = (double) entity.getPos().getZ() + 0.5D;
        double d3 = 0.46875D;
        double d4 = entity.getBbWidth() % 32 == 0 ? 0.5D : 0.0D;
        double d5 = entity.getBbHeight() % 32 == 0 ? 0.5D : 0.0D;
        Direction horizontal = entity.getDirection();
        d0 = d0 - (double) horizontal.getStepX() * 0.46875D;
        d2 = d2 - (double) horizontal.getStepZ() * 0.46875D;
        d1 = d1 + d5;
        Direction direction = horizontal == Direction.DOWN || horizontal == Direction.UP ? horizontal.getOpposite() : horizontal.getCounterClockWise();
        d0 = d0 + d4 * (double) direction.getStepX();
        d2 = d2 + d4 * (double) direction.getStepZ();

        // The function call below set the following fields from the "entity" class. posX, posY, posZ.
        // This will probably have to change when the mappings get updated.
        entity.setPosRaw(d0, d1, d2);
        double d6 = entity.getBbWidth();
        double d7 = entity.getBbHeight();
        double d8 = entity.getBbWidth();

        if (horizontal.getAxis() == Direction.Axis.Z) {
            d8 = 1.0D;
        } else {
            d6 = 1.0D;
        }

        d6 = d6 / 32.0D;
        d7 = d7 / 32.0D;
        d8 = d8 / 32.0D;
        entity.setBoundingBox(new AABB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
    }

    private static CompoundTag updateTagDueToVersionUpdate(Entity entity, CompoundTag compoundTag) {
        if (entity instanceof Painting) {
            // In MC 1.19 some tags changed so convert them now.
            if (compoundTag.contains("Facing")) {
                byte facingByte = compoundTag.getByte("Facing").orElse((byte) 0);
                compoundTag.putByte("facing", facingByte);
            }

            if (compoundTag.contains("Motive")) {
                String motiveData = compoundTag.getString("Motive").orElse("");
                compoundTag.putString("variant", motiveData);
            }
        }

        return compoundTag;
    }
}
