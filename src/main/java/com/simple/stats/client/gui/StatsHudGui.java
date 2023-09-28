package com.simple.stats.client.gui;

import java.awt.*;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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

    /**
     * -1 start reCalculate (when player change the server)
     */
    private static int brokenLevel = -1;

    /**
     * -1 start reCalculate (when player change the server)
     */
    private static int placedLevel = -1;

    private static boolean debug = true;
    private static final LevelCacher.Func<Long> func = x -> {
        if (debug) {
            return (long) (10L * x + (10L * x) * 0.5 * x);
        }
        return (long) (500L * x + (500L * x) * 0.5 * x);
    };

    private static LevelCacher<Long> placedCache = new LevelCacher<>(func);
    private static LevelCacher<Long> brokenCache = new LevelCacher<>(func);

    private Minecraft mc;
    private static final RenderItem renderItem = new RenderItem();
    private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    private static final TextureManager textureManager = Minecraft.getMinecraft()
        .getTextureManager();

    private final int red = 0xFFFFF000;
    private final int green = 0xFFFFF000;
    int fonColor = 0xFF000000; // Черный цвет

    int barLength = 80;

    public static void registerHandler() {
        MinecraftForge.EVENT_BUS.register(new StatsHudGui(Minecraft.getMinecraft(), fontRenderer));
    }

    public StatsHudGui(Minecraft mc, FontRenderer fontRenderer) {
        super();
        this.mc = mc;
    }

    @SubscribeEvent
    public void onRenderStatsHud(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        // y= 500*x + (500*x) * 0.5 * x
        // y(1) = 500 + 500
        // y(2) = 1000 + 2000
        // y(3) = 1500 + 4500
        // ...
        // y(20) = 10_000 +100_000
        // y(100) = 50_000 +50_000
        //
        drawTag(Blocks.stone, 10, 5, getPlaced(), green); // placed
        placedLevel = placedCache.getLevel(placed, placedLevel);
        fontRenderer.drawString(Long.toString(placedLevel), 0, 5, Color.GREEN.getRGB());
        RenderHelper.disableStandardItemLighting();

        drawTag(Items.diamond_pickaxe, 10, 20, getBroken(), red); // broken
        brokenLevel = brokenCache.getLevel(broken, StatsHudGui.brokenLevel);
        fontRenderer.drawString(Long.toString(brokenLevel), 10, 2, Color.RED.getRGB());
        RenderHelper.disableStandardItemLighting();
    }

    private void drawProgressBar(int x, int y, int width, int height, float progress, int barColor) {

        // Рассчитайте ширину заполнения прогресс-бара на основе значения progress
        int filledWidth = Math.min((int) (width * progress), barLength);

        // Отрисуйте фон прогресс-бара
        Gui.drawRect(x, y, x + width, y + height, fonColor);

        // Отрисуйте заполненную часть прогресс-бара
        Gui.drawRect(x, y, x + filledWidth, y + height, barColor);
    }

    private void drawTag(Block block, int x, int y, long currentLevelAmount, int barColor) {
        if (block != null) drawTag(new ItemStack(block), x, y, currentLevelAmount, barColor);
        else drawTag((ItemStack) null, x, y, currentLevelAmount, barColor);
    }

    private void drawTag(Item item, int x, int y, long currentLevelAmount, int barColor) {
        if (item != null) drawTag(new ItemStack(item), x, y, currentLevelAmount, barColor);
        else drawTag((ItemStack) null, x, y, currentLevelAmount, barColor);
    }

    private void drawTag(ItemStack itemStack, int x, int y, long currentLevelAmount, int barColor) {
        if (itemStack != null && itemStack.getItem() != null) {
            renderItem.renderItemIntoGUI(fontRenderer, textureManager, itemStack, x, y);
            RenderHelper.disableStandardItemLighting();
        }
        drawProgressBar(x + 20, y + 3, barLength, 10, currentLevelAmount / 100f, barColor);
        RenderHelper.disableStandardItemLighting();
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
