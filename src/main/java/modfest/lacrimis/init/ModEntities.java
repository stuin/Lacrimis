package modfest.lacrimis.init;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.*;
import modfest.lacrimis.entity.GhostEntity;
import modfest.lacrimis.entity.SoulShellEntity;
import modfest.lacrimis.entity.TaintedPearlEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

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

    public static TagKey<EntityType<?>> combiner_blocked;

    public static void register() {
        ghost = register("ghost", buildType(SpawnGroup.MONSTER, (type, world) -> new GhostEntity(world), 0.75f, 2.0f, 64, 4));
        soulShell = register("soul_shell", buildType((type, world) -> new SoulShellEntity(world), 0.6F, 1.8F));
        taintedPearl = register("tainted_pearl", buildType(SpawnGroup.MISC, (type, world) -> new TaintedPearlEntity(world), 0.25F, 0.25F, 4, 10));

        FabricDefaultAttributeRegistry.register(ghost, GhostEntity.createGhostAttributes());
        FabricDefaultAttributeRegistry.register(soulShell, SoulShellEntity.createSoulShellAttributes());

        infusionTable = register("infusion_table_entity", InfusionTableEntity::new, ModBlocks.infusionTable);
        crucible = register("crucible_entity", CrucibleEntity::new, ModBlocks.crucible);
        combiner = register("combiner_entity", CombinerEntity::new, ModBlocks.combiner);
        tearLantern = register("tear_lantern_entity", TearLanternEntity::new, ModBlocks.tearLantern);
        networkLink = register("network_link_entity", NetworkLinkEntity::new, ModBlocks.networkLink);


        combiner_blocked = TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(Lacrimis.MODID, "combiner_blocked"));
    }

    private static <T extends Entity> EntityType<T> register(String name, FabricEntityTypeBuilder<T> builder) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Lacrimis.MODID, name), builder.build());
    }

    private static <T extends Entity> FabricEntityTypeBuilder<T> buildType(EntityType.EntityFactory<T> f, float width, float height) {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, f).dimensions(EntityDimensions.fixed(width, height));
    }

    private static <T extends Entity> FabricEntityTypeBuilder<T> buildType(SpawnGroup group, EntityType.EntityFactory<T> f, float width, float height, int trackRange, int updateRate) {
        return FabricEntityTypeBuilder.create(group, f).dimensions(EntityDimensions.fixed(width, height)).trackRangeBlocks(trackRange).trackedUpdateRate(updateRate);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Lacrimis.MODID, name), FabricBlockEntityTypeBuilder.create(factory, blocks).build(null));
    }

    @Nullable
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }
}
