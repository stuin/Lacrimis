package modfest.lacrimis.init;

import modfest.lacrimis.block.entity.CombinerEntity;
import modfest.lacrimis.block.entity.CrucibleEntity;
import modfest.lacrimis.block.entity.InfusionTableEntity;
import modfest.lacrimis.block.entity.TearLanternEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.entity.GhostEntity;

import java.util.function.Supplier;

public class ModEntityTypes {

    public static EntityType<GhostEntity> ghost;

    //Block entities
    public static BlockEntityType<InfusionTableEntity> infusionTable;
    public static BlockEntityType<CrucibleEntity> crucible;
    public static BlockEntityType<CombinerEntity> combiner;
    public static BlockEntityType<TearLanternEntity> tearLantern;

    public static void register() {
        ghost = Registry.register(Registry.ENTITY_TYPE, new Identifier(Lacrimis.MODID, "ghost"),
                FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GhostEntity::new)
                        .dimensions(EntityDimensions.fixed(0.75f, 2.0f))
                        .trackable(64, 4)
                        .fireImmune()
                        .build());
        FabricDefaultAttributeRegistry.register(ghost, GhostEntity.createGhostAttributes());

        infusionTable = register("infusion_table_entity", InfusionTableEntity::new, ModBlocks.infusionTable);
        crucible = register("crucible_entity", CrucibleEntity::new, ModBlocks.crucible);
        combiner = register("combiner_entity", CombinerEntity::new, ModBlocks.combiner);
        tearLantern = register("tear_lantern_entity", TearLanternEntity::new, ModBlocks.tearLantern);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, Supplier<T> f, Block... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Lacrimis.MODID, name), BlockEntityType.Builder.create(f, blocks).build(null));
    }
}
