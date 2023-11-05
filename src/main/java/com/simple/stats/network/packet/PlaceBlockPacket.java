package com.simple.stats.network.packet;

import com.simple.stats.SimpleStats;
import com.simple.stats.config.SSConfig;
import net.minecraft.entity.player.EntityPlayer;

import com.simple.stats.annotations.ClientSide;
import com.simple.stats.annotations.ServerSide;
import com.simple.stats.client.gui.StatsHudGui;

import io.netty.buffer.ByteBuf;

public class PlaceBlockPacket extends BasePacket<PlaceBlockPacket> {

    long placed;

    public PlaceBlockPacket() {
    }

    public PlaceBlockPacket(long placed) {
        this.placed = placed;
    }


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
        if (SSConfig.DEBUG) {
            SimpleStats.LOG.debug("CLIENT_SIDE: " + StatsHudGui.getPlaced());
        }
    }

    @ServerSide
    @Override
    public void handleServerSide(PlaceBlockPacket message, EntityPlayer p) {
        if (SSConfig.DEBUG) {
            SimpleStats.LOG.debug("SERVER_SIDE: " + StatsHudGui.getPlaced());
        }
    }
}
