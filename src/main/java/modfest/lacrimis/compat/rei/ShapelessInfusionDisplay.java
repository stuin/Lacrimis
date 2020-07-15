package modfest.lacrimis.compat.rei;

import modfest.lacrimis.crafting.ShapelessInfusionRecipe;

public class ShapelessInfusionDisplay extends InfusionDisplay {

    public ShapelessInfusionDisplay(ShapelessInfusionRecipe recipe) {
        super(recipe);
    }

    public int getWidth() {
        return this.display.getPreviewInputs().size() > 4 ? 3 : 2;
    }

    public int getHeight() {
        return this.display.getPreviewInputs().size() > 4 ? 3 : 2;
    }
}
