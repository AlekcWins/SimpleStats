package com.simple.stats.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

public class BlockPlace {

    private BlockPlace() {
    }

    public static void registerHandler() {
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventHandler());
    }

    public static class BlockBreakEventHandler {

        @SubscribeEvent
        public void onBlockPlace(BlockEvent.PlaceEvent event) {
            System.out.println("the block was the place");
        }
    }
}
