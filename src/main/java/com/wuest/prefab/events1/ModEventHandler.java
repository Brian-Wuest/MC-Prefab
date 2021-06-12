package com.wuest.prefab.Events;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;
import com.wuest.prefab.Structures.Items.ItemBulldozer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.ArrayList;

/**
 * This is the server side event handler.
 *
 * @author WuestMan
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
@EventBusSubscriber(modid = Prefab.MODID)
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
        if (!event.getPlayer().level.isClientSide) {
            CompoundNBT tag = CommonProxy.proxyConfiguration.serverConfiguration.ToNBTTagCompound();
            Prefab.network.sendTo(new ConfigSyncMessage(tag), ((ServerPlayerEntity) event.getPlayer()).connection.connection, NetworkDirection.PLAY_TO_CLIENT);

            Prefab.LOGGER.info("Sent config to '" + event.getPlayer().getDisplayName().getString() + "'.");
        }
    }

    @SubscribeEvent
    public static void AnvilUpdate(AnvilUpdateEvent event) {
        ItemStack rightItem = event.getRight();
        ItemStack leftItem = event.getLeft();
        Item tripleCompressedStone = ModRegistry.TripleCompressedStoneItem.get();

        ItemBulldozer bulldozer = ModRegistry.Bulldozer.get();
        if (rightItem.getItem() == tripleCompressedStone || leftItem.getItem() == tripleCompressedStone) {
            if (rightItem.getItem() == bulldozer || leftItem.getItem() == bulldozer) {
                event.setCost(4);

                ItemStack outputStack = new ItemStack(bulldozer);
                bulldozer.setPoweredValue(outputStack, true);
                outputStack.setDamageValue(0);
                event.setOutput(outputStack);
            }
        }
    }
}
