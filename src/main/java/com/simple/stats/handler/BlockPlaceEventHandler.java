package com.simple.stats.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

import com.simple.stats.SimpleStats;
import com.simple.stats.annotations.ClientSide;
import com.simple.stats.annotations.ServerSide;
import com.simple.stats.client.gui.StatsHudGui;
import com.simple.stats.datastorage.PlayerNBTStorage;
import com.simple.stats.network.NetworkHandler;
import com.simple.stats.network.packet.PlaceBlockPacket;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

public class BlockPlaceEventHandler {

    public static final BlockPlaceEventHandler instance = new BlockPlaceEventHandler();
    public static final String PLACED_TAG_NAME = "placed";

    private final PlayerNBTStorage storage = new PlayerNBTStorage();

    private long blocksPlaced;

    private BlockPlaceEventHandler() {}

    public static void registerHandler() {
        FMLCommonHandler.instance()
            .bus()
            .register(instance);
        MinecraftForge.EVENT_BUS.register(instance);
    }

    @ServerSide
    @SubscribeEvent
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        EntityPlayer player = event.player;
        storage.setContextObject(player);
        if (blocksPlaced != Long.MAX_VALUE) {
            blocksPlaced += 1;
            storage.saveData(blocksPlaced, PLACED_TAG_NAME);
            NetworkHandler.sendToClient(new PlaceBlockPacket(blocksPlaced), (EntityPlayerMP) player);
        }

        SimpleStats.LOG.info("the block was the place. #" + blocksPlaced);
    }

    @ServerSide
    @SubscribeEvent
    public void onServerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        storage.setContextObject(event.player);
        storage.readData(PLACED_TAG_NAME)
            .ifPresent(v -> blocksPlaced = v);
        NetworkHandler.sendToClient(new PlaceBlockPacket(blocksPlaced), (EntityPlayerMP) event.player);
    }

    @ClientSide
    @SubscribeEvent
    public void onClientJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        StatsHudGui.setPlacedLevel(-1);
        StatsHudGui.resetPlacedCache();
    }

}
