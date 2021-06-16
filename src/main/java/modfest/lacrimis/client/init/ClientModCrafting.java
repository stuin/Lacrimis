package modfest.lacrimis.client.init;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.compat.patchiouli.PageCrucible;
import modfest.lacrimis.compat.patchiouli.PageInfusion;
import modfest.lacrimis.crafting.CombinerScreen;
import modfest.lacrimis.crafting.CombinerScreenHandler;
import modfest.lacrimis.crafting.InfusionScreen;
import modfest.lacrimis.crafting.InfusionScreenHandler;
import modfest.lacrimis.init.ModCrafting;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class ClientModCrafting {
    public static ScreenHandlerType<InfusionScreenHandler> INFUSION_SCREEN_HANDLER;
    public static ScreenHandlerType<CombinerScreenHandler> COMBINER_SCREEN_HANDLER;

    public static void registerClient() {
        // GUI Screens
        //ScreenRegistry.register(INFUSION_SCREEN_HANDLER, InfusionScreen::new);
        //ScreenRegistry.register(COMBINER_SCREEN_HANDLER, CombinerScreen::new);

        // Patchouli pages
        ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(Lacrimis.MODID, "crucible"), PageCrucible.class);
        ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(Lacrimis.MODID, "infusion"), PageInfusion.class);
    }
}
