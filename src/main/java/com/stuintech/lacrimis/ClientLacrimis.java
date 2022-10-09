package com.stuintech.lacrimis;

import com.stuintech.lacrimis.client.init.ClientModRenderers;
import com.stuintech.lacrimis.client.init.ClientModCrafting;
import com.stuintech.lacrimis.client.init.ClientModNetworking;
import com.stuintech.lacrimis.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

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
