package com.simple.stats.client.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.item.Item;
import net.minecraft.block.Block;

@SideOnly(Side.CLIENT)
public class StatsHudGui extends Gui {


    private static Long broken = 0L;
    private static Long placed = 0L;

    private Minecraft mc;
    private static final RenderItem renderItem = new RenderItem();
    private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    private static final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

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

        drawTag(Blocks.stone, 10, 5, getPlaced(), green);  //placed
        drawTag(Items.diamond_pickaxe, 10, 20, getBroken(), red); //broken

    }

    private void drawProgressBar(int x, int y, int width, int height, float progress, int barColor) {

        // Рассчитайте ширину заполнения прогресс-бара на основе значения progress
        int filledWidth = Math.min((int) (width * progress), barLength);

        // Отрисуйте фон прогресс-бара
        Gui.drawRect(x, y, x + width, y + height, fonColor);

        // Отрисуйте заполненную часть прогресс-бара
        Gui.drawRect(x, y, x + filledWidth, y + height, barColor);
    }

    private void drawTag(Block block, int x, int y, long amount, int barColor) {
        if (block != null)
            drawTag(new ItemStack(block), x, y, amount, barColor);
        else
            drawTag((ItemStack) null, x, y, amount, barColor);
    }

    private void drawTag(Item item, int x, int y, long amount, int barColor) {
        if (item != null)
            drawTag(new ItemStack(item), x, y, amount, barColor);
        else
            drawTag((ItemStack) null, x, y, amount, barColor);
    }

    private void drawTag(ItemStack itemStack, int x, int y, long amount, int barColor) {
        if (itemStack != null && itemStack.getItem() != null) {
            renderItem.renderItemAndEffectIntoGUI(fontRenderer, textureManager, itemStack, x, y);
        }

        drawProgressBar(x+20, y+3, barLength, 10, amount / 100f, barColor);
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


}
