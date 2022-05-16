package com.stuintech.lacrimis.client.init;

import com.stuintech.lacrimis.Lacrimis;
import com.stuintech.lacrimis.client.render.screen.CombinerScreen;
import com.stuintech.lacrimis.client.render.screen.InfusionScreen;
import com.stuintech.lacrimis.compat.patchiouli.PageCrucible;
import com.stuintech.lacrimis.compat.patchiouli.PageInfusion;
import com.stuintech.lacrimis.crafting.ModCrafting;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.util.Identifier;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class ClientModCrafting {
    public static void registerClient() {
        // GUI Screens
        ScreenRegistry.register(ModCrafting.INFUSION_SCREEN_HANDLER, InfusionScreen::new);
        ScreenRegistry.register(ModCrafting.COMBINER_SCREEN_HANDLER, CombinerScreen::new);

        // Patchouli pages
        ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(Lacrimis.MODID, "crucible"), PageCrucible.class);
        ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(Lacrimis.MODID, "infusion"), PageInfusion.class);
    }
}
