package modfest.lacrimis.init;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.*;
import modfest.lacrimis.block.rune.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final Block.Settings ductSettings = AbstractBlock.Settings.of(Material.STONE).strength(0.25f);
    public static final Block.Settings wardedSettings = FabricBlockSettings.copy(Blocks.STONE)
            .strength(-1.0F, 3600000.0F);
    public static final Block.Settings runeSettings = FabricBlockSettings.copy(Blocks.STONE);

    //Main blocks
    public static InfusionTableBlock infusionTable;
    public static CrucibleBlock crucible;
    public static CombinerBlock combiner;
    public static DuctBlock duct;
    public static GatedDuctBlock gatedDuct;
    public static OneWayDuctBlock oneWayDuct;
    public static TaintOutputBlock taintOutput;
    public static TearCollectorBlock tearCollector;
    public static TearLanternBlock tearLantern;
    public static NetworkLinkBlock networkLink;
    public static DrainedCryingObsidianBlock drainedCryingObsidian;
    public static CreativeTearsBlock creativeTearsBlock;
    public static WardedBlock wardedStone;
    public static Block taintedStone;
    public static Block taintedDirt;
    public static TaintBlock taint;

    //Rune cage blocks
    public static Block runeStone;
    public static Block flipRune;
    public static BasicRuneBlock rune1;
    public static BasicRuneBlock rune2;
    public static AdvancedRuneBlock rune3;
    public static DuctRuneBlock ductRune1;
    public static DuctRuneBlock ductRune2;

    //Rune center blocks
    public static SoulExtractionBlock extractionRune;
    public static SoulTeleportBlock destinationRune;
    public static SoulTeleportBlock transportRune;
    public static SoulSwapBlock soulSwapRune;
    public static BlockTeleportBlock blockTransportRune;
    public static WardingBlock wardingRune;
    public static SpawnerBreakerBlock spawnerRune;

    //Block tags
    public static Tag<Block> cage_materials;
    public static Tag<Block> non_transportable;
    public static Tag<Block> tier1;
    public static Tag<Block> tier2;
    public static Tag<Block> tier3;
    public static Tag<Block> tainted;

    public static void register() {
        infusionTable = register("infusion_table", new InfusionTableBlock(Settings.copy(Blocks.ENCHANTING_TABLE).nonOpaque()));
        crucible = register("crucible", new CrucibleBlock(Settings.copy(Blocks.CAULDRON).strength(5.0F, 1200.0F).nonOpaque()));
        combiner = register("combiner", new CombinerBlock(Settings.copy(Blocks.ENCHANTING_TABLE)));
        duct = register("duct", new DuctBlock(ductSettings));
        gatedDuct = register("gated_duct", new GatedDuctBlock(ductSettings));
        oneWayDuct = register("one_way_duct", new OneWayDuctBlock(ductSettings));
        taintOutput = register("taint_output", new TaintOutputBlock(Settings.copy(Blocks.DISPENSER)));
        tearCollector = register("tear_collector", new TearCollectorBlock(Settings.copy(Blocks.DISPENSER)));
        tearLantern = register("tear_lantern", new TearLanternBlock(AbstractBlock.Settings.of(Material.METAL).requiresTool().strength(3.5F).sounds(BlockSoundGroup.LANTERN).luminance((state) -> 5).nonOpaque()));
        networkLink = register("network_link", new NetworkLinkBlock(wardedSettings));
        drainedCryingObsidian = register("drained_crying_obsidian", new DrainedCryingObsidianBlock(Settings.copy(Blocks.CRYING_OBSIDIAN)));
        creativeTearsBlock = register("creative_tears_block", new CreativeTearsBlock(wardedSettings));
        wardedStone = register("warded", new WardedBlock(wardedSettings));
        taintedStone = register("tainted_stone", new Block(FabricBlockSettings.copy(Blocks.STONE).strength(1F)));
        taintedDirt = register("tainted_dirt", new Block(FabricBlockSettings.copy(Blocks.DIRT).strength(1F)));
        taint = register("taint", new TaintBlock(FabricBlockSettings.copy(Blocks.OAK_LEAVES).strength(1F)));

        runeStone = register("rune/stone", new Block(runeSettings));
        flipRune = register("rune/flip", new Block(runeSettings));
        rune1 = register("rune/tier1", new BasicRuneBlock(1));
        rune2 = register("rune/tier2", new BasicRuneBlock(2));
        rune3 = register("rune/tier3", new AdvancedRuneBlock());
        ductRune1 = register("rune/duct1", new DuctRuneBlock(1));
        ductRune2 = register("rune/duct2", new DuctRuneBlock(2));
        extractionRune = register("rune/extraction", new SoulExtractionBlock());
        destinationRune = register("rune/destination", new SoulTeleportBlock(false));
        soulSwapRune = register("rune/soul_swap", new SoulSwapBlock());
        transportRune = register("rune/entity_transport", new SoulTeleportBlock(true));
        blockTransportRune = register("rune/block_transport", new BlockTeleportBlock());
        wardingRune = register("rune/warding", new WardingBlock());
        spawnerRune = register("rune/spawner", new SpawnerBreakerBlock());

        //Block tags
        cage_materials = TagRegistry.block(new Identifier(Lacrimis.MODID, "cage_materials"));
        non_transportable = TagRegistry.block(new Identifier(Lacrimis.MODID, "non_transportable"));
        tier1 = TagRegistry.block(new Identifier(Lacrimis.MODID, "tier1"));
        tier2 = TagRegistry.block(new Identifier(Lacrimis.MODID, "tier2"));
        tier3 = TagRegistry.block(new Identifier(Lacrimis.MODID, "tier3"));
        tainted = TagRegistry.block(new Identifier(Lacrimis.MODID, "tainted"));
    }

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(Registry.BLOCK, new Identifier(Lacrimis.MODID, name), block);
    }

}
