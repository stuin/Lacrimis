package modfest.lacrimis.compat.rei;

import modfest.lacrimis.crafting.ShapedInfusionRecipe;

public class ShapedInfusionDisplay extends InfusionDisplay {
    public ShapedInfusionDisplay(ShapedInfusionRecipe recipe) {
        super(recipe);
    }

    public int getHeight() {
        return ((ShapedInfusionRecipe)this.display).getHeight();
    }

    public int getWidth() {
        return ((ShapedInfusionRecipe)this.display).getWidth();
    }
}