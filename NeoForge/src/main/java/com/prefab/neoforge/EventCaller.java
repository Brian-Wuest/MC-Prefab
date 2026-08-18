package com.prefab.neoforge;

import com.prefab.IEventCaller;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

public class EventCaller implements IEventCaller {
    @Override
    public boolean canBreakBlock(ServerLevel world, Player player, BlockState blockState, BlockPos blockPos) {
        GameType gameType = ((ServerPlayer)player).gameMode.getGameModeForPlayer();
        if (player.blockActionRestricted(world, blockPos, gameType)) {
            return false;
        }

        if (blockState.getBlock() instanceof GameMasterBlock && !player.canUseGameMasterBlocks()) {
            return false;
        }

        // Post the block break event
        BreakBlockEvent event = new BreakBlockEvent(world, blockPos, blockState, player);
        event.setCanceled(false);
        NeoForge.EVENT_BUS.post(event);

        return !event.isCanceled();
    }
}
