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
    public static final Block.Settings conduitSettings = AbstractBlock.Settings.of(Material.STONE).strength(0.25f);
    public static final Block.Settings wardedSettings = FabricBlockSettings.copy(Blocks.STONE)
            .strength(-1.0F, 3600000.0F);
    public static final Block.Settings runeSettings = FabricBlockSettings.copy(Blocks.STONE);

    //Main blocks
    public static InfusionTableBlock infusionTable;
    public static CrucibleBlock crucible;
    public static CombinerBlock combiner;
    public static ConduitBlock conduit;
    public static GatedConduitBlock gatedConduit;
    public static OneWayConduitBlock oneWayConduit;
    public static TaintOutputBlock taintOutput;
    public static TearLantern tearLantern;
    public static DrainedCryingObsidianBlock drainedCryingObsidian;
    public static CreativeTearsBlock creativeTearsBlock;
    public static WardedBlock wardedStone;

    //Rune cage blocks
    public static Block runeStone;
    public static RuneBlock rune1;
    public static RuneBlock rune2;
    public static Block flipRune;
    public static AdvancedRuneBlock rune3;
    public static PipeConnectorBlock pipeRune1;
    public static PipeConnectorBlock pipeRune2;

    //Rune center blocks
    public static HealBlock healRune;
    public static SoulExtractionBlock extractionRune;
    public static SoulTeleportBlock destinationRune;
    public static SoulTeleportBlock transportRune;
    public static BlockTeleportBlock blockTransportRune;
    public static WardingBlock wardingRune;
    public static SpawnerBreakerBlock spawnerRune;
    public static TarotAdderBlock tarotAdderRune;

    //Block tags
    public static Tag<Block> cage_materials;
    public static Tag<Block> non_transportable;
    public static Tag<Block> tier1;
    public static Tag<Block> tier2;
    public static Tag<Block> tier3;

    public static void register() {
        infusionTable = register("infusion_table", new InfusionTableBlock(Settings.copy(Blocks.ENCHANTING_TABLE).nonOpaque()));
        crucible = register("crucible", new CrucibleBlock(Settings.copy(Blocks.CAULDRON).strength(5.0F, 1200.0F).nonOpaque()));
        combiner = register("combiner", new CombinerBlock(Settings.copy(Blocks.ENCHANTING_TABLE)));
        conduit = register("conduit", new ConduitBlock(conduitSettings));
        gatedConduit = register("gated_conduit", new GatedConduitBlock(conduitSettings));
        oneWayConduit = register("one_way_conduit", new OneWayConduitBlock(conduitSettings));
        taintOutput = register("taint_output", new TaintOutputBlock(Settings.copy(Blocks.DISPENSER)));
        tearLantern = register("tear_lantern", new TearLantern(AbstractBlock.Settings.of(Material.METAL).requiresTool().strength(3.5F).sounds(BlockSoundGroup.LANTERN).lightLevel((state) -> 5).nonOpaque()));
        drainedCryingObsidian = register("drained_crying_obsidian", new DrainedCryingObsidianBlock(Settings.copy(Blocks.CRYING_OBSIDIAN)));
        creativeTearsBlock = register("creative_tears_block", new CreativeTearsBlock(wardedSettings));
        wardedStone = register("warded", new WardedBlock(wardedSettings));

        runeStone = register("rune/stone", new Block(runeSettings));
        rune1 = register("rune/tier1", new RuneBlock(1));
        rune2 = register("rune/tier2", new RuneBlock(2));
        rune3 = register("rune/tier3", new AdvancedRuneBlock());
        flipRune = register("rune/flip", new Block(runeSettings));
        pipeRune1 = register("rune/pipe1", new PipeConnectorBlock(1));
        pipeRune2 = register("rune/pipe2", new PipeConnectorBlock(2));
        healRune = register("rune/healing", new HealBlock());
        extractionRune = register("rune/extraction", new SoulExtractionBlock());
        destinationRune = register("rune/destination", new SoulTeleportBlock(false));
        transportRune = register("rune/entity_transport", new SoulTeleportBlock(true));
        blockTransportRune = register("rune/block_transport", new BlockTeleportBlock());
        wardingRune = register("rune/warding", new WardingBlock());
        spawnerRune = register("rune/spawner", new SpawnerBreakerBlock());
        tarotAdderRune = register("rune/tarot_adder", new TarotAdderBlock());

        //Block tags
        cage_materials = TagRegistry.block(new Identifier(Lacrimis.MODID, "cage_materials"));
        non_transportable = TagRegistry.block(new Identifier(Lacrimis.MODID, "non_transportable"));
        tier1 = TagRegistry.block(new Identifier(Lacrimis.MODID, "tier1"));
        tier2 = TagRegistry.block(new Identifier(Lacrimis.MODID, "tier2"));
        tier3 = TagRegistry.block(new Identifier(Lacrimis.MODID, "tier3"));
    }

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(Registry.BLOCK, new Identifier(Lacrimis.MODID, name), block);
    }

}
