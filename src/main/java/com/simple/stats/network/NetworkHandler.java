package com.simple.stats.network;

import net.minecraft.entity.player.EntityPlayerMP;

import com.simple.stats.Tags;
import com.simple.stats.network.packet.BrokenBlockPacket;
import com.simple.stats.network.packet.PlaceBlockPacket;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public final class NetworkHandler {

    private static SimpleNetworkWrapper INSTANCE;

    public static void registerHandler() {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MODID);
        INSTANCE.registerMessage(BrokenBlockPacket.class, BrokenBlockPacket.class, 0, Side.CLIENT);
        // INSTANCE.registerMessage(BrokenBlockPacket.class, BrokenBlockPacket.class, 0, Side.SERVER);

        INSTANCE.registerMessage(PlaceBlockPacket.class, PlaceBlockPacket.class, 1, Side.CLIENT);
        // INSTANCE.registerMessage(PlaceBlockPacket.class, PlaceBlockPacket.class, 1, Side.SERVER);
    }

    public static void sendToClient(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendTo(message, player);
    }
}
