package modfest.soulflame.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.ConduitBlock;
import modfest.soulflame.block.CreativeTearsBlock;
import modfest.soulflame.block.CrucibleBlock;
import modfest.soulflame.block.InfusionTableBlock;

public class ModBlocks {

    public static InfusionTableBlock infusionTable;
    public static CrucibleBlock crucible;
    public static ConduitBlock conduit;
    public static CreativeTearsBlock creativeTearsBlock;

    public static void register() {
        infusionTable = register("infusion_table", new InfusionTableBlock(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).nonOpaque()));
        crucible = register("crucible", new CrucibleBlock(FabricBlockSettings.copy(Blocks.CAULDRON).nonOpaque()));
        conduit = register("conduit", new ConduitBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.25f)));
        creativeTearsBlock = register("creative_tears_block", new CreativeTearsBlock(FabricBlockSettings.copy(Blocks.STONE)));
    }

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(Registry.BLOCK, new Identifier(SoulFlame.MODID, name), block);
    }

}
