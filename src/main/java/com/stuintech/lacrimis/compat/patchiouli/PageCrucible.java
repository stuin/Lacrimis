package com.stuintech.lacrimis.compat.patchiouli;

import com.stuintech.lacrimis.crafting.CrucibleRecipe;
import com.stuintech.lacrimis.crafting.ModCrafting;
import net.minecraft.client.util.math.MatrixStack;
import vazkii.patchouli.client.book.page.abstr.PageSimpleProcessingRecipe;

public class PageCrucible extends PageSimpleProcessingRecipe<CrucibleRecipe> {

    public PageCrucible() {
        super(ModCrafting.CRUCIBLE_RECIPE);
    }

    @Override
    protected void drawRecipe(MatrixStack ms, CrucibleRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        super.drawRecipe(ms, recipe, recipeX, recipeY, mouseX, mouseY, second);

        ms.push();
        ms.scale(0.5F, 0.5F, 1.0F);
        this.parent.drawCenteredStringNoShadow(ms, Integer.toString(recipe.getTears()), (recipeX + 65) * 2, (recipeY + 19) * 2, this.book.headerColor);
        ms.pop();
    }
}
