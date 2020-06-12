package modfest.soulflame;

import modfest.soulflame.block.entity.renderer.CrucibleEntityRenderer;
import modfest.soulflame.init.ModBlockEntityTypes;
import modfest.soulflame.init.ModBlocks;
import modfest.soulflame.init.ModInfusion;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class ClientSoulFlame implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.infusionTable, RenderLayer.getCutout());

		BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityTypes.crucible, CrucibleEntityRenderer::new);
		CrucibleEntityRenderer.onInit();

		ModInfusion.registerClient();
	}
}
