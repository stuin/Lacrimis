package modfest.lacrimis.mixin;

import net.minecraft.client.gui.screen.recipebook.RecipeBookResults;
import net.minecraft.recipe.book.RecipeBook;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import modfest.lacrimis.client.recipebook.RecipeBookReceiver;

@Mixin(RecipeBookResults.class)
public abstract class RecipeBookResultsMixin implements RecipeBookReceiver {

    @Shadow
    private RecipeBook recipeBook;

    public void setRecipeBook(RecipeBook recipeBook) {
        this.recipeBook = recipeBook;
    }

}
