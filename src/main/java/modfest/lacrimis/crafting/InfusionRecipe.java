package modfest.lacrimis.crafting;

import modfest.lacrimis.init.ModCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public abstract class InfusionRecipe implements Recipe<InfusionInventory> {
    private final int tears;
    private final ItemStack result;
    private final Identifier id;

    public InfusionRecipe(Identifier id, int tears, ItemStack result) {
        this.id = id;
        this.tears = tears;
        this.result = result;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public ItemStack getOutput() {
        return result;
    }


    public RecipeType<?> getType() {
        return ModCrafting.INFUSION_RECIPE;
    }

    @Override
    public ItemStack craft(InfusionInventory craftingInventory) {
        return this.getOutput().copy();
    }

    public int getTears() {
        return tears;
    }
}
