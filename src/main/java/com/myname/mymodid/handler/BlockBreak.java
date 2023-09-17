package com.myname.mymodid.handler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BlockBreak {

    public BlockBreak() {
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventHandler());
    }

    public static class BlockBreakEventHandler {

        @SubscribeEvent
        public void onBlockBreak(BreakEvent event) {
            System.out.println("the block was broken");
        }
    }
}
