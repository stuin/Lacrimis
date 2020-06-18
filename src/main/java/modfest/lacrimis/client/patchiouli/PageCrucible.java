package modfest.lacrimis.client.patchiouli;

import com.mojang.blaze3d.systems.RenderSystem;
import modfest.lacrimis.crafting.CrucibleRecipe;
import modfest.lacrimis.init.ModCrafting;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public class PageCrucible extends PageDoubleRecipeRegistry<CrucibleRecipe> {

    public PageCrucible() {
        super(ModCrafting.CRUCIBLE_RECIPE);
    }

    protected void drawRecipe(MatrixStack ms, CrucibleRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        this.mc.getTextureManager().bindTexture(ModCrafting.craftingTexture);
        RenderSystem.enableBlend();
        DrawableHelper.drawTexture(ms, recipeX, recipeY, 11.0F, 71.0F, 96, 24, 128, 128);
        this.parent.drawCenteredStringNoShadow(ms, this.getTitle(second), 58, recipeY - 10, this.book.headerColor);
        this.parent.renderIngredient(ms, recipeX + 4, recipeY + 4, mouseX, mouseY, recipe.getBase());
        this.parent.renderItemStack(ms, recipeX + 76, recipeY + 4, mouseX, mouseY, recipe.getOutput());
        ms.push();
        ms.scale(0.5F, 0.5F, 1.0F);
        this.parent.drawCenteredStringNoShadow(ms, "" + recipe.getTears(), (recipeX + 48) * 2, (recipeY + 19) * 2, this.book.headerColor);
        ms.pop();
    }

    protected ItemStack getRecipeOutput(CrucibleRecipe recipe) {
        return recipe == null ? ItemStack.EMPTY : recipe.getOutput();
    }

    protected int getRecipeHeight() {
        return 45;
    }
}
