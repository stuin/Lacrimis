package modfest.lacrimis.client.init;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.client.render.screen.CombinerScreen;
import modfest.lacrimis.client.render.screen.InfusionScreen;
import modfest.lacrimis.compat.patchiouli.PageCrucible;
import modfest.lacrimis.compat.patchiouli.PageInfusion;
import modfest.lacrimis.init.ModCrafting;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.util.Identifier;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class ClientModCrafting {
    public static void registerClient() {
        ScreenRegistry.register(ModCrafting.INFUSION_SCREEN_HANDLER, InfusionScreen::new);
        ScreenRegistry.register(ModCrafting.COMBINER_SCREEN_HANDLER, CombinerScreen::new);

        // Patchouli pages
        ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(Lacrimis.MODID, "crucible"), PageCrucible.class);
        ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(Lacrimis.MODID, "infusion"), PageInfusion.class);
    }
}
