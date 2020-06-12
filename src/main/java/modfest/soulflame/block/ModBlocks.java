package modfest.soulflame.block;

import modfest.soulflame.SoulFlame;
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
    public static Block infusionTable = new InfusionTable(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).nonOpaque());
    
    public static BlockEntityType<InfusionTableEntity> infusionTableEntity;

    public static void register() {
        //Blocks
        Registry.register(Registry.BLOCK, SoulFlame.MODID + ":infusion_table", infusionTable);

        //Block items
        Registry.register(Registry.ITEM, SoulFlame.MODID + ":infusion_table",
                new BlockItem(infusionTable, ModItems.settings));
        
        //Block entities
        infusionTableEntity = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(SoulFlame.MODID, "infusion_table_entity"),
                BlockEntityType.Builder.create(InfusionTableEntity::new, new Block[]{infusionTable}).build(null));

    }
}
