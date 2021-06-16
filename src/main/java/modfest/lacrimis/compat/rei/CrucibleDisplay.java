package modfest.lacrimis.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import modfest.lacrimis.crafting.CrucibleRecipe;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CrucibleDisplay  extends BasicDisplay implements SimpleGridMenuDisplay {
    private final Recipe<?> recipe;
    private final int tears;

    public CrucibleDisplay(CrucibleRecipe recipe) {
        this(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())),
                recipe, recipe.getTears());
    }

    public CrucibleDisplay(List<EntryIngredient> input, List<EntryIngredient> output, NbtCompound tag) {
        this(input, output, RecipeManagerContext.getInstance().byId(tag, "location"),
                tag.getInt("tears"));
    }

    public CrucibleDisplay(List<EntryIngredient> input, List<EntryIngredient> output, Recipe<?> recipe, int tears) {
        super(input, output, Optional.ofNullable(recipe).map(Recipe::getId));
        this.recipe = recipe;
        this.tears = tears;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return LacrimisPlugin.CRUCIBLE;
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.of(recipe.getId());
    }

    public int getTears() {
        return tears;
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    public static <R extends CrucibleDisplay> BasicDisplay.Serializer<R> serializer(BasicDisplay.Serializer.RecipeLessConstructor<R> constructor) {
        return BasicDisplay.Serializer.ofRecipeLess(constructor, (display, tag) -> {
            tag.putInt("tears", display.getTears());
        });
    }
}
