package modfest.lacrimis.crafting;

import com.mojang.blaze3d.systems.RenderSystem;
import modfest.lacrimis.Lacrimis;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class CombinerScreen extends HandledScreen<CombinerScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("textures/gui/container/smithing.png");

    public CombinerScreen(CombinerScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        this.titleX = 60;
        this.titleY = 18;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableBlend();

        Text text = handler.getEntity();
        textRenderer.draw(matrices, text, x + titleX, y + titleY + 15, 4210752);

        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(TEXTURE);
        int i = (width - backgroundWidth) / 2;
        int j = (height - backgroundHeight) / 2;
        drawTexture(matrices, i, j, 0, 0, backgroundWidth, backgroundHeight);
        drawTexture(matrices, i + 59, j + 20, 0, backgroundHeight + (handler.getSlot(0).hasStack() ? 0 : 16), 110, 16);
        if ((handler.getSlot(0).hasStack() || handler.getSlot(1).hasStack()) && !handler.getSlot(2).hasStack()) {
            drawTexture(matrices, i + 99, j + 45, backgroundWidth, 0, 28, 21);
        }
    }
}