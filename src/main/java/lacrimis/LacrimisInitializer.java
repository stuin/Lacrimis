package lacrimis;

import java.util.Map;

import com.google.common.collect.MapMaker;

import lacrimis.Lacrimis.BlockEntityTypes;
import lacrimis.Lacrimis.Blocks;
import lacrimis.blocks.conduits.ConduitsManager;
import lacrimis.client.particles.AzothMistParticle;
import lacrimis.client.render.CauldronEntityRenderer;
import lacrimis.client.render.InfusionTableEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.world.World;

@EnvironmentInterface(itf = ClientModInitializer.class, value = EnvType.CLIENT)
public class LacrimisInitializer implements ModInitializer, ClientModInitializer {
    private static Map<World, ConduitsManager> conduits = new MapMaker().weakKeys().makeMap();

    public static ConduitsManager getConduitManager(World world) {
        if (!conduits.containsKey(world)) {
            conduits.put(world, new ConduitsManager(world));
        }
        return conduits.get(world);
    }

    @Override
    public void onInitialize() {
        Lacrimis.Blocks.AZOTH_CAULDRON.getClass();
        Lacrimis.BlockEntityTypes.AZOTH_CAULDRON.getClass();
        Lacrimis.Items.AZOTH_CAULDRON.getClass();
        Lacrimis.ParticleTypes.AZOTH_MIST.getClass();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityTypes.AZOTH_CAULDRON, CauldronEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityTypes.INFUSION_TABLE, InfusionTableEntityRenderer::new);

        ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) -> {
            out.accept(CauldronEntityRenderer.CAULDRON_AZOTH_MODEL_ID);
            out.accept(InfusionTableEntityRenderer.INFUSION_TABLE_AZOTH_MODEL_ID);
        });

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), Blocks.AZOTH_CAULDRON, Blocks.AZOTH_CONDUIT);

        ParticleFactoryRegistry.getInstance().register(Lacrimis.ParticleTypes.AZOTH_MIST, AzothMistParticle.Factory::new);
    }
}
