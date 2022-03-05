package modfest.lacrimis.client.init;

import modfest.lacrimis.client.ObsidianTearFlyingParticle;
import modfest.lacrimis.client.PurpleMistParticle;
import modfest.lacrimis.client.render.block.CrucibleEntityRenderer;
import modfest.lacrimis.client.render.block.InfusionTableEntityRenderer;
import modfest.lacrimis.client.render.block.NetworkLinkEntityRenderer;
import modfest.lacrimis.client.render.entity.SoulShellRenderer;
import modfest.lacrimis.entity.ModEntities;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

import static modfest.lacrimis.init.ModParticles.OBSIDIAN_TEAR_FLYING;
import static modfest.lacrimis.init.ModParticles.PURPLE_MIST;

public class ClientModRenderers {
    public static void registerClient() {
        //EntityRendererRegistry.INSTANCE.register(ModEntities.ghost, GhostEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntities.soulShell, SoulShellRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntities.taintedPearl, FlyingItemEntityRenderer::new);

        BlockEntityRendererRegistry.INSTANCE.register(ModEntities.crucible, CrucibleEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModEntities.infusionTable, InfusionTableEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModEntities.networkLink, NetworkLinkEntityRenderer::new);

        CrucibleEntityRenderer.onInit();
        InfusionTableEntityRenderer.onInit();

        ParticleFactoryRegistry.getInstance().register(PURPLE_MIST, PurpleMistParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(OBSIDIAN_TEAR_FLYING, ObsidianTearFlyingParticle.Factory::new);
    }
}
