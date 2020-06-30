package azoth.common;

import azoth.Azoth;
import azoth.common.blocks.CrucibleBlock;
import azoth.common.blocks.InfusionTableBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;

public class AzothBlocks {
    public static final Block CRUCIBLE = new CrucibleBlock(FabricBlockSettings.copy(Blocks.CAULDRON));
    public static final Block INFUSION_TABLE = new InfusionTableBlock(FabricBlockSettings.copy(Blocks.CAULDRON));

    public static void register() {
        Registry.register(Registry.BLOCK, Azoth.createID("crucible"), CRUCIBLE);
        Registry.register(Registry.BLOCK, Azoth.createID("infusion_table"), INFUSION_TABLE);
    }
}
