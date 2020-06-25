package modfest.lacrimis.client.recipebook;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.screen.AbstractRecipeScreenHandler;

public class CustomRecipeBookWidget extends RecipeBookWidget {
    @Override
    public void initialize(int parentWidth, int parentHeight, MinecraftClient client, boolean isNarrow, AbstractRecipeScreenHandler<?> craftingScreenHandler) {
        super.initialize(parentWidth, parentHeight, client, isNarrow, craftingScreenHandler);
        recipeBook = new CustomRecipeBook(recipeBook);
        if (this.isOpen()) {
            this.reset(isNarrow);
        }
        if (recipesArea instanceof RecipeBookReceiver)
            ((RecipeBookReceiver) recipesArea).setRecipeBook(recipeBook);
    }


}
