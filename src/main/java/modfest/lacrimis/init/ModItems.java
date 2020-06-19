package modfest.lacrimis.init;

import java.util.Map;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.item.BottleOfTearsItem;
import modfest.lacrimis.item.DiviningRodItem;
import modfest.lacrimis.item.TarotCardItem;
import modfest.lacrimis.item.armor.CustomArmorMaterials;
import modfest.lacrimis.tarot.TarotCardType;
import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    private static final Item.Settings SETTINGS = new Item.Settings().group(Lacrimis.ITEM_GROUP);
    private static final Item.Settings TOOL_SETTINGS = new Item.Settings().group(Lacrimis.ITEM_GROUP).maxCount(1);
    private static final Item.Settings RUNE_SETTINGS = new Item.Settings().group(Lacrimis.RUNE_ITEM_GROUP);
    private static final Item.Settings TAROT_SETTINGS = new Item.Settings().group(Lacrimis.TAROT_ITEM_GROUP);

    //Main blocks
    public static BlockItem infusionTable;
    public static BlockItem crucible;
    public static BlockItem combiner;
    public static BlockItem conduit;
    public static BlockItem gatedConduit;
    public static BlockItem oneWayConduit;
    public static BlockItem taintOutput;
    public static BlockItem tearLantern;
    public static BlockItem drainedCryingObsidian;
    public static BlockItem creativeTearsBlock;
    public static BlockItem wardedStone;

    //Main items
    public static BottleOfTearsItem bottleOfTears;
    public static DiviningRodItem diviningRod;
    public static Item tearIngot;
    public static Item warpedSigil;
    public static Item taintedSludge;
    public static Item solidifiedTear;

    public static Item tearSoakenHelmet;
    public static Item tearSoakenChestplate;
    public static Item tearSoakenLeggings;
    public static Item tearSoakenBoots;

    // Tarot cards
    public static Item baseTarot;
    public static Map<TarotCardType, TarotCardItem> tarotCards;

    //Rune cage blocks
    public static BlockItem runeStone;
    public static BlockItem rune1;
    public static BlockItem rune2;
    public static BlockItem rune3;
    public static BlockItem flipRune;
    public static BlockItem pipeRune1;
    public static BlockItem pipeRune2;

    //Rune center blocks
    public static BlockItem healRune;
    public static BlockItem extractionRune;
    public static BlockItem destinationRune;
    public static BlockItem transportRune;
    public static BlockItem blockTransportRune;
    public static BlockItem wardingRune;

    public static void register() {
        infusionTable = register("infusion_table", ModBlocks.infusionTable);
        crucible = register("crucible", ModBlocks.crucible);
        combiner = register("combiner", ModBlocks.combiner);
        conduit = register("conduit", ModBlocks.conduit);
        gatedConduit = register("gated_conduit", ModBlocks.gatedConduit);
        oneWayConduit = register("one_way_conduit", ModBlocks.oneWayConduit);
        taintOutput = register("taint_output", ModBlocks.taintOutput);
        tearLantern = register("tear_lantern", ModBlocks.tearLantern);
        drainedCryingObsidian = register("drained_crying_obsidian", ModBlocks.drainedCryingObsidian);
        creativeTearsBlock = register("creative_tears_block", ModBlocks.creativeTearsBlock);
        wardedStone = register("warded_stone", ModBlocks.wardedStone);

        bottleOfTears = register("bottle_of_tears", new BottleOfTearsItem(SETTINGS));
        diviningRod = register("divining_rod", new DiviningRodItem(TOOL_SETTINGS));
        tearIngot = register("tear_ingot", new Item(SETTINGS));
        warpedSigil = register("warped_sigil", new Item(SETTINGS));
        taintedSludge = register("tainted_sludge", new Item(SETTINGS));
        solidifiedTear = register("solidified_tear", new Item(SETTINGS));

        tearSoakenHelmet = register("tear_soaked_helmet",
                new ArmorItem(CustomArmorMaterials.TEAR_SOAKEN, EquipmentSlot.HEAD, TOOL_SETTINGS));
        tearSoakenChestplate = register("tear_soaked_chestplate",
                new ArmorItem(CustomArmorMaterials.TEAR_SOAKEN, EquipmentSlot.CHEST, TOOL_SETTINGS));
        tearSoakenLeggings = register("tear_soaked_leggings",
                new ArmorItem(CustomArmorMaterials.TEAR_SOAKEN, EquipmentSlot.LEGS, TOOL_SETTINGS));
        tearSoakenBoots = register("tear_soaked_boots",
                new ArmorItem(CustomArmorMaterials.TEAR_SOAKEN, EquipmentSlot.FEET, TOOL_SETTINGS));

        baseTarot = register("base_tarot", new Item(TAROT_SETTINGS));
        tarotCards = TarotCardType.tarotInit(k -> register(String.format("tarot_card_%s", k.id), new TarotCardItem(k, TAROT_SETTINGS)));

        runeStone = register2("rune/stone", ModBlocks.runeStone);
        rune1 = register2("rune/tier1", ModBlocks.rune1);
        rune2 = register2("rune/tier2", ModBlocks.rune2);
        rune3 = register2("rune/tier3", ModBlocks.rune3);
        flipRune = register2("rune/flip", ModBlocks.flipRune);
        pipeRune1 = register2("rune/pipe1", ModBlocks.pipeRune1);
        pipeRune2 = register2("rune/pipe2", ModBlocks.pipeRune2);
        healRune = register2("rune/healing", ModBlocks.healRune);
        extractionRune = register2("rune/extraction", ModBlocks.extractionRune);
        destinationRune = register2("rune/destination", ModBlocks.destinationRune);
        transportRune = register2("rune/entity_transport", ModBlocks.transportRune);
        blockTransportRune = register2("rune/block_transport", ModBlocks.blockTransportRune);
        wardingRune = register2("rune/warding", ModBlocks.wardingRune);

    }

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, new Identifier(Lacrimis.MODID, name), item);
    }

    private static BlockItem register(String name, Block block) {
        return register(name, new BlockItem(block, SETTINGS));
    }

    private static BlockItem register2(String name, Block block) {
        return register(name, new BlockItem(block, RUNE_SETTINGS));
    }

}
