package modfest.lacrimis.infusion;

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
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

//Mostly copied from CraftingScreen
@Environment(EnvType.CLIENT)
public class InfusionScreen extends HandledScreen<InfusionScreenHandler> implements RecipeBookProvider {
    private static final Identifier TEXTURE = new Identifier("textures/gui/container/crafting_table.png");
    private static final Identifier RECIPE_BUTTON_TEXTURE = new Identifier(Lacrimis.MODID, "textures/item/guide.png");
    private final RecipeBookWidget recipeBook = new RecipeBookWidget();
    private boolean isNarrow;

    public InfusionScreen(InfusionScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void init() {
        super.init();
        isNarrow = width < 379;
        recipeBook.initialize(width, height, client, isNarrow, handler);
        x = recipeBook.findLeftEdge(isNarrow, width, backgroundWidth);
        children.add(recipeBook);
        setInitialFocus(recipeBook);
        addButton(new TexturedButtonWidget(x + 5, height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (buttonWidget) -> {
            recipeBook.reset(isNarrow);
            recipeBook.toggleOpen();
            x = recipeBook.findLeftEdge(isNarrow, width, backgroundWidth);
            ((TexturedButtonWidget)buttonWidget).setPos(x + 5, height / 2 - 49);
        }));
        titleX = 24;
    }

    public void tick() {
        super.tick();
        recipeBook.update();
    }
  
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        if (recipeBook.isOpen() && isNarrow) {
            drawBackground(matrices, delta, mouseX, mouseY);
            recipeBook.render(matrices, mouseX, mouseY, delta);
        } else {
            recipeBook.render(matrices, mouseX, mouseY, delta);
            super.render(matrices, mouseX, mouseY, delta);
            recipeBook.drawGhostSlots(matrices, x, y, true, delta);
        }

        drawMouseoverTooltip(matrices, mouseX, mouseY);
        String s = handler.getCurrentTears() + "/" + handler.getRequiredTears();
        textRenderer.draw(matrices, s, x + backgroundWidth - 52, titleY + 52 + y, 4210752);
    }

    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(TEXTURE);
        int i = x;
        int j = (height - backgroundHeight) / 2;
        drawTexture(matrices, i, j, 0, 0, backgroundWidth, backgroundHeight);
    }

    protected boolean isPointWithinBounds(int xPosition, int yPosition, int width, int height, double pointX, double pointY) {
        return (!isNarrow || !recipeBook.isOpen()) &&
                super.isPointWithinBounds(xPosition, yPosition, width, height, pointX, pointY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (recipeBook.mouseClicked(mouseX, mouseY, button)) {
            setFocused(recipeBook);
            return true;
        } else {
            return isNarrow && recipeBook.isOpen() || super.mouseClicked(mouseX, mouseY, button);
        }
    }

    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        boolean bl = mouseX < (double)left || mouseY < (double)top ||
                mouseX >= (double)(left + backgroundWidth) || mouseY >= (double)(top + backgroundHeight);
        return recipeBook.isClickOutsideBounds(mouseX, mouseY, x, y, backgroundWidth, backgroundHeight, button) && bl;
    }

    protected void onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType) {
        super.onMouseClick(slot, invSlot, clickData, actionType);
        recipeBook.slotClicked(slot);
    }

    public void refreshRecipeBook() {
        recipeBook.refresh();
    }

    public void removed() {
        recipeBook.close();
        super.removed();
    }

    public RecipeBookWidget getRecipeBookWidget() {
        return recipeBook;
    }
}
