package modfest.lacrimis.compat.rei;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeDisplay;
import me.shedaniel.rei.server.ContainerInfo;
import modfest.lacrimis.crafting.InfusionRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class InfusionDisplay implements TransferRecipeDisplay {
    protected InfusionRecipe display;
    protected List<List<EntryStack>> input;
    protected List<EntryStack> output;

    public InfusionDisplay(InfusionRecipe recipe) {
        this.display = recipe;
        this.input = recipe.getPreviewInputs().stream().map((i) -> {
            List<EntryStack> entries = new ArrayList();
            ItemStack[] var2 = i.getMatchingStacksClient();

            for(ItemStack stack : var2)
                entries.add(EntryStack.create(stack));

            return entries;
        }).collect(Collectors.toList());
        this.output = Collections.singletonList(EntryStack.create(recipe.getOutput()));
    }

    public Optional<Identifier> getRecipeLocation() {
        return Optional.ofNullable(this.display).map(InfusionRecipe::getId);
    }
    
    public Identifier getRecipeCategory() {
        return LacrimisPlugin.INFUSION;
    }

    public List<List<EntryStack>> getInputEntries() {
        return this.input;
    }

    public List<EntryStack> getOutputEntries() {
        return this.output;
    }

    public List<List<EntryStack>> getRequiredEntries() {
        return this.input;
    }
    
    public int getTears() {
        return display.getTears();
    }

    public List<List<EntryStack>> getOrganisedInputEntries(ContainerInfo<ScreenHandler> containerInfo, ScreenHandler container) {
        List<List<EntryStack>> list = Lists.newArrayListWithCapacity(containerInfo.getCraftingWidth(container) * containerInfo.getCraftingHeight(container));

        int i;
        for(i = 0; i < containerInfo.getCraftingWidth(container) * containerInfo.getCraftingHeight(container); ++i) {
            list.add(Collections.emptyList());
        }

        for(i = 0; i < this.getInputEntries().size(); ++i) {
            List<EntryStack> stacks = this.getInputEntries().get(i);
            list.set(InfusionCategory.getSlotWithSize(this, i, containerInfo.getCraftingWidth(container)), stacks);
        }

        return list;
    }
}