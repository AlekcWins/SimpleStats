package com.simple.stats.handler;

import com.simple.stats.SimpleStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

import com.simple.stats.datastorage.IStorageData;
import com.simple.stats.datastorage.PlayerNBTStorage;
import com.simple.stats.network.NetworkHandler;
import com.simple.stats.network.packet.BrokenBlockPacket;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

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

    @SubscribeEvent
    public void onBlockBreak(BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        storage.setContextObject(player);
        blocksBroken += 1;
        storage.saveData(blocksBroken, BROKEN_TAG_NAME);
        NetworkHandler.sendToClient(new BrokenBlockPacket(blocksBroken), (EntityPlayerMP) player);
        SimpleStats.LOG.info("the block was broken. #" + blocksBroken);
    }

    // this work for single server and multiplayer
    @SubscribeEvent
    public void onServerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        storage.setContextObject(event.player);
        storage.readData(BROKEN_TAG_NAME)
            .ifPresent(v -> blocksBroken = v);
        NetworkHandler.sendToClient(new BrokenBlockPacket(blocksBroken), (EntityPlayerMP) event.player);
    }

}
