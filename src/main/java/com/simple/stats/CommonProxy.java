package com.simple.stats;

import com.simple.stats.config.SSConfig;
import com.simple.stats.handler.BlockBreakEventHandler;
import com.simple.stats.handler.BlockPlaceEventHandler;
import com.simple.stats.network.NetworkHandler;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {
    public static final SSConfig config = new SSConfig();

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        SimpleStats.LOG.info("I am " + Tags.MODNAME + " at version " + Tags.VERSION);
        config.processConfig(event.getSuggestedConfigurationFile());
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        BlockPlaceEventHandler.registerHandler();
        BlockBreakEventHandler.registerHandler();
        NetworkHandler.registerHandler();
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
    }
}
