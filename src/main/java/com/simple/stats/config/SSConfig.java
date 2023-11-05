package com.simple.stats.config;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.awt.Color;
import java.io.File;

public class SSConfig {
    // TODO: remove before commmit->  переделать имена констант
    public static boolean DEBUG = false;

    // bar colors
    public static int barBackgroundColor = new Color(0, 8, 20).getRGB();
    public static int barProgressBackgroundColor = new Color(0, 29, 61).getRGB();
    public static int barProgressBackgroundColorBroken = new Color(58, 168, 14).getRGB();
    public static int barProgressBackgroundColorPlaced = new Color(39, 154, 8).getRGB();
    public static int barPercentColor = Color.RED.getRGB();
    public static int barLevelColor = Color.RED.getRGB();

    // bar size
    public static int barWidth = 80;
    public static int barHeight = 10;

    // bar items
    public static ItemStack itemForBrokenBar;
    public static ItemStack itemForPlacedBar;

    //settings
    public static boolean settingShowPercentToShowStatusInBar = true;

    public void processConfig(File configFile) {
        Configuration config = new Configuration(configFile);

        settingShowPercentToShowStatusInBar = config.getBoolean("barTextStatusColor", "settings", settingShowPercentToShowStatusInBar, "Use percent for show bar status");
        DEBUG = config.getBoolean("debug", "settings", DEBUG, "Use debug");

        config.setCategoryComment("bar colors", "All values are an integer from a RGB color.");

        barBackgroundColor = config.getInt("barBackgroundColor", "bar colors", barBackgroundColor, -16777216, -1, "Bar background color");
        barProgressBackgroundColor = config.getInt("barProgressBackgroundColor", "bar colors", barBackgroundColor, -16777216, -1, "Bar progress background color");
        barProgressBackgroundColorBroken = config.getInt("barPercentColorBroken", "bar colors", barProgressBackgroundColorBroken, -16777216, -1, "Bar progress color for Broken");
        barProgressBackgroundColorPlaced = config.getInt("barPercentColorPlaced", "bar colors", barProgressBackgroundColorPlaced, -16777216, -1, "Bar progress color for Placed");
        barPercentColor = config.getInt("barPercentColor", "bar colors", barPercentColor, -16777216, -1, "Bar percent color");
        barLevelColor = config.getInt("barLevelColor", "bar colors", barLevelColor, -16777216, -1, "Bar level color");

        String itemPlacedName = config.getString("itemBrokenName", "bar items", "stone", "Item for broken bar");
        String itemBrokenName = config.getString("itemPlacedName", "bar items", "diamond_pickaxe", "Item for placed bar");
        itemForPlacedBar = findItemStack(itemPlacedName, new ItemStack(Blocks.stone));
        itemForBrokenBar = findItemStack(itemBrokenName, new ItemStack(Items.diamond_pickaxe));

        if (config.hasChanged()) {
            config.save();
        }
    }

    private ItemStack findItemStack(String findItemName, ItemStack defaultItem) {
        ItemStack result = GameRegistry.findItemStack("minecraft", findItemName, 1);

        if (result == null) {
            return defaultItem;
        }
        return result;
    }


}
