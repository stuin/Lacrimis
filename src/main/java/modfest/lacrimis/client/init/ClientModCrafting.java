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
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class ClientModCrafting {
    public static void registerClient() {
        if(MinecraftClient.getInstance().player != null) {
            // Infusion GUI
            ScreenProviderRegistry.INSTANCE.<InfusionScreenHandler>registerFactory(ModCrafting.INFUSION_SCREEN_ID,
                    container -> new InfusionScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText(Lacrimis.MODID + ".gui.infusion")));
            ScreenProviderRegistry.INSTANCE.<CombinerScreenHandler>registerFactory(ModCrafting.COMBINER_SCREEN_ID,
                    container -> new CombinerScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText(Lacrimis.MODID + ".gui.combiner")));
        }
        // Patchouli pages
        ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(Lacrimis.MODID, "crucible"), PageCrucible.class);
        ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(Lacrimis.MODID, "infusion"), PageInfusion.class);
    }
}
