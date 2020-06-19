package modfest.lacrimis.client.recipebook;

import modfest.lacrimis.crafting.InfusionRecipe;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import java.util.*;

public class CustomRecipeBook extends ClientRecipeBook {
    private final ClientRecipeBook source;
    private final List<RecipeResultCollection> results = new ArrayList<>();
    private final List<RecipeResultCollection> searchResults = new ArrayList<>();

    public CustomRecipeBook(ClientRecipeBook source) {
        super(source.manager);
        this.source = source;
    }

    @Override
    public void reload() {
        this.results.clear();

        for(Recipe<?> recipe : manager.values()) {
            if(!recipe.isIgnoredInRecipeBook())
                if(recipe instanceof InfusionRecipe) {
                    RecipeResultCollection collection = new RecipeResultCollection();
                    collection.addRecipe(recipe);
                    collection.initialize(source);
                    results.add(collection);
                    searchResults.add(collection);
                }
        }
    }

    @Override
    public List<RecipeResultCollection> getOrderedResults() {
        return results;
    }

    @Override
    public List<RecipeResultCollection> getResultsForGroup(RecipeBookGroup category) {
        this.reload();
        if(category == RecipeBookGroup.SEARCH)
            return searchResults;
        return results;
    }

    @Override
    public boolean contains(Identifier id) {
        return true;
    }

    @Override
    public boolean contains(Recipe<?> recipe) {
        return recipe instanceof InfusionRecipe;
    }
}
