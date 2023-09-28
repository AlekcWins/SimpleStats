package com.simple.stats.network.packet;

import net.minecraft.entity.player.EntityPlayer;

import com.simple.stats.annotations.ClientSide;
import com.simple.stats.annotations.ServerSide;
import com.simple.stats.client.gui.StatsHudGui;

import io.netty.buffer.ByteBuf;

public class PlaceBlockPacket extends BasePacket<PlaceBlockPacket> {

    public PlaceBlockPacket() {}

    public PlaceBlockPacket(long placed) {
        this.placed = placed;
    }

    long placed;

    @Override
    public void fromBytes(ByteBuf buf) {
        placed = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(placed);
    }

    @ClientSide
    @Override
    public void handleClientSide(PlaceBlockPacket message, EntityPlayer p) {
        StatsHudGui.setPlaced(message.placed);
        // SimpleStats.LOG.info("CLIENT_SIDE: " + StatsHudGui.getPlaced());
    }

    @ServerSide
    @Override
    public void handleServerSide(PlaceBlockPacket message, EntityPlayer p) {
        // nothing
        // SimpleStats.LOG.info("SERVER_SIDE: " + StatsHudGui.getPlaced());
    }
}
