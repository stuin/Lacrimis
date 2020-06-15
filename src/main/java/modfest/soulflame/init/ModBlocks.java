package modfest.soulflame.init;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.ConduitBlock;
import modfest.soulflame.block.CreativeTearsBlock;
import modfest.soulflame.block.CrucibleBlock;
import modfest.soulflame.block.DrainedCryingObsidianBlock;
import modfest.soulflame.block.GatedConduitBlock;
import modfest.soulflame.block.InfusionTableBlock;
import modfest.soulflame.block.TearLantern;
import modfest.soulflame.block.rune.BlockTeleportBlock;
import modfest.soulflame.block.rune.HealBlock;
import modfest.soulflame.block.rune.PipeConnectorBlock;
import modfest.soulflame.block.rune.RuneBlock;
import modfest.soulflame.block.rune.SoulExtractionBlock;
import modfest.soulflame.block.rune.SoulTeleportBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final Block.Settings runeSettings = FabricBlockSettings.copy(Blocks.STONE).nonOpaque();
    public static Tag<Block> conductive;

    //Main blocks
    public static InfusionTableBlock infusionTable;
    public static CrucibleBlock crucible;
    public static ConduitBlock conduit;
    public static GatedConduitBlock gatedConduit;
    public static TearLantern tearLantern;
    public static CreativeTearsBlock creativeTearsBlock;
    public static DrainedCryingObsidianBlock drainedCryingObsidian;

    //Rune cage blocks
    public static RuneBlock rune1;
    public static RuneBlock rune2;
    public static PipeConnectorBlock pipeRune1;
    public static PipeConnectorBlock pipeRune2;
    public static Block flipRune;
    public static HealBlock healRune;
    public static SoulExtractionBlock extractionRune;
    public static SoulTeleportBlock destinationRune;
    public static SoulTeleportBlock transportRune;
    public static BlockTeleportBlock blockTransportRune;

    public static void register() {
        infusionTable = register("infusion_table", new InfusionTableBlock(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).nonOpaque()));
        crucible = register("crucible", new CrucibleBlock(FabricBlockSettings.copy(Blocks.CAULDRON).nonOpaque()));
        conduit = register("conduit", new ConduitBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.25f)));
        gatedConduit = register("gated_conduit", new GatedConduitBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.25f)));
        tearLantern = register("tear_lantern", new TearLantern(AbstractBlock.Settings.of(Material.METAL).requiresTool().strength(3.5F).sounds(BlockSoundGroup.LANTERN).lightLevel((state) -> 5).nonOpaque()));
        creativeTearsBlock = register("creative_tears_block", new CreativeTearsBlock(FabricBlockSettings.copy(Blocks.STONE)));
        drainedCryingObsidian = register("drained_crying_obsidian", new DrainedCryingObsidianBlock(FabricBlockSettings.copy(Blocks.CRYING_OBSIDIAN)));

        rune1 = register("rune/tier1", new RuneBlock(1));
        rune2 = register("rune/tier2", new RuneBlock(2));
        pipeRune1 = register("rune/pipe1", new PipeConnectorBlock(1));
        pipeRune2 = register("rune/pipe2", new PipeConnectorBlock(2));
        flipRune = register("rune/flip", new Block(runeSettings));
        healRune = register("rune/healing", new HealBlock());
        extractionRune = register("rune/extraction", new SoulExtractionBlock());
        destinationRune = register("rune/destination", new SoulTeleportBlock(false));
        transportRune = register("rune/entity_transport", new SoulTeleportBlock(true));
        blockTransportRune = register("rune/block_transport", new BlockTeleportBlock());

        conductive = TagRegistry.block(new Identifier(SoulFlame.MODID, "conductive"));
    }

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(Registry.BLOCK, new Identifier(SoulFlame.MODID, name), block);
    }

}
