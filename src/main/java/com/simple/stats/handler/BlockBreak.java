package com.simple.stats.handler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BlockBreak {

    private BlockBreak() {}

    public static void registerHandler() {
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventHandler());
    }

    public static class BlockBreakEventHandler {

        private int blocksPlaced = -1;

        @SubscribeEvent
        public void onBlockBreak(BreakEvent event) {
            if (blocksPlaced == -1) {
                blocksPlaced = playerNBT.readDataFromPlayerNBT(event.getPlayer(), "placed");
            }
            blocksPlaced += 1;

            playerNBT.writeDataToPlayerNBT(event.getPlayer(), "broken", blocksPlaced);
            System.out.println("the block was broken. #" + blocksPlaced);
        }
    }
}
