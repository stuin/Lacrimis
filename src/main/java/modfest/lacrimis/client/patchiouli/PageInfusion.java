package modfest.lacrimis.client.patchiouli;

import com.mojang.blaze3d.systems.RenderSystem;
import modfest.lacrimis.infusion.ShapedInfusionRecipe;
import modfest.lacrimis.init.ModInfusion;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public class PageInfusion extends PageDoubleRecipeRegistry<ShapedInfusionRecipe> {
    public PageInfusion() {
        super(ModInfusion.INFUSION_RECIPE);
    }

    protected void drawRecipe(MatrixStack ms, ShapedInfusionRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        this.mc.getTextureManager().bindTexture(ModInfusion.craftingTexture);
        RenderSystem.enableBlend();
        DrawableHelper.drawTexture(ms, recipeX - 2, recipeY - 2, 0.0F, 0.0F, 100, 62, 128, 128);
        boolean shaped = recipe instanceof ShapedInfusionRecipe;
        int wrap;
        if (!shaped) {
            int iconX = recipeX + 62;
            wrap = recipeY + 2;
            DrawableHelper.drawTexture(ms, iconX, wrap, 0.0F, 64.0F, 11, 11, 128, 128);
            if (this.parent.isMouseInRelativeRange((double)mouseX, (double)mouseY, iconX, wrap, 11, 11)) {
                this.parent.setTooltip(new TranslatableText("patchouli.gui.lexicon.shapeless"));
            }
        }

        this.parent.drawCenteredStringNoShadow(ms, this.getTitle(second), 58, recipeY - 10, this.book.headerColor);
        this.parent.renderItemStack(ms, recipeX + 79, recipeY + 22, mouseX, mouseY, recipe.getOutput());
        DefaultedList<Ingredient> ingredients = recipe.getPreviewInputs();
        wrap = 3;
        if (shaped) {
            wrap = recipe.getWidth();
        }

        for(int i = 0; i < ingredients.size(); ++i) {
            this.parent.renderIngredient(ms, recipeX + i % wrap * 19 + 3, recipeY + i / wrap * 19 + 3, mouseX, mouseY, (Ingredient)ingredients.get(i));
        }

    }

    protected int getRecipeHeight() {
        return 78;
    }

    protected ItemStack getRecipeOutput(ShapedInfusionRecipe recipe) {
        return recipe == null ? ItemStack.EMPTY : recipe.getOutput();
    }
}
