package com.simple.stats.client.gui;


import com.simple.stats.config.SSConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import com.simple.stats.LevelCacher;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.text.DecimalFormat;

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


    private static final RenderItem renderItem = new RenderItem();
    private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    private static final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();


    /**
     * Formula for calculate level
     */
    private static final LevelCacher.Func<Long> func = x -> {
        if (SSConfig.DEBUG) {
            return (long) (10L * x + (10L * x) * 0.5 * x);
        }
        return (long) (500L * x + (500L * x) * 0.5 * x);
    };

    private static final LevelCacher<Long> placedCache = new LevelCacher<>(func);
    private static final LevelCacher<Long> brokenCache = new LevelCacher<>(func);

    public StatsHudGui(Minecraft mc, FontRenderer fontRenderer) {
        super();
    }

    public static void registerHandler() {
        MinecraftForge.EVENT_BUS.register(new StatsHudGui(Minecraft.getMinecraft(), fontRenderer));
    }

    @SubscribeEvent
    public void onRenderStatsHud(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        showPlacedBar();
        showBrokenBar();
    }

    private void showPlacedBar() {
        placedLevel = placedCache.getLevel(placed, placedLevel);
        long maxStatForCurrLevel = placedCache.getMaxValueInLevel(placedLevel);

        // TODO: remove before commmit->  вынести в отдельный метод
        float placedProgress = (float) (placed - maxStatForCurrLevel) / (placedCache.getMaxValueInLevel(placedLevel + 1) - maxStatForCurrLevel);
        long statNow = placed - maxStatForCurrLevel;
        long statNeeds = placedCache.getMaxValueInLevel(placedLevel + 1) - maxStatForCurrLevel;

        drawTag(SSConfig.itemForPlacedBar, 10, 5, placedProgress, placedLevel, SSConfig.barProgressBackgroundColorPlaced, statNow, statNeeds);
    }

    private void showBrokenBar() {
        brokenLevel = brokenCache.getLevel(broken, brokenLevel);
        long brokenPreviousLevelStats = brokenCache.getMaxValueInLevel(brokenLevel);

        // TODO: remove before commmit->  вынести в отдельный метод
        float brokenProgress = (float) (broken - brokenPreviousLevelStats) / (brokenCache.getMaxValueInLevel(brokenLevel + 1) - brokenPreviousLevelStats);
        long statNow = broken - brokenPreviousLevelStats;
        long statNeeds = brokenCache.getMaxValueInLevel(brokenLevel + 1) - brokenPreviousLevelStats;

        drawTag(SSConfig.itemForBrokenBar, 10, 20, brokenProgress, brokenLevel, SSConfig.barProgressBackgroundColorBroken, statNow, statNeeds);
    }


    private void drawTag(ItemStack itemStack, int x, int y, float progress, int level, int progressBarColor, long statNow, long statNeeds) {
        if (itemStack != null && itemStack.getItem() != null) {
            renderItem.renderItemIntoGUI(fontRenderer, textureManager, itemStack, x, y);
            RenderHelper.disableStandardItemLighting();
        }
        String textLevel = Long.toString(level);

        // TODO: remove before commmit->  подумать о маг числах и нужны ли они
        float progressBar = (float) Math.max(0, progress - 0.05);
        int filledWidth = Math.min((int) (SSConfig.barWidth * progressBar), SSConfig.barWidth);
        int spaceForLevel = textLevel.length() * 6 + 5;


        // TODO: remove before commmit->  убрать тени у текста или написать свои
        drawProgressBar(x, y + 4, SSConfig.barWidth, progressBarColor, spaceForLevel, filledWidth);
        fontRenderer.drawStringWithShadow(textLevel, x + 25, y + 5, SSConfig.barLevelColor);

        fontRenderer.FONT_HEIGHT = 2;

        // TODO: remove before commmit->  for AlexWins mb abstract for formatter
        if (SSConfig.settingShowPercentToShowStatusInBar) {
            String textPercent = String.format("%.2f", Math.min((progressBar * 100), 100)) + "%";
            fontRenderer.drawStringWithShadow(textPercent, x + 40 + spaceForLevel, y + 5, SSConfig.barPercentColor);
        } else {
            String textCount = longNumberPrettier(statNow) + " / " + longNumberPrettier(statNeeds);
            fontRenderer.drawStringWithShadow(textCount, x + 40 + spaceForLevel, y + 5, SSConfig.barPercentColor);
        }
        fontRenderer.FONT_HEIGHT = 9;
        RenderHelper.disableStandardItemLighting();
    }


    private void drawProgressBar(int x, int y, int width, int progressBarColor, int spaceForLevel, int filledWidth) {
        Gui.drawRect(x - 1 + 20, y - 1, x + width + 25 + 1 + spaceForLevel, y + SSConfig.barHeight + 1, SSConfig.barBackgroundColor); // filling
        Gui.drawRect(x + 25 + spaceForLevel, y, x + 25 + width + spaceForLevel, y + SSConfig.barHeight, SSConfig.barProgressBackgroundColor); // background
        Gui.drawRect(x + 25 + spaceForLevel, y, x + 25 + spaceForLevel + filledWidth, y + SSConfig.barHeight, progressBarColor); // progress bar
    }


    //** from 3400000 do 3 400 000 */
    private static String longNumberPrettier(long number) {
        return String.format("%,d", number).replace(",", " ");
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
