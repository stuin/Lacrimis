package modfest.lacrimis.init;

import modfest.lacrimis.block.entity.*;
import modfest.lacrimis.entity.SoulShellEntity;
import modfest.lacrimis.entity.TaintedPearlEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.entity.GhostEntity;

import java.util.function.Supplier;

public class ModEntities {

    public static EntityType<GhostEntity> ghost;
    public static EntityType<SoulShellEntity> soulShell;
    public static EntityType<TaintedPearlEntity> taintedPearl;

    //Block entities
    public static BlockEntityType<InfusionTableEntity> infusionTable;
    public static BlockEntityType<CrucibleEntity> crucible;
    public static BlockEntityType<CombinerEntity> combiner;
    public static BlockEntityType<TearLanternEntity> tearLantern;
    public static BlockEntityType<NetworkLinkEntity> networkLink;

    public static void register() {
        ghost = register("ghost", buildType(SpawnGroup.MONSTER, GhostEntity::new, 0.75f, 2.0f).trackRangeBlocks(64).trackedUpdateRate(4).fireImmune());
        soulShell = register("soul_shell", buildType(SpawnGroup.MISC, SoulShellEntity::new, 0.6F, 1.8F));
        taintedPearl = register("tainted_pearl", buildType(SpawnGroup.MISC, TaintedPearlEntity::new, 0.25F, 0.25F).trackRangeChunks(4).trackedUpdateRate(10));

        FabricDefaultAttributeRegistry.register(ghost, GhostEntity.createGhostAttributes());
        FabricDefaultAttributeRegistry.register(soulShell, SoulShellEntity.createSoulShellAttributes());

        infusionTable = register("infusion_table_entity", InfusionTableEntity::new, ModBlocks.infusionTable);
        crucible = register("crucible_entity", CrucibleEntity::new, ModBlocks.crucible);
        combiner = register("combiner_entity", CombinerEntity::new, ModBlocks.combiner);
        tearLantern = register("tear_lantern_entity", TearLanternEntity::new, ModBlocks.tearLantern);
        networkLink = register("network_link_entity", NetworkLinkEntity::new, ModBlocks.networkLink);
    }

    private static <T extends Entity> EntityType<T> register(String name, FabricEntityTypeBuilder<T> builder) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Lacrimis.MODID, name), builder.build());
    }

    private static <T extends Entity> FabricEntityTypeBuilder<T> buildType(SpawnGroup group, EntityType.EntityFactory<T> f, float width, float height) {
        return FabricEntityTypeBuilder.create(group, f).dimensions(EntityDimensions.fixed(width, height));
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, Supplier<T> f, Block... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Lacrimis.MODID, name), BlockEntityType.Builder.create(f, blocks).build(null));
    }
}
