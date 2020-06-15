package modfest.soulflame;

import modfest.soulflame.client.render.blocks.CrucibleEntityRenderer;
import modfest.soulflame.client.render.blocks.InfusionTableEntityRenderer;
import modfest.soulflame.client.render.entity.GhostEntityRenderer;
import modfest.soulflame.init.ModBlockEntityTypes;
import modfest.soulflame.init.ModBlocks;
import modfest.soulflame.init.ModEntityTypes;
import modfest.soulflame.init.ModInfusion;
import modfest.soulflame.init.ModNetworking;
import modfest.soulflame.init.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class ClientSoulFlame implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.infusionTable, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.tearLantern, RenderLayer.getCutout());

        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityTypes.crucible, CrucibleEntityRenderer::new);
        CrucibleEntityRenderer.onInit();

        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityTypes.infusionTable, InfusionTableEntityRenderer::new);
        InfusionTableEntityRenderer.onInit();

        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.ghost, (dispatcher, ctx) -> new GhostEntityRenderer(dispatcher));

        ModInfusion.registerClient();
        ModNetworking.registerClient();
        ModParticles.registerClient();
    }
}
