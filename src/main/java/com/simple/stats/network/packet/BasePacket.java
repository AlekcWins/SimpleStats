package com.simple.stats.network.packet;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

//todo two class
public abstract class BasePacket<REQ extends IMessage> implements IMessage, IMessageHandler<REQ, REQ> {

    @Override
    public REQ onMessage(REQ message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            handleServerSide(message, ctx.getServerHandler().playerEntity);
        } else {
            // todo null???
            handleClientSide(message, null);
        }
        // todo null???
        return null;
    }

    public abstract void handleClientSide(REQ message, EntityPlayer p);

    public abstract void handleServerSide(REQ message, EntityPlayer p);
}
