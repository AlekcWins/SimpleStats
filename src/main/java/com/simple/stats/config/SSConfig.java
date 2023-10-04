package com.simple.stats.config;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.awt.Color;
import java.io.File;

public class SSConfig {

    // bar colorss
    public static int barBackgroundColor = new Color(0, 8, 20).getRGB();
    public static int barProgressBackgroundColor = new Color(0, 29, 61).getRGB();
//    public static int barProgressColor = new Color(100, 53, 102).getRGB();
    public static int barTextStatusColor = new Color(255, 255, 255).getRGB();
    public static int barPercentColorPlaced = new Color(39, 154, 8).getRGB();
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

        config.setCategoryComment("bar colors", "All values are an integer from a RGB color.");

        barBackgroundColor = config.getInt("barBackgroundColor", "bar colors", barBackgroundColor, -16777216, -1, "Bar background color");
        barProgressBackgroundColor = config.getInt("barProgressBackgroundColor", "bar colors", barBackgroundColor, -16777216, -1, "Bar progress background color");
//        barProgressColor = config.getInt("barProgressColor", "bar colors", barProgressColor, -16777216, -1, "Bar progress color by default");
        barTextStatusColor = config.getInt("barPercentColorBroken", "bar colors", barTextStatusColor, -16777216, -1, "Bar progress color for Broken");
        barPercentColorPlaced =  config.getInt("barPercentColorPlaced", "bar colors", barPercentColorPlaced, -16777216, -1, "Bar progress color for Placed");
        barPercentColor = config.getInt("barPercentColor", "bar colors", barPercentColor, -16777216, -1, "Bar percent color");
        barLevelColor = config.getInt("barLevelColor", "bar colors", barLevelColor, -16777216, -1, "Bar level color");

//        barWidth = config.getInt("barWidth", "bar size", barWidth, 40, 200, "Bar width");
//        barHeight = config.getInt("barHeight", "bar size", barHeight, 5, 20, "Bar height");

        String itemBrokenName = config.getString("itemBrokenName", "bar items", "stone", "Item for broken bar");
        String itemPlacedName = config.getString("itemPlacedName", "bar items", "diamond_pickaxe", "Item for placed bar");
        itemForBrokenBar = findItemStack(itemBrokenName, new ItemStack(Blocks.stone));
        itemForPlacedBar = findItemStack(itemPlacedName, new ItemStack(Items.diamond_pickaxe));

        if (config.hasChanged()) {
            config.save();
        }
    }

    private ItemStack findItemStack(String aItem, ItemStack aReplacement) {
        ItemStack result = GameRegistry.findItemStack("minecraft", aItem, 1);

        if (result == null) {
            return aReplacement;
        }
        return result;
    }


}
