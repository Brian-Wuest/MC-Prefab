package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.config.EntityPlayerConfiguration;
import com.wuest.prefab.proxy.messages.PlayerEntityTagMessage;
import com.wuest.prefab.structures.base.BuildBlock;
import com.wuest.prefab.structures.base.BuildingMethods;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.AdvancedHouseConfiguration;
import com.wuest.prefab.structures.config.StructureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkDirection;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
public class StructureAdvancedHouse extends Structure {
    private BlockPos chestPosition = null;
    private ArrayList<BlockPos> furnacePosition = null;
    private BlockPos trapDoorPosition = null;

    @Override
    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, Level world, BlockPos originalPos,
                                                   Block foundBlock, BlockState blockState, Player player) {

        AdvancedHouseConfiguration houseConfiguration = (AdvancedHouseConfiguration) configuration;

        if (foundBlock instanceof FurnaceBlock) {
            if (this.furnacePosition == null) {
                this.furnacePosition = new ArrayList<>();
            }

            this.furnacePosition.add(block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing));
        } else if (foundBlock instanceof ChestBlock && this.chestPosition == null) {
            this.chestPosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock instanceof TrapDoorBlock && this.trapDoorPosition == null) {
            // The trap door will still be added, but the mine shaft may not be
            // built.
            this.trapDoorPosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock == Blocks.SPONGE) {
            // Sponges are sometimes used in-place of trapdoors when trapdoors are used for decoration.
            this.trapDoorPosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing).above();
        } else if (foundBlock instanceof BedBlock) {
            BlockPos bedHeadPosition = block.getStartingPosition().getRelativePosition(originalPos, this.getClearSpace().getShape().getDirection(), configuration.houseFacing);
            BlockPos bedFootPosition = block.getSubBlock().getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);

            Tuple<BlockState, BlockState> blockStateTuple = BuildingMethods.getBedState(bedHeadPosition, bedFootPosition, houseConfiguration.bedColor);
            block.setBlockState(blockStateTuple.getFirst());
            block.getSubBlock().setBlockState(blockStateTuple.getSecond());
            this.priorityOneBlocks.add(block);

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
     * @param player        The player which initiated the construction.
     */
    @Override
    public void AfterBuilding(StructureConfiguration configuration, ServerLevel world, BlockPos originalPos, Player player) {
        AdvancedHouseConfiguration houseConfig = (AdvancedHouseConfiguration) configuration;
        EntityPlayerConfiguration playerConfig = EntityPlayerConfiguration.loadFromEntityData(player);

        BuildingMethods.FillFurnaces(world, this.furnacePosition);

        if (this.chestPosition != null && !playerConfig.builtStarterHouse) {
            // Fill the chest if the player hasn't generated the starting house yet.
            BuildingMethods.FillChest(world, this.chestPosition);
        }

        int minimumHeightForMineshaft = world.getMinBuildHeight() + 21;

        if (this.trapDoorPosition != null && this.trapDoorPosition.getY() > minimumHeightForMineshaft && houseConfig.addMineshaft) {
            // Build the mineshaft.
            BuildingMethods.PlaceMineShaft(world, this.trapDoorPosition.below(), houseConfig.houseFacing, false);
        }

        // Make sure to set this value so the player cannot fill the chest a second time.
        playerConfig.builtStarterHouse = true;
        playerConfig.saveToPlayer(player);

        // Make sure to send a message to the client to sync up the server player information and the client player
        // information.
        Prefab.network.sendTo(new PlayerEntityTagMessage(playerConfig.getModIsPlayerNewTag(player)), ((ServerPlayer) player).connection.connection,
                NetworkDirection.PLAY_TO_CLIENT);
    }

}