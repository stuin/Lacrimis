package modfest.lacrimis;

import modfest.lacrimis.client.render.entity.GhostEntityRenderer;
import modfest.lacrimis.init.*;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

import modfest.lacrimis.client.render.block.CrucibleEntityRenderer;
import modfest.lacrimis.client.render.block.InfusionTableEntityRenderer;

@Environment(EnvType.CLIENT)
public class ClientLacrimis implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.infusionTable, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.tearLantern, RenderLayer.getCutout());

        BlockEntityRendererRegistry.INSTANCE.register(ModEntityTypes.crucible, CrucibleEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModEntityTypes.infusionTable, InfusionTableEntityRenderer::new);
        CrucibleEntityRenderer.onInit();
        InfusionTableEntityRenderer.onInit();

        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.ghost, (dispatcher, ctx) -> new GhostEntityRenderer(dispatcher));

        ModCrafting.registerClient();
        ModNetworking.registerClient();
        ModParticles.registerClient();
    }
}
