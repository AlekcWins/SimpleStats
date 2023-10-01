package com.simple.stats.client.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

import com.simple.stats.LevelCacher;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StatsHudGui extends Gui {

    private static Long broken = 0L;
    private static Long placed = 0L;

    private final static int barHeight = 10;

    /**
     * -1 start reCalculate (when player change the server)
     */
    private static int brokenLevel = -1;

    /**
     * -1 start reCalculate (when player change the server)
     */
    private static int placedLevel = -1;

    private static boolean debug = true;

    private static final RenderItem renderItem = new RenderItem();
    private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    private static final TextureManager textureManager = Minecraft.getMinecraft()
        .getTextureManager();

    private final int red = 0xFFFFF000;
    private final int green = 0xFFFFF000;
    int fonColor = 0xFF000000; // Черный цвет

    int barWidth = 80;

    private static final LevelCacher.Func<Long> func = x -> {
        if (debug) {
            return (long) (10L * x + (10L * x) * 0.5 * x);
        }
        return (long) (500L * x + (500L * x) * 0.5 * x);
    };

    private static final LevelCacher<Long> placedCache = new LevelCacher<>(func);
    private static final LevelCacher<Long> brokenCache = new LevelCacher<>(func);

    public static void registerHandler() {
        MinecraftForge.EVENT_BUS.register(new StatsHudGui(Minecraft.getMinecraft(), fontRenderer));
    }

    public StatsHudGui(Minecraft mc, FontRenderer fontRenderer) {
        super();
    }

    @SubscribeEvent
    public void onRenderStatsHud(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        // placed
        placedLevel = placedCache.getLevel(placed, placedLevel);
        long placedPreviousLevelStats = placedCache.getMaxValueInLevel(placedLevel);
        float placedProgress = (float) (placed - placedPreviousLevelStats)
            / (placedCache.getMaxValueInLevel(placedLevel + 1) - placedPreviousLevelStats);
        drawTag(new ItemStack(Blocks.stone), 10, 5, placedProgress, placedLevel, Color.GREEN.getRGB());

        // broken
        brokenLevel = brokenCache.getLevel(broken, brokenLevel);
        long brokenPreviousLevelStats = brokenCache.getMaxValueInLevel(brokenLevel);
        float brokenProgress = (float) (broken - brokenPreviousLevelStats)
            / (brokenCache.getMaxValueInLevel(brokenLevel + 1) - brokenPreviousLevelStats);
        drawTag(new ItemStack(Items.diamond_pickaxe), 10, 20, brokenProgress, brokenLevel, Color.RED.getRGB());
    }

    private void drawTag(ItemStack itemStack, int x, int y, float progress, int level, int barColor) {
        if (itemStack != null && itemStack.getItem() != null) {
            renderItem.renderItemIntoGUI(fontRenderer, textureManager, itemStack, x, y);
            RenderHelper.disableStandardItemLighting();
        }
        String textLevel = Long.toString(level);
        drawProgressBar(x, y + 4, barWidth, progress, barColor, textLevel.length());

        fontRenderer.drawStringWithShadow(textLevel, x + 25, y + 5, Color.RED.getRGB());
        RenderHelper.disableStandardItemLighting();
    }

    private void drawProgressBar(int x, int y, int width, float progress, int barColor, int textLength) {

        // Рассчитайте ширину заполнения прогресс-бара на основе значения progress
        int coloredBar = barWidth;
        float progressBar = (float) Math.max(0, progress - 0.05);
        int filledWidth = Math.min((int) (coloredBar * progressBar), coloredBar);
        int spaceForText = textLength * 6 + 5;

        Gui.drawRect(
            x - 1 + 20,
            y - 1,
            x + width + 25 + 1 + spaceForText,
            y + barHeight + 1,
            new Color(0, 8, 20).getRGB()); // рамки
        Gui.drawRect(
            x + 25 + spaceForText,
            y,
            x + 25 + width + spaceForText,
            y + barHeight,
            new Color(0, 29, 61).getRGB()); // фон
        Gui.drawRect(
            x + 25 + spaceForText,
            y,
            x + 25 + spaceForText + filledWidth,
            y + barHeight,
            new Color(100, 53, 102).getRGB()); // шкала
    }

    public static void setBroken(Long broken) {
        StatsHudGui.broken = broken;
    }

    public static void setPlaced(Long placed) {
        StatsHudGui.placed = placed;
    }

    public static Long getBroken() {
        return broken;
    }

    public static Long getPlaced() {
        return placed;
    }

    public static void setBrokenLevel(int brokenLevel) {
        StatsHudGui.brokenLevel = brokenLevel;
    }

    public static void setPlacedLevel(int placedLevel) {
        StatsHudGui.placedLevel = placedLevel;
    }

    public static void resetPlacedCache() {
        StatsHudGui.placedCache.resetCache();
    }

    public static void resetBrokenCache() {
        StatsHudGui.brokenCache.resetCache();
    }
}
