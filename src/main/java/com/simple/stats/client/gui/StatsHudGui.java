package com.simple.stats.client.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class StatsHudGui extends Gui {
    // todo only setter
    public static Long broken = 0L;
    public static Long placed = 0L;
    // todo only setter
    private Minecraft mc;

    public static void registerHandler() {
        MinecraftForge.EVENT_BUS.register(new StatsHudGui(Minecraft.getMinecraft()));
    }

    public StatsHudGui(Minecraft mc) {
        super();
        this.mc = mc;
    }

    @SubscribeEvent
    public void onRenderStatsHud(RenderGameOverlayEvent.Post event) {
        if( event.type != RenderGameOverlayEvent.ElementType.TEXT)
            return;
        GL11.glDisable(GL11.GL_LIGHTING);
        this.mc.fontRenderer.drawString("broken", 5, 5, 0xFFFFFF00);
        drawProgressBar(50, 5, 50, 10, broken / 100f);

        this.mc.fontRenderer.drawString("placed", 5, 12, 0xFFFFFF00);
        drawProgressBar(50, 12, 50, 10, placed / 100f);

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void drawProgressBar(int x, int y, int width, int height, float progress) {
        // Определите стили и цвета для прогресс-бара
        int barColor = 0xFFFFF000; // Красный цвет
        int borderColor = 0xFF000000; // Черный цвет

        // Рассчитайте ширину заполнения прогресс-бара на основе значения progress
        int filledWidth = (int) (width * progress);

        // Отрисуйте рамку прогресс-бара
        Gui.drawRect(x, y, x + width, y + height, borderColor);

        // Отрисуйте заполненную часть прогресс-бара
        Gui.drawRect(x, y, x + filledWidth, y + height, barColor);
    }
}
