package com.stuintech.lacrimis.client.init;

import com.stuintech.lacrimis.client.render.block.CrucibleEntityRenderer;
import com.stuintech.lacrimis.client.render.block.InfusionTableEntityRenderer;
import com.stuintech.lacrimis.client.render.block.NetworkLinkEntityRenderer;
import com.stuintech.lacrimis.client.render.entity.SoulShellRenderer;
import com.stuintech.lacrimis.client.ObsidianTearFlyingParticle;
import com.stuintech.lacrimis.client.PurpleMistParticle;
import com.stuintech.lacrimis.entity.ModEntities;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

import static com.stuintech.lacrimis.init.ModParticles.OBSIDIAN_TEAR_FLYING;
import static com.stuintech.lacrimis.init.ModParticles.PURPLE_MIST;

public class ClientModRenderers {
    public static void registerClient() {
        //EntityRendererRegistry.INSTANCE.register(ModEntities.ghost, GhostEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.soulShell, SoulShellRenderer::new);
        EntityRendererRegistry.register(ModEntities.taintedPearl, FlyingItemEntityRenderer::new);

        BlockEntityRendererRegistry.register(ModEntities.crucible, CrucibleEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModEntities.infusionTable, InfusionTableEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModEntities.networkLink, NetworkLinkEntityRenderer::new);

        CrucibleEntityRenderer.onInit();
        InfusionTableEntityRenderer.onInit();

        ParticleFactoryRegistry.getInstance().register(PURPLE_MIST, PurpleMistParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(OBSIDIAN_TEAR_FLYING, ObsidianTearFlyingParticle.Factory::new);
    }
}
