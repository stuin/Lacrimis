package com.stuintech.lacrimis.block;

import com.stuintech.lacrimis.Lacrimis;
import com.stuintech.lacrimis.block.entity.*;
import com.stuintech.lacrimis.block.rune.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.TagKey;
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
    public static TaintOutputBlock taintOutput;
    public static NetworkLinkBlock networkLink;
    public static DrainedCryingObsidianBlock drainedCryingObsidian;
    public static CreativeTearsBlock creativeTearsBlock;

    public static Block runicStone;
    public static Block runicBrick;
    public static GlassBlock runicGlass;
    public static Block runicLight;
    public static WardedBlock wardedStone;
    public static WardedBlock wardedBrick;
    public static WardedGlassBlock wardedGlass;
    public static WardedBlock wardedLight;

    public static TaintedBlock taintedStone;
    public static TaintedBlock taintedDirt;
    public static TaintBlock taint;
    public static DuctBlock duct;
    public static GatedDuctBlock gatedDuct;
    public static OneWayDuctBlock oneWayDuct;
    public static TearCollectorBlock tearCollector;
    public static TearLanternBlock tearLantern;

    //Rune cage blocks
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
    public static TagKey<Block> cage_materials;
    public static TagKey<Block> non_transportable;
    public static TagKey<Block> resistant;
    public static TagKey<Block> tainted;
    public static TagKey<Block> tier1;
    public static TagKey<Block> tier2;
    public static TagKey<Block> tier3;

    public static void register() {
        crucible = register("crucible", new CrucibleBlock(Settings.copy(Blocks.CAULDRON).strength(2.0F, 1200.0F).nonOpaque()));
        infusionTable = register("infusion_table", new InfusionTableBlock(Settings.copy(Blocks.STONECUTTER).nonOpaque()));
        taintOutput = register("taint_output", new TaintOutputBlock(Settings.copy(Blocks.DISPENSER)));
        combiner = register("combiner", new CombinerBlock(Settings.copy(Blocks.DISPENSER)));
        networkLink = register("network_link", new NetworkLinkBlock(Settings.copy(Blocks.BEACON)));
        drainedCryingObsidian = register("drained_crying_obsidian", new DrainedCryingObsidianBlock(Settings.copy(Blocks.CRYING_OBSIDIAN)));
        creativeTearsBlock = register("creative_tears_block", new CreativeTearsBlock(wardedSettings));

        runicStone = register("runic_stone", new Block(runeSettings));
        runicBrick = register("runic_brick", new Block(runeSettings));
        runicGlass = register("runic_glass", new GlassBlock(FabricBlockSettings.copy(runicStone).nonOpaque()));
        runicLight = register("runic_light", new Block(FabricBlockSettings.copy(runicStone).luminance((state) -> 15)));
        wardedStone = register("warded_stone", new WardedBlock(wardedSettings));
        wardedBrick = register("warded_brick", new WardedBlock(wardedSettings));
        wardedGlass = register("warded_glass", new WardedGlassBlock(FabricBlockSettings.copy(wardedStone).nonOpaque()));
        wardedLight = register("warded_light", new WardedBlock(FabricBlockSettings.copy(wardedStone).luminance((state) -> 15)));

        taint = register("taint", new TaintBlock(FabricBlockSettings.copy(Blocks.OAK_LEAVES)));
        taintedStone = register("tainted_stone", new TaintedBlock(FabricBlockSettings.copy(Blocks.STONE)));
        taintedDirt = register("tainted_dirt", new TaintedBlock(FabricBlockSettings.copy(Blocks.DIRT)));
        duct = register("duct", new DuctBlock(ductSettings));
        gatedDuct = register("gated_duct", new GatedDuctBlock(ductSettings));
        oneWayDuct = register("one_way_duct", new OneWayDuctBlock(ductSettings));
        tearCollector = register("tear_collector", new TearCollectorBlock(Settings.copy(Blocks.DISPENSER)));
        tearLantern = register("tear_lantern", new TearLanternBlock(AbstractBlock.Settings.of(Material.METAL).requiresTool().strength(3.5F).sounds(BlockSoundGroup.LANTERN).luminance((state) -> 5).nonOpaque()));

        rune1 = register("rune/tier1", new BasicRuneBlock(1));
        rune2 = register("rune/tier2", new BasicRuneBlock(2));
        rune3 = register("rune/tier3", new AdvancedRuneBlock());
        ductRune1 = register("rune/duct1", new DuctRuneBlock(1));
        ductRune2 = register("rune/duct2", new DuctRuneBlock(2));
        flipRune = register("rune/flip", new Block(runeSettings));
        extractionRune = register("rune/extraction", new SoulExtractionBlock());
        destinationRune = register("rune/destination", new SoulTeleportBlock(false));
        transportRune = register("rune/entity_transport", new SoulTeleportBlock(true));
        blockTransportRune = register("rune/block_transport", new BlockTeleportBlock());
        soulSwapRune = register("rune/soul_swap", new SoulSwapBlock());
        wardingRune = register("rune/warding", new WardingBlock());
        spawnerRune = register("rune/spawner", new SpawnerBreakerBlock());

        //Block tags
        cage_materials = TagKey.of(Registry.BLOCK_KEY, new Identifier(Lacrimis.MODID, "cage_materials"));
        non_transportable = TagKey.of(Registry.BLOCK_KEY, new Identifier(Lacrimis.MODID, "non_transportable"));
        resistant = TagKey.of(Registry.BLOCK_KEY, new Identifier(Lacrimis.MODID, "resistant"));
        tainted = TagKey.of(Registry.BLOCK_KEY, new Identifier(Lacrimis.MODID, "tainted"));
        tier1 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Lacrimis.MODID, "tier1"));
        tier2 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Lacrimis.MODID, "tier2"));
        tier3 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Lacrimis.MODID, "tier3"));

        //Flammables
        FlammableBlockRegistry.getDefaultInstance().add(taintedDirt, 20, 5);
        FlammableBlockRegistry.getDefaultInstance().add(taintedStone, 20, 5);
    }

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(Registry.BLOCK, new Identifier(Lacrimis.MODID, name), block);
    }

}
