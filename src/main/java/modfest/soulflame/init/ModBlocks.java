package modfest.soulflame.init;

import modfest.soulflame.block.*;
import modfest.soulflame.block.ConduitBlock;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import modfest.soulflame.SoulFlame;

public class ModBlocks {

    public static InfusionTableBlock infusionTable;
    public static CrucibleBlock crucible;
    public static ConduitBlock conduit;
    public static TearLantern tearLantern;
    public static CreativeTearsBlock creativeTearsBlock;

    public static void register() {
        infusionTable = register("infusion_table", new InfusionTableBlock(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).nonOpaque()));
        crucible = register("crucible", new CrucibleBlock(FabricBlockSettings.copy(Blocks.CAULDRON).nonOpaque()));
        conduit = register("conduit", new ConduitBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.25f)));
        tearLantern = register("tear_lantern", new TearLantern(AbstractBlock.Settings.of(Material.METAL).requiresTool().strength(3.5F).sounds(BlockSoundGroup.LANTERN).lightLevel((state) -> 10).nonOpaque()));
        creativeTearsBlock = register("creative_tears_block", new CreativeTearsBlock(FabricBlockSettings.copy(Blocks.STONE)));
    }

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(Registry.BLOCK, new Identifier(SoulFlame.MODID, name), block);
    }

}
