package modfest.soulflame.init;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.item.BottleOfTearsItem;
import modfest.soulflame.item.DiviningRodItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    private static final Item.Settings SETTINGS = new Item.Settings().group(SoulFlame.ITEM_GROUP);

    //Main blocks
    public static BlockItem infusionTable;
    public static BlockItem crucible;
    public static BlockItem conduit;
    public static BlockItem gatedConduit;
    public static BlockItem tearLantern;
    public static BlockItem creativeTearsBlock;
    public static BlockItem drainedCryingObsidian;

    //Rune cage blocks
    public static BlockItem rune1;
    public static BlockItem rune2;
    public static BlockItem pipeRune1;
    public static BlockItem pipeRune2;
    public static BlockItem flipRune;
    public static BlockItem healRune;
    public static BlockItem extractionRune;
    public static BlockItem destinationRune;
    public static BlockItem transportRune;
    public static BlockItem blockTransportRune;

    //Main items
    public static BottleOfTearsItem bottleOfTears;
    public static DiviningRodItem diviningRod;
    public static Item tearIngot;

    public static void register() {
        infusionTable = register("infusion_table", ModBlocks.infusionTable);
        crucible = register("crucible", ModBlocks.crucible);
        conduit = register("conduit", ModBlocks.conduit);
        gatedConduit = register("gated_conduit", ModBlocks.gatedConduit);
        tearLantern = register("tear_lantern", ModBlocks.tearLantern);
        creativeTearsBlock = register("creative_tears_block", ModBlocks.creativeTearsBlock);
        drainedCryingObsidian = register("drained_crying_obsidian", ModBlocks.drainedCryingObsidian);

        rune1 = register("rune/tier1", ModBlocks.rune1);
        rune2 = register("rune/tier2", ModBlocks.rune2);
        pipeRune1 = register("rune/pipe1", ModBlocks.pipeRune1);
        pipeRune2 = register("rune/pipe2", ModBlocks.pipeRune2);
        flipRune = register("rune/flip", ModBlocks.flipRune);
        healRune = register("rune/healing", ModBlocks.healRune);
        extractionRune = register("rune/extraction", ModBlocks.extractionRune);
        destinationRune = register("rune/destination", ModBlocks.destinationRune);
        transportRune = register("rune/entity_transport", ModBlocks.transportRune);
        blockTransportRune = register("rune/block_transport", ModBlocks.blockTransportRune);

        bottleOfTears = register("bottle_of_tears", new BottleOfTearsItem(SETTINGS));
        diviningRod = register("divining_rod", new DiviningRodItem(SETTINGS));
        tearIngot = register("tear_ingot", new Item((new Item.Settings()).group(SoulFlame.ITEM_GROUP)));
    }

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, new Identifier(SoulFlame.MODID, name), item);
    }

    private static BlockItem register(String name, Block block) {
        return register(name, new BlockItem(block, SETTINGS));
    }

}
