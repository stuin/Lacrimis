package modfest.soulflame.init;

import modfest.soulflame.block.*;
import modfest.soulflame.block.ConduitBlock;
import modfest.soulflame.block.runic.HealCenterBlock;
import modfest.soulflame.block.runic.PipeRuneBlock;
import modfest.soulflame.block.runic.RuneBlock;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import modfest.soulflame.SoulFlame;

public class ModBlocks {
    //Soul power blocks
    public static InfusionTableBlock infusionTable;
    public static CrucibleBlock crucible;
    public static ConduitBlock conduit;
    public static TearLantern tearLantern;
    public static CreativeTearsBlock creativeTearsBlock;

    //Rune cage blocks
    public static RuneBlock rune1;
    public static RuneBlock rune2;
    public static PipeRuneBlock pipeRune;
    public static HealCenterBlock healRune;

    public static void register() {
        infusionTable = register("infusion_table", new InfusionTableBlock(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).nonOpaque()));
        crucible = register("crucible", new CrucibleBlock(FabricBlockSettings.copy(Blocks.CAULDRON).nonOpaque()));
        conduit = register("conduit", new ConduitBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.25f)));
        tearLantern = register("tear_lantern", new TearLantern(FabricBlockSettings.of(Material.METAL)));
        creativeTearsBlock = register("creative_tears_block", new CreativeTearsBlock(FabricBlockSettings.copy(Blocks.STONE)));
        rune1 = register("rune_tier1", new RuneBlock(FabricBlockSettings.copy(Blocks.STONE).nonOpaque(), 1));
        rune2 = register("rune_tier2", new RuneBlock(FabricBlockSettings.copy(Blocks.STONE).nonOpaque(), 2));
        pipeRune = register("pipe_rune", new PipeRuneBlock(FabricBlockSettings.copy(Blocks.STONE).nonOpaque()));
        healRune = register("heal_rune", new HealCenterBlock(FabricBlockSettings.copy(Blocks.STONE).nonOpaque()));

    }

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(Registry.BLOCK, new Identifier(SoulFlame.MODID, name), block);
    }

}
