package azoth.common;

import com.google.common.collect.ImmutableSet;

import azoth.Azoth;
import azoth.common.blocks.AzothCauldronBlock;
import azoth.common.blocks.AzothConduitBlock;
import azoth.common.blocks.InfusionTableBlock;
import azoth.common.blocks.entity.AzothCauldronBlockEntity;
import azoth.common.blocks.entity.InfusionTableBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class AzothBlocks {
    public static final Block INFUSION_TABLE = new InfusionTableBlock(FabricBlockSettings.copy(Blocks.CAULDRON));
    public static final Block AZOTH_CONDUIT = new AzothConduitBlock(FabricBlockSettings.copy(Blocks.CAULDRON));
    public static final Block AZOTH_CAULDRON = new AzothCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON).dropsLike(Blocks.CAULDRON));

    public static final BlockEntityType<AzothCauldronBlockEntity> CRUCIBLE_ENTITY = new BlockEntityType<>(AzothCauldronBlockEntity::new, ImmutableSet.of(AZOTH_CAULDRON), null);
    public static final BlockEntityType<InfusionTableBlockEntity> INFUSION_TABLE_ENTITY = new BlockEntityType<>(InfusionTableBlockEntity::new, ImmutableSet.of(INFUSION_TABLE), null);

    public static void register() {
        Registry.register(Registry.BLOCK, Azoth.createID("infusion_table"), INFUSION_TABLE);
        Registry.register(Registry.BLOCK, Azoth.createID("conduit"), AZOTH_CONDUIT);
        Registry.register(Registry.BLOCK, Azoth.createID("cauldron"), AZOTH_CAULDRON);

        Registry.register(Registry.BLOCK_ENTITY_TYPE, Azoth.createID("cauldron"), CRUCIBLE_ENTITY);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, Azoth.createID("infusion_table"), INFUSION_TABLE_ENTITY);
    }
}
