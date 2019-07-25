package com.wuest.prefab.Events;

import com.wuest.prefab.Blocks.BlockCompressedStone;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.ArrayList;

/**
 * This is the server side event hander.
 *
 * @author WuestMan
 */
@EventBusSubscriber(modid = Prefab.MODID, value = {Dist.DEDICATED_SERVER})
public final class ModEventHandler {
    /**
     * Determines the affected blocks by redstone power.
     */
    public static ArrayList<BlockPos> RedstoneAffectedBlockPositions = new ArrayList<>();

    static {
        ModEventHandler.RedstoneAffectedBlockPositions = new ArrayList<>();
    }

    /**
     * This event occurs when a player logs in. This is used to send server configuration to the client.
     *
     * @param event The event object.
     */
    @SubscribeEvent
    public static void onPlayerLoginEvent(PlayerLoggedInEvent event) {
        if (!event.getPlayer().world.isRemote) {
            CompoundNBT tag = CommonProxy.proxyConfiguration.serverConfiguration.ToNBTTagCompound();
            Prefab.network.sendTo(new ConfigSyncMessage(tag), ((ServerPlayerEntity) event.getPlayer()).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
            System.out.println("Sent config to '" + event.getPlayer().getDisplayName().getString() + "'.");
        }
    }

    @SubscribeEvent
    public static void AnvilUpdate(AnvilUpdateEvent event) {
        ItemStack rightItem = event.getRight();
        ItemStack leftItem = event.getLeft();
        Item tripleCompressedStone = ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE).getItem();

        if (rightItem.getItem() == tripleCompressedStone || leftItem.getItem() == tripleCompressedStone) {
            if (rightItem.getItem() == ModRegistry.Bulldozer() || leftItem.getItem() == ModRegistry.Bulldozer()) {
                event.setCost(4);
                ItemStack bulldozer = rightItem.getItem() == ModRegistry.Bulldozer() ? rightItem : leftItem;

                ItemStack outputStack = new ItemStack(ModRegistry.Bulldozer());
                ModRegistry.Bulldozer().setPoweredValue(outputStack, true);
                outputStack.setDamage(0);
                event.setOutput(outputStack);
            }
        }
    }
}
