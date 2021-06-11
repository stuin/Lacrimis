package modfest.lacrimis.compat.patchiouli;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import com.mojang.blaze3d.systems.RenderSystem;

import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

import modfest.lacrimis.crafting.InfusionRecipe;
import modfest.lacrimis.crafting.ShapedInfusionRecipe;
import modfest.lacrimis.init.ModCrafting;

public class PageInfusion extends PageDoubleRecipeRegistry<InfusionRecipe> {
    public PageInfusion() {
        super(ModCrafting.INFUSION_RECIPE);
    }

    @Override
    protected void drawRecipe(MatrixStack ms, InfusionRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        this.mc.getTextureManager().bindTexture(ModCrafting.craftingTexture);
        RenderSystem.enableBlend();
        DrawableHelper.drawTexture(ms, recipeX - 2, recipeY - 2, 0.0F, 0.0F, 100, 62, 128, 128);
        boolean shaped = recipe instanceof ShapedInfusionRecipe;
        int wrap;
        if (!shaped) {
            int iconX = recipeX + 62;
            wrap = recipeY + 2;
            DrawableHelper.drawTexture(ms, iconX, wrap, 0.0F, 64.0F, 11, 11, 128, 128);
            if (this.parent.isMouseInRelativeRange(mouseX, mouseY, iconX, wrap, 11, 11)) {
                this.parent.setTooltip(new TranslatableText("patchouli.gui.lexicon.shapeless"));
            }
        }

        this.parent.drawCenteredStringNoShadow(ms, this.getTitle(second).asOrderedText(), 58, recipeY - 10, this.book.headerColor);
        this.parent.renderItemStack(ms, recipeX + 79, recipeY + 22, mouseX, mouseY, recipe.getOutput());
        DefaultedList<Ingredient> ingredients = recipe.getIngredients();
        wrap = 3;
        if (shaped) {
            wrap = ((ShapedInfusionRecipe)recipe).getWidth();
        }

        for(int i = 0; i < ingredients.size(); ++i) {
            this.parent.renderIngredient(ms, recipeX + i % wrap * 19 + 3, recipeY + i / wrap * 19 + 3, mouseX, mouseY, ingredients.get(i));
        }

        this.parent.renderItemStack(ms, recipeX + 79, recipeY + 41, mouseX, mouseY, recipe.createIcon());

        ms.push();
        ms.scale(0.5F, 0.5F, 1.0F);
        this.parent.drawCenteredStringNoShadow(ms, Integer.toString(recipe.getTears()), (recipeX + 68) * 2, (recipeY + 35) * 2, this.book.headerColor);
        ms.pop();
    }

    @Override
    protected int getRecipeHeight() {
        return 78;
    }

    @Override
    protected ItemStack getRecipeOutput(InfusionRecipe recipe) {
        return recipe == null ? ItemStack.EMPTY : recipe.getOutput();
    }
}
