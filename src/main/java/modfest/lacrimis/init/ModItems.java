package modfest.lacrimis.init;

import modfest.lacrimis.item.BottleOfTearsItem;
import modfest.lacrimis.item.DiviningRodItem;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.item.armor.CustomArmorMaterials;
import modfest.lacrimis.item.armor.SoakedArmor;
import modfest.lacrimis.item.tools.CustomToolMaterials;
import modfest.lacrimis.item.tools.SoakenSword;

public class ModItems {
    private static final Item.Settings SETTINGS = new Item.Settings().group(Lacrimis.ITEM_GROUP);
    private static final Item.Settings TOOL_SETTINGS = new Item.Settings().group(Lacrimis.ITEM_GROUP).maxCount(1);
    private static final Item.Settings RUNE_SETTINGS = new Item.Settings().group(Lacrimis.RUNE_ITEM_GROUP);

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
    public static Item brokenSpawner;

    public static Item tearSoakenHelmet;
    public static Item tearSoakenChestplate;
    public static Item tearSoakenLeggings;
    public static Item tearSoakenBoots;

    public static Item tearSoakenSword;

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
    public static BlockItem spawnerRune;

    //Item tags
    public static Tag<Item> sludgeMaterials;

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
        wardedStone = register("warded", ModBlocks.wardedStone);

        bottleOfTears = register("bottle_of_tears", new BottleOfTearsItem(SETTINGS));
        diviningRod = register("divining_rod", new DiviningRodItem(TOOL_SETTINGS));
        tearIngot = register("tear_ingot", new Item(SETTINGS));
        warpedSigil = register("warped_sigil", new Item(SETTINGS));
        taintedSludge = register("tainted_sludge", new Item(SETTINGS));
        solidifiedTear = register("solidified_tear", new Item(SETTINGS));
        brokenSpawner = register("broken_spawner", new Item(SETTINGS));

        // I just realized the inconsistency of soaken/soaked woops, fix later or never >:D
        tearSoakenHelmet = register("tear_soaked_helmet",
                new SoakedArmor(CustomArmorMaterials.TEAR_SOAKEN, EquipmentSlot.HEAD, TOOL_SETTINGS));
        tearSoakenChestplate = register("tear_soaked_chestplate",
                new SoakedArmor(CustomArmorMaterials.TEAR_SOAKEN, EquipmentSlot.CHEST, TOOL_SETTINGS));
        tearSoakenLeggings = register("tear_soaked_leggings",
                new SoakedArmor(CustomArmorMaterials.TEAR_SOAKEN, EquipmentSlot.LEGS, TOOL_SETTINGS));
        tearSoakenBoots = register("tear_soaked_boots",
                new SoakedArmor(CustomArmorMaterials.TEAR_SOAKEN, EquipmentSlot.FEET, TOOL_SETTINGS));

        tearSoakenSword = register("tear_soaked_sword", new SoakenSword(CustomToolMaterials.SOAKEN, 3, -2.4F, TOOL_SETTINGS));

        runeStone = registerRune("rune/stone", ModBlocks.runeStone);
        rune1 = registerRune("rune/tier1", ModBlocks.rune1);
        rune2 = registerRune("rune/tier2", ModBlocks.rune2);
        rune3 = registerRune("rune/tier3", ModBlocks.rune3);
        flipRune = registerRune("rune/flip", ModBlocks.flipRune);
        pipeRune1 = registerRune("rune/pipe1", ModBlocks.pipeRune1);
        pipeRune2 = registerRune("rune/pipe2", ModBlocks.pipeRune2);
        healRune = registerRune("rune/healing", ModBlocks.healRune);
        extractionRune = registerRune("rune/extraction", ModBlocks.extractionRune);
        destinationRune = registerRune("rune/destination", ModBlocks.destinationRune);
        transportRune = registerRune("rune/entity_transport", ModBlocks.transportRune);
        blockTransportRune = registerRune("rune/block_transport", ModBlocks.blockTransportRune);
        wardingRune = registerRune("rune/warding", ModBlocks.wardingRune);
        spawnerRune = registerRune("rune/spawner", ModBlocks.spawnerRune);

        //Item tags
        sludgeMaterials = TagRegistry.item(new Identifier(Lacrimis.MODID, "sludge_materials"));
    }

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, new Identifier(Lacrimis.MODID, name), item);
    }

    private static BlockItem register(String name, Block block) {
        return register(name, new BlockItem(block, SETTINGS));
    }

    private static BlockItem registerRune(String name, Block block) {
        return register(name, new BlockItem(block, RUNE_SETTINGS));
    }

}
