package modfest.lacrimis.compat.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import modfest.lacrimis.crafting.*;
import modfest.lacrimis.init.ModItems;
import net.minecraft.util.Identifier;

public class LacrimisPlugin implements REIPluginV0 {
    public static final Identifier INFUSION = new Identifier("lacrimis", "infusion");
    public static final Identifier CRUCIBLE = new Identifier("lacrimis", "crucible");
    public static final Identifier PLUGIN = new Identifier("lacrimis", "plugin");

    @Override
    public Identifier getPluginIdentifier() {
        return PLUGIN;
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new InfusionCategory());
        recipeHelper.registerCategory(new CrucibleCategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        recipeHelper.registerRecipes(INFUSION, ShapedInfusionRecipe.class, ShapedInfusionDisplay::new);
        recipeHelper.registerRecipes(INFUSION, ShapelessInfusionRecipe.class, ShapelessInfusionDisplay::new);
        recipeHelper.registerRecipes(CRUCIBLE, CrucibleRecipe.class, CrucibleDisplay::new);
    }

    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerWorkingStations(INFUSION, EntryStack.create(ModItems.infusionTable));
        recipeHelper.registerWorkingStations(CRUCIBLE, EntryStack.create(ModItems.crucible), EntryStack.create(ModItems.infusionTable));
        //recipeHelper.registerScreenClickArea(new Rectangle(88, 32, 28, 23), InfusionScreen.class, INFUSION);
        //recipeHelper.registerScreenClickArea(new Rectangle(88, 32, 28, 23), InfusionScreen.class, CRUCIBLE);
    }
}
