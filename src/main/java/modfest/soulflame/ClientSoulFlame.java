package modfest.soulflame;

import modfest.soulflame.block.ModBlocks;
import modfest.soulflame.infusion.InfusionScreen;
import modfest.soulflame.infusion.InfusionScreenHandler;
import modfest.soulflame.infusion.ModInfusion;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.TranslatableText;

public class ClientSoulFlame implements ClientModInitializer {
	@Environment(EnvType.CLIENT)
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.infusionTable, RenderLayer.getCutout());

		ScreenProviderRegistry.INSTANCE.registerFactory(ModInfusion.INFUSION_SCREEN_ID, (ContainerScreenFactory<InfusionScreenHandler>) container -> new InfusionScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText("gui.soulflame.infusion")));
	}
}
