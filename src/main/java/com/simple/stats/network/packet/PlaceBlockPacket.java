package com.simple.stats.network.packet;

import com.simple.stats.SimpleStats;
import net.minecraft.entity.player.EntityPlayer;

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

    @Override
    public void handleClientSide(PlaceBlockPacket message, EntityPlayer p) {
        StatsHudGui.placed = message.placed;
        SimpleStats.LOG.info("CLIENT_SIDE: " + placed);
    }

    @Override
    public void handleServerSide(PlaceBlockPacket message, EntityPlayer p) {
        // nothing
        SimpleStats.LOG.info("SERVER_SIDE: " + placed);
    }
}
