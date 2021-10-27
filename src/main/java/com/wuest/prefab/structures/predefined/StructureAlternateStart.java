package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.config.EntityPlayerConfiguration;
import com.wuest.prefab.proxy.messages.PlayerEntityTagMessage;
import com.wuest.prefab.structures.base.BuildBlock;
import com.wuest.prefab.structures.base.BuildingMethods;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.HouseConfiguration;
import com.wuest.prefab.structures.config.StructureConfiguration;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
@SuppressWarnings({"unused", "ConstantConditions", "UnusedAssignment"})
public class StructureAlternateStart extends Structure {
    private BlockPos chestPosition = null;
    private BlockPos furnacePosition = null;
    private BlockPos trapDoorPosition = null;

    @Override
    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
                                                   Direction assumedNorth, Block foundBlock, BlockState blockState, PlayerEntity player) {
        HouseConfiguration houseConfig = (HouseConfiguration) configuration;

        if ((!houseConfig.addBed && foundBlock instanceof BedBlock) || (!houseConfig.addChest && foundBlock instanceof ChestBlock)
                || (!houseConfig.addTorches && foundBlock instanceof TorchBlock)
                || (!houseConfig.addCraftingTable && foundBlock instanceof CraftingTableBlock)
                || (!houseConfig.addFurnace && foundBlock instanceof FurnaceBlock)
                || (!houseConfig.addChest && foundBlock instanceof BarrelBlock)
                || (foundBlock instanceof SeaGrassBlock)
                || (foundBlock instanceof TallSeaGrassBlock)) {
            // Don't place the block, returning true means that this has been
            // "handled"
            return true;
        }

        if (foundBlock instanceof FurnaceBlock) {
            this.furnacePosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock instanceof TrapDoorBlock && houseConfig.addMineShaft && this.trapDoorPosition == null) {
            // The trap door will still be added, but the mine shaft may not be
            // built.
            this.trapDoorPosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if ((foundBlock instanceof ChestBlock && this.chestPosition == null)
                || (foundBlock instanceof BarrelBlock && this.chestPosition == null)) {
            this.chestPosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock == Blocks.SPONGE && houseConfig.addMineShaft) {
            // Sponges are sometimes used in-place of trapdoors when trapdoors are used for decoration.
            this.trapDoorPosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing).above();
        } else if (foundBlock instanceof BedBlock && houseConfig.addBed) {
            BlockPos bedHeadPosition = block.getStartingPosition().getRelativePosition(originalPos, this.getClearSpace().getShape().getDirection(), configuration.houseFacing);
            BlockPos bedFootPosition = block.getSubBlock().getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);

            Tuple<BlockState, BlockState> blockStateTuple = BuildingMethods.getBedState(bedHeadPosition, bedFootPosition, houseConfig.bedColor);
            block.setBlockState(blockStateTuple.getFirst());
            block.getSubBlock().setBlockState(blockStateTuple.getSecond());

            this.priorityOneBlocks.add(block);

            // Return true so the bed is not placed.
            return true;
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
        HouseConfiguration houseConfig = (HouseConfiguration) configuration;
        EntityPlayerConfiguration playerConfig = EntityPlayerConfiguration.loadFromEntityData(player);

        ArrayList<BlockPos> furnacePositions = new ArrayList<>();

        if (this.furnacePosition != null) {
            furnacePositions.add(this.furnacePosition);
        }

        BuildingMethods.FillFurnaces(world, furnacePositions);

        if (this.chestPosition != null && houseConfig.addChestContents) {
            // Fill the chest.
            BuildingMethods.FillChest(world, this.chestPosition);
        }

        if (this.trapDoorPosition != null && this.trapDoorPosition.getY() > 15 && houseConfig.addMineShaft) {
            // Build the mineshaft.
            BuildingMethods.PlaceMineShaft(world, this.trapDoorPosition.below(), houseConfig.houseFacing, false);
        }

        // Make sure to set this value so the player cannot fill the chest a second time.
        playerConfig.builtStarterHouse = true;
        playerConfig.saveToPlayer(player);

        // Make sure to send a message to the client to sync up the server player information and the client player
        // information.
        Prefab.network.sendTo(new PlayerEntityTagMessage(playerConfig.getModIsPlayerNewTag(player)), ((ServerPlayerEntity) player).connection.connection,
                NetworkDirection.PLAY_TO_CLIENT);
    }

    @Override
    protected boolean hasGlassColor(StructureConfiguration configuration) {
        return true;
    }

    @Override
    protected FullDyeColor getGlassColor(StructureConfiguration configuration) {
        HouseConfiguration houseConfig = (HouseConfiguration) configuration;
        return houseConfig.glassColor;
    }
}
