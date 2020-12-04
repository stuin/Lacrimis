package azoth;

import azoth.client.render.CauldronEntityRenderer;
import azoth.client.render.InfusionTableEntityRenderer;
import azoth.common.AzothBlocks;
import azoth.common.AzothParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class AzothClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(AzothBlocks.CRUCIBLE_ENTITY, CauldronEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(AzothBlocks.INFUSION_TABLE_ENTITY, InfusionTableEntityRenderer::new);

        ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) -> {
            out.accept(CauldronEntityRenderer.CRUCIBLE_AZOTH_MODEL_ID);
            out.accept(InfusionTableEntityRenderer.INFUSION_TABLE_OVERLAY_MODEL_ID);
        });

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), AzothBlocks.AZOTH_CAULDRON, AzothBlocks.AZOTH_CONDUIT);

        AzothParticles.registerClient();
    }
}
