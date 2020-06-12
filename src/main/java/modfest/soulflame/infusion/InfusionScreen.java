package modfest.soulflame.infusion;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

//Mostly copied from CraftingScreen
@Environment(EnvType.CLIENT)
public class InfusionScreen extends HandledScreen<InfusionScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("textures/gui/container/crafting_table.png");
    private boolean isNarrow;

    public InfusionScreen(InfusionScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void init() {
        super.init();
        this.isNarrow = this.width < 379;
        this.titleX = 24;
    }

    public void tick() {
        super.tick();
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
        if(handler.tank != null)
            this.textRenderer.draw(matrices,"" + handler.tank.usage(), x + 10, titleY + 20 + y, 4210752);
    }

    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(TEXTURE);
        int i = this.x;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    protected boolean isPointWithinBounds(int xPosition, int yPosition, int width, int height, double pointX, double pointY) {
        return !this.isNarrow && super.isPointWithinBounds(xPosition, yPosition, width, height, pointX, pointY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.isNarrow || super.mouseClicked(mouseX, mouseY, button);
    }

    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        return mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth) || mouseY >= (double)(top + this.backgroundHeight);
    }

    protected void onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType) {
        super.onMouseClick(slot, invSlot, clickData, actionType);
    }

    public void removed() {
        super.removed();
    }
}
