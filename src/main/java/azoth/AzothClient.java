package azoth;

import azoth.client.render.CrucibleEntityRenderer;
import azoth.common.AzothBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class AzothClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(AzothBlocks.CRUCIBLE_ENTITY, CrucibleEntityRenderer::new);

        CrucibleEntityRenderer.onInit();
    }
}
