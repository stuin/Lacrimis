package modfest.soulflame;

import modfest.soulflame.infusion.InfusionScreen;
import modfest.soulflame.infusion.InfusionScreenHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.TranslatableText;

public class ClientSoulFlame implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.infusionTable, RenderLayer.getCutout());

		ScreenProviderRegistry.INSTANCE.registerFactory(SoulFlame.INFUSION_SCREEN_ID, new ContainerScreenFactory<InfusionScreenHandler>() {
			@Override
			public HandledScreen create(InfusionScreenHandler container) {
				return new InfusionScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText("gui.soulflame.infusion"));
			}
		});
	}
}
