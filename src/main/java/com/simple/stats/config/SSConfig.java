package com.simple.stats.config;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.awt.Color;
import java.io.File;

public class SSConfig {

    // bar colors
    public static int barBackgroundColor = new Color(0, 8, 20).getRGB();
    public static int barProgressBackgroundColor = new Color(0, 29, 61).getRGB();
    public static int barProgressColor = new Color(100, 53, 102).getRGB();
    public static int barPercentColor = Color.RED.getRGB();
    public static int barLevelColor = Color.RED.getRGB();

    // bar size
    public static int barWidth = 80;
    public static int barHeight = 10;

    // bar items
    public static ItemStack itemForBrokenBar;
    public static ItemStack itemForPlacedBar;


    public void processConfig(File configFile) {
        Configuration config = new Configuration(configFile);

        config.setCategoryComment("bar color", "All values are an integer from a RGB color.");

        barBackgroundColor = config.getInt("barBackgroundColor", "bar color", barBackgroundColor, -16777216, -1, "Bar background color");
        barProgressBackgroundColor = config.getInt("barProgressBackgroundColor", "bar color", barBackgroundColor, -16777216, -1, "Bar progress background color");
        barProgressColor = config.getInt("barProgressColor", "bar color", barProgressColor, -16777216, -1, "Bar progress color");
        barPercentColor = config.getInt("barPercentColor", "bar color", barPercentColor, -16777216, -1, "Bar percent color");
        barLevelColor = config.getInt("barLevelColor", "bar color", barLevelColor, -16777216, -1, "Bar level color");

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
