package com.stuintech.lacrimis.client.render.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stuintech.lacrimis.Lacrimis;
import com.stuintech.lacrimis.crafting.CombinerScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CombinerScreen extends HandledScreen<CombinerScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(Lacrimis.MODID, "textures/gui/combiner.png");

    public CombinerScreen(CombinerScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        this.titleX = 60;
        this.titleY = 18;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableBlend();

        textRenderer.draw(matrices, handler.getEntity(), x + titleX, y + titleY + 15, 4210752);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (width - backgroundWidth) / 2;
        int j = (height - backgroundHeight) / 2;
        drawTexture(matrices, i, j, 0, 0, backgroundWidth, backgroundHeight);
        drawTexture(matrices, i + 59, j + 20, 0, backgroundHeight + (handler.getSlot(0).hasStack() ? 0 : 16), 110, 16);
        if ((handler.getSlot(0).hasStack() || handler.getSlot(1).hasStack()) && !handler.getSlot(2).hasStack()) {
            drawTexture(matrices, i + 99, j + 45, backgroundWidth, 0, 28, 21);
        }
    }
}