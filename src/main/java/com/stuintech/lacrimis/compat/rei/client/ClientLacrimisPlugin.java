package com.stuintech.lacrimis.compat.rei.client;

import com.stuintech.lacrimis.crafting.CrucibleRecipe;
import com.stuintech.lacrimis.crafting.ShapedInfusionRecipe;
import com.stuintech.lacrimis.crafting.ShapelessInfusionRecipe;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import com.stuintech.lacrimis.client.render.screen.InfusionScreen;
import com.stuintech.lacrimis.compat.rei.CrucibleDisplay;
import com.stuintech.lacrimis.compat.rei.InfusionDisplay;
import com.stuintech.lacrimis.compat.rei.LacrimisPlugin;
import com.stuintech.lacrimis.item.ModItems;

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
