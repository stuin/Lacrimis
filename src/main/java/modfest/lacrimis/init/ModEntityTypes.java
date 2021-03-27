package modfest.lacrimis.init;

import modfest.lacrimis.block.entity.CombinerEntity;
import modfest.lacrimis.block.entity.CrucibleEntity;
import modfest.lacrimis.block.entity.InfusionTableEntity;
import modfest.lacrimis.block.entity.TearLanternEntity;
import modfest.lacrimis.client.render.block.CrucibleEntityRenderer;
import modfest.lacrimis.client.render.block.InfusionTableEntityRenderer;
import modfest.lacrimis.client.render.entity.GhostEntityRenderer;
import modfest.lacrimis.entity.TaintedPearlEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.entity.GhostEntity;

import java.util.function.Supplier;

public class ModEntityTypes {

    public static EntityType<GhostEntity> ghost;
    public static EntityType<TaintedPearlEntity> taintedPearl;

    //Block entities
    public static BlockEntityType<InfusionTableEntity> infusionTable;
    public static BlockEntityType<CrucibleEntity> crucible;
    public static BlockEntityType<CombinerEntity> combiner;
    public static BlockEntityType<TearLanternEntity> tearLantern;

    public static void register() {
        ghost = Registry.register(Registry.ENTITY_TYPE, new Identifier(Lacrimis.MODID, "ghost"),
                FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GhostEntity::new)
                        .dimensions(EntityDimensions.fixed(0.75f, 2.0f))
                        .trackRangeBlocks(64).trackedUpdateRate(4)
                        .fireImmune()
                        .build());
        FabricDefaultAttributeRegistry.register(ghost, GhostEntity.createGhostAttributes());
        taintedPearl = Registry.register(Registry.ENTITY_TYPE, new Identifier(Lacrimis.MODID, "tainted_pearl"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, TaintedPearlEntity::new)
                        .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                        .trackRangeChunks(4).trackedUpdateRate(10)
                        .build());

        infusionTable = register("infusion_table_entity", InfusionTableEntity::new, ModBlocks.infusionTable);
        crucible = register("crucible_entity", CrucibleEntity::new, ModBlocks.crucible);
        combiner = register("combiner_entity", CombinerEntity::new, ModBlocks.combiner);
        tearLantern = register("tear_lantern_entity", TearLanternEntity::new, ModBlocks.tearLantern);
    }
    
    public static void registerClient() {
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.ghost,
                (dispatcher, ctx) -> new GhostEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.taintedPearl,
                (dispatcher, ctx) -> new FlyingItemEntityRenderer<TaintedPearlEntity>(dispatcher, ctx.getItemRenderer()));

        BlockEntityRendererRegistry.INSTANCE.register(ModEntityTypes.crucible, CrucibleEntityRenderer::new);
        CrucibleEntityRenderer.onInit();
        BlockEntityRendererRegistry.INSTANCE.register(ModEntityTypes.infusionTable, InfusionTableEntityRenderer::new);
        InfusionTableEntityRenderer.onInit();
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, Supplier<T> f, Block... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Lacrimis.MODID, name), BlockEntityType.Builder.create(f, blocks).build(null));
    }
}
