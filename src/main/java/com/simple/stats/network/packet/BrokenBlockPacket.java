package com.simple.stats.network.packet;

import com.simple.stats.SimpleStats;
import net.minecraft.entity.player.EntityPlayer;

import com.simple.stats.client.gui.StatsHudGui;

import io.netty.buffer.ByteBuf;

public class BrokenBlockPacket extends BasePacket<BrokenBlockPacket> {

    public BrokenBlockPacket() {}

    public BrokenBlockPacket(long broken) {
        this.broken = broken;
    }

    long broken;

    @Override
    public void fromBytes(ByteBuf buf) {
        broken = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(broken);
    }

    @Override
    public void handleClientSide(BrokenBlockPacket message, EntityPlayer p) {
        StatsHudGui.broken = message.broken;
        SimpleStats.LOG.info("CLIENT_SIDE: " + StatsHudGui.broken);
    }

    @Override
    public void handleServerSide(BrokenBlockPacket message, EntityPlayer p) {
        SimpleStats.LOG.info("SERVER_SIDE: " + broken);
    }
}
