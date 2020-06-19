package modfest.lacrimis.init;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;

import vazkii.patchouli.client.book.ClientBookRegistry;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.client.patchiouli.PageCrucible;
import modfest.lacrimis.client.patchiouli.PageInfusion;
import modfest.lacrimis.crafting.CombinerScreen;
import modfest.lacrimis.crafting.CombinerScreenHandler;
import modfest.lacrimis.crafting.InfusionScreen;
import modfest.lacrimis.crafting.InfusionScreenHandler;

public class ClientModCrafting {

    public static void register() {
        // Infusion GUI
        ScreenProviderRegistry.INSTANCE.registerFactory(ModCrafting.INFUSION_SCREEN_ID,
                (ContainerScreenFactory<InfusionScreenHandler>) container -> new InfusionScreen(container, MinecraftClient.getInstance().player.inventory,
                        new TranslatableText(Lacrimis.MODID + ".gui.infusion")));
        ScreenProviderRegistry.INSTANCE.registerFactory(ModCrafting.COMBINER_SCREEN_ID,
                (ContainerScreenFactory<CombinerScreenHandler>) container -> new CombinerScreen(container, MinecraftClient.getInstance().player.inventory,
                        new TranslatableText(Lacrimis.MODID + ".gui.combiner")));

        // Patchouli pages
        ClientBookRegistry.INSTANCE.pageTypes.put(Lacrimis.MODID + ":crucible", PageCrucible.class);
        ClientBookRegistry.INSTANCE.pageTypes.put(Lacrimis.MODID + ":infusion", PageInfusion.class);
    }

}
