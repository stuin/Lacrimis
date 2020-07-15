package modfest.lacrimis.compat.rei;

import modfest.lacrimis.crafting.CrucibleRecipe;
import net.minecraft.util.Identifier;

public class CrucibleDisplay extends InfusionDisplay {

    public CrucibleDisplay(CrucibleRecipe recipe) {
        super(recipe);
    }

    public Identifier getRecipeCategory() {
        return LacrimisPlugin.CRUCIBLE;
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }
}
