package modfest.lacrimis.compat.rei.client;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import modfest.lacrimis.client.render.screen.InfusionScreen;
import modfest.lacrimis.compat.rei.CrucibleDisplay;
import modfest.lacrimis.compat.rei.InfusionDisplay;
import modfest.lacrimis.compat.rei.LacrimisPlugin;
import modfest.lacrimis.crafting.CrucibleRecipe;
import modfest.lacrimis.crafting.ShapedInfusionRecipe;
import modfest.lacrimis.crafting.ShapelessInfusionRecipe;
import modfest.lacrimis.item.ModItems;

public class ClientLacrimisPlugin implements REIClientPlugin {
    public ClientLacrimisPlugin() {
        //ClientInternals.attachInstance((Supplier<Object>) () -> this, Lacrimis.MODID + "Plugin");
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new InfusionCategory(), new CrucibleCategory());

        registry.addWorkstations(LacrimisPlugin.INFUSION, EntryStacks.of(ModItems.infusionTable));
        registry.addWorkstations(LacrimisPlugin.CRUCIBLE, EntryStacks.of(ModItems.crucible), EntryStacks.of(ModItems.infusionTable));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(ShapedInfusionRecipe.class, InfusionDisplay::new);
        registry.registerFiller(ShapelessInfusionRecipe.class, InfusionDisplay::new);
        registry.registerFiller(CrucibleRecipe.class, CrucibleDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(137, 29, 10, 13), InfusionScreen.class, LacrimisPlugin.INFUSION);
        registry.registerContainerClickArea(new Rectangle(137, 29, 10, 13), InfusionScreen.class, LacrimisPlugin.CRUCIBLE);
    }
}
