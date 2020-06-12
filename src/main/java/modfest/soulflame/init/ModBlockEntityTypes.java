package modfest.soulflame.init;

import modfest.soulflame.block.entity.TearLanternEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.entity.CrucibleEntity;
import modfest.soulflame.block.entity.InfusionTableEntity;
import modfest.soulflame.block.entity.TearLanternEntity;

public class ModBlockEntityTypes {

    public static BlockEntityType<InfusionTableEntity> infusionTable;
    public static BlockEntityType<CrucibleEntity> crucible;
    public static BlockEntityType<TearLanternEntity> tearLantern;

    public static void register() {
        infusionTable = register("infusion_table_entity", InfusionTableEntity::new, ModBlocks.infusionTable);
        crucible = register("crucible_entity", CrucibleEntity::new, ModBlocks.crucible);
        tearLantern = register("tear_lantern_entity", TearLanternEntity::new, ModBlocks.tearLantern);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, Supplier<T> f, Block... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(SoulFlame.MODID, name), BlockEntityType.Builder.create(f, blocks).build(null));
    }

}
