package com.simple.stats.handler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BlockPlace {

    private BlockPlace() {}

    public static void registerHandler() {
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventHandler());
    }

    public static class BlockBreakEventHandler {

        private int blocksPlaced = -1;

        @SubscribeEvent
        public void onBlockPlace(BlockEvent.PlaceEvent event) {
            if (blocksPlaced == -1) {
                blocksPlaced = playerNBT.readDataFromPlayerNBT(event.player, "placed");
            }
            blocksPlaced += 1;

            playerNBT.writeDataToPlayerNBT(event.player, "placed", blocksPlaced);
            System.out.println("the block was the place. #" + blocksPlaced);
        }
    }
}
