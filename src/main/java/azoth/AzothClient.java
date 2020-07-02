package azoth;

import azoth.client.render.CrucibleEntityRenderer;
import azoth.client.render.InfusionTableEntityRenderer;
import azoth.common.AzothBlocks;
import azoth.common.AzothParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class AzothClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(AzothBlocks.CRUCIBLE_ENTITY, CrucibleEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(AzothBlocks.INFUSION_TABLE_ENTITY, InfusionTableEntityRenderer::new);

        ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) -> {
            out.accept(CrucibleEntityRenderer.CRUCIBLE_AZOTH_MODEL_ID);
            out.accept(InfusionTableEntityRenderer.INFUSION_TABLE_OVERLAY_MODEL_ID);
        });

        AzothParticles.registerClient();
    }
}
