package modfest.lacrimis.init;

import modfest.lacrimis.compat.patchiouli.PageCrucible;
import modfest.lacrimis.compat.patchiouli.PageInfusion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;

import net.minecraft.util.Identifier;
import vazkii.patchouli.client.book.ClientBookRegistry;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.crafting.CombinerScreen;
import modfest.lacrimis.crafting.CombinerScreenHandler;
import modfest.lacrimis.crafting.InfusionScreen;
import modfest.lacrimis.crafting.InfusionScreenHandler;

public class ClientModCrafting {

    public static void register() {
        // Infusion GUI
        ScreenProviderRegistry.INSTANCE.<InfusionScreenHandler>registerFactory(ModCrafting.INFUSION_SCREEN_ID,
                container -> new InfusionScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText(Lacrimis.MODID + ".gui.infusion")));
        ScreenProviderRegistry.INSTANCE.<CombinerScreenHandler>registerFactory(ModCrafting.COMBINER_SCREEN_ID,
                container -> new CombinerScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText(Lacrimis.MODID + ".gui.combiner")));

        // Patchouli pages
        ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(Lacrimis.MODID, "crucible"), PageCrucible.class);
        ClientBookRegistry.INSTANCE.pageTypes.put(new Identifier(Lacrimis.MODID, "infusion"), PageInfusion.class);
    }

}
