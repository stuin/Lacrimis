package modfest.lacrimis.mixin;

import modfest.lacrimis.client.recipebook.RecipeBookReciever;
import net.minecraft.client.gui.screen.recipebook.RecipeBookResults;
import net.minecraft.recipe.book.RecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RecipeBookResults.class)
public abstract class RecipeBookResultsMixin implements RecipeBookReciever {

    @Shadow
    private RecipeBook recipeBook;
    
    public void setRecipeBook(RecipeBook recipeBook) {
        this.recipeBook = recipeBook;
    }

}
