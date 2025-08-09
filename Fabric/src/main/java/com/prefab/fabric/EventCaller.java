package com.prefab.fabric;

import com.prefab.IEventCaller;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.state.BlockState;

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

        return PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(world, player, blockPos, blockState, null);
    }
}
