package modfest.lacrimis.client.recipebook;

import modfest.lacrimis.crafting.InfusionRecipe;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;

import java.util.*;

public class CustomRecipeBook extends ClientRecipeBook {
    private final ClientRecipeBook source;
    private final List<RecipeResultCollection> results = new ArrayList<>();

    public CustomRecipeBook(ClientRecipeBook source) {
        super(source.manager);
        this.source = source;
    }

    @Override
    public void reload() {
        this.results.clear();
        results.add(new RecipeResultCollection());

        for(Recipe<?> recipe : manager.values()) {
            if(!recipe.isIgnoredInRecipeBook())
                if(recipe instanceof InfusionRecipe)
                    results.get(0).addRecipe(recipe);
        }
        results.get(0).initialize(source);
    }

    @Override
    public List<RecipeResultCollection> getOrderedResults() {
        return results;
    }

    @Override
    public List<RecipeResultCollection> getResultsForGroup(RecipeBookGroup category) {
        return results;
    }
}
