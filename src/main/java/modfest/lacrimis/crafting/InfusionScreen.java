package modfest.lacrimis.crafting;

import com.mojang.blaze3d.systems.RenderSystem;

import modfest.lacrimis.Lacrimis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
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
    private static final Identifier START_BUTTON_TEXTURE = new Identifier(Lacrimis.MODID, "textures/gui/start_button.png");
    private TexturedButtonWidget startButton;
    private boolean isNarrow;

    public InfusionScreen(InfusionScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        isNarrow = width < 379;
        startButton = new TexturedButtonWidget(x + backgroundWidth - 54, y + titleY + 3, 20, 18, 0, 0, 19, START_BUTTON_TEXTURE, (buttonWidget) -> {
            handler.startCrafting();
        });
        addButton(startButton);
        titleX = 24;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

        drawMouseoverTooltip(matrices, mouseX, mouseY);
        String s = handler.getCurrentTears() + "/" + handler.getRequiredTears();
        textRenderer.draw(matrices, s, x + backgroundWidth - 52, titleY + 52 + y, 4210752);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(TEXTURE);
        int i = (width - backgroundWidth) / 2;
        int j = (height - backgroundHeight) / 2;
        drawTexture(matrices, i, j, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected boolean isPointWithinBounds(int xPosition, int yPosition, int width, int height, double pointX, double pointY) {
        return (!isNarrow) && super.isPointWithinBounds(xPosition, yPosition, width, height, pointX, pointY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return isNarrow || super.mouseClicked(mouseX, mouseY, button);
    }
}
