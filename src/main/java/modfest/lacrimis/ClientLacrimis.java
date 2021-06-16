package modfest.lacrimis;

import modfest.lacrimis.client.init.ClientModCrafting;
import modfest.lacrimis.client.init.ClientModNetworking;
import modfest.lacrimis.client.init.ClientModRenderers;
import modfest.lacrimis.init.*;
import net.minecraft.client.render.RenderLayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;

@Environment(EnvType.CLIENT)
public class ClientLacrimis implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.infusionTable, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.tearLantern, RenderLayer.getCutout());

        ClientModRenderers.registerClient();
        ClientModCrafting.registerClient();
        ClientModNetworking.registerClient();
    }
}
