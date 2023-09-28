package com.simple.stats.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

import com.simple.stats.SimpleStats;
import com.simple.stats.annotations.ClientSide;
import com.simple.stats.annotations.ServerSide;
import com.simple.stats.client.gui.StatsHudGui;
import com.simple.stats.datastorage.IStorageData;
import com.simple.stats.datastorage.PlayerNBTStorage;
import com.simple.stats.network.NetworkHandler;
import com.simple.stats.network.packet.BrokenBlockPacket;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

public class BlockBreakEventHandler {

    public static final BlockBreakEventHandler instance = new BlockBreakEventHandler();
    public static final String BROKEN_TAG_NAME = "broken";

    private final IStorageData<Long, EntityPlayer> storage = new PlayerNBTStorage();

    private long blocksBroken;

    private BlockBreakEventHandler() {}

    public static void registerHandler() {
        FMLCommonHandler.instance()
            .bus()
            .register(instance);
        MinecraftForge.EVENT_BUS.register(instance);
    }

    @ServerSide
    @SubscribeEvent
    public void onBlockBreak(BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        storage.setContextObject(player);
        if (blocksBroken != Long.MAX_VALUE) {
            blocksBroken += 1;
            storage.saveData(blocksBroken, BROKEN_TAG_NAME);
            NetworkHandler.sendToClient(new BrokenBlockPacket(blocksBroken), (EntityPlayerMP) player);
        }

        SimpleStats.LOG.info("the block was broken. #" + blocksBroken);
    }

    @ServerSide
    @SubscribeEvent
    public void onServerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        SimpleStats.LOG.info("onServerJoin");

        storage.setContextObject(event.player);
        storage.readData(BROKEN_TAG_NAME)
            .ifPresent(v -> blocksBroken = v);
        NetworkHandler.sendToClient(new BrokenBlockPacket(blocksBroken), (EntityPlayerMP) event.player);
    }

    @ClientSide
    @SubscribeEvent
    public void onClientJoin(ClientConnectedToServerEvent event) {
        StatsHudGui.setBrokenLevel(-1);
        StatsHudGui.resetPlacedCache();
    }

}
