package modfest.soulflame.block;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.entity.CrucibleEntity;
import modfest.soulflame.block.entity.InfusionTableEntity;
import modfest.soulflame.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final Block infusionTable = new InfusionTable(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).nonOpaque());
    public static final Block crucible = new Crucible(FabricBlockSettings.copy(Blocks.CAULDRON).nonOpaque());
    
    public static BlockEntityType<InfusionTableEntity> infusionTableEntity;
    public static BlockEntityType<CrucibleEntity> crucibleEntity;

    public static void register() {
        //Blocks
        Registry.register(Registry.BLOCK, SoulFlame.MODID + ":infusion_table", infusionTable);
        Registry.register(Registry.BLOCK, SoulFlame.MODID + ":crucible", crucible);

        //Block items
        Registry.register(Registry.ITEM, SoulFlame.MODID + ":infusion_table",
                new BlockItem(infusionTable, ModItems.settings));
        Registry.register(Registry.ITEM, SoulFlame.MODID + ":crucible",
                new BlockItem(crucible, ModItems.settings));
        
        //Block entities
        infusionTableEntity = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(SoulFlame.MODID, "infusion_table_entity"),
                BlockEntityType.Builder.create(InfusionTableEntity::new, new Block[]{infusionTable}).build(null));
        crucibleEntity = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(SoulFlame.MODID, "crucible_entity"),
                BlockEntityType.Builder.create(CrucibleEntity::new, new Block[]{crucible}).build(null));

    }
}
