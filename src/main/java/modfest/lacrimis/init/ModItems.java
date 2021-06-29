package modfest.lacrimis.init;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.item.*;
import modfest.lacrimis.item.armor.CustomArmorMaterials;
import modfest.lacrimis.item.armor.SoakedArmor;
import modfest.lacrimis.item.tools.CustomToolMaterials;
import modfest.lacrimis.item.tools.SoakedSwordItem;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    private static final Item.Settings SETTINGS = new Item.Settings().group(Lacrimis.ITEM_GROUP);
    private static final Item.Settings SMALL_SETTINGS = new Item.Settings().group(Lacrimis.ITEM_GROUP).maxCount(16);
    private static final Item.Settings TOOL_SETTINGS = new Item.Settings().group(Lacrimis.ITEM_GROUP).maxCount(1);
    private static final Item.Settings RUNE_SETTINGS = new Item.Settings().group(Lacrimis.RUNE_ITEM_GROUP);

    //Main blocks
    public static BlockItem crucible;
    public static BlockItem infusionTable;
    public static BlockItem combiner;
    public static BlockItem taintOutput;
    public static BlockItem networkLink;
    public static BlockItem duct;
    public static BlockItem gatedDuct;
    public static BlockItem oneWayDuct;
    public static BlockItem tearCollector;
    public static BlockItem tearLantern;
    public static BlockItem drainedCryingObsidian;
    public static BlockItem creativeTearsBlock;
    public static BlockItem wardedStone;
    public static BlockItem taint;
    public static BlockItem taintedStone;
    public static BlockItem taintedDirt;

    //Main items
    public static DiviningRodItem diviningRod;
    public static DiviningRodItem goldDiviningRod;
    public static BottleOfTearsItem bottleOfTears;
    public static Item tearIngot;
    public static Item taintedSludge;
    public static Item solidifiedTear;
    public static Item brokenSpawner;
    public static TaintedPearlItem taintedPearl;
    public static SoulShellItem soulShell;
    public static SoulTotemItem soulTotem;

    //Tools/armor
    public static Item tearSoakedHelmet;
    public static Item tearSoakedChestplate;
    public static Item tearSoakedLeggings;
    public static Item tearSoakedBoots;
    public static Item tearSoakedSword;

    //Rune cage blocks
    public static BlockItem runeStone;
    public static BlockItem rune1;
    public static BlockItem rune2;
    public static BlockItem rune3;
    public static BlockItem flipRune;
    public static BlockItem ductRune1;
    public static BlockItem ductRune2;

    //Rune center blocks
    public static BlockItem extractionRune;
    public static BlockItem destinationRune;
    public static BlockItem soulSwapRune;
    public static BlockItem transportRune;
    public static BlockItem blockTransportRune;
    public static BlockItem wardingRune;
    public static BlockItem spawnerRune;

    //Item tags
    public static Tag<Item> sludgeMaterials;

    public static void register() {
        crucible = register("crucible", ModBlocks.crucible);
        infusionTable = register("infusion_table", ModBlocks.infusionTable);
        runeStone = register("rune/stone", ModBlocks.runeStone);
        taintOutput = register("taint_output", ModBlocks.taintOutput);
        combiner = register("combiner", ModBlocks.combiner);
        networkLink = register("network_link", ModBlocks.networkLink);
        creativeTearsBlock = register("creative_tears_block", ModBlocks.creativeTearsBlock);
        wardedStone = register("warded", ModBlocks.wardedStone);
        drainedCryingObsidian = register("drained_crying_obsidian", ModBlocks.drainedCryingObsidian);
        taintedStone = register("tainted_stone", ModBlocks.taintedStone);
        taintedDirt = register("tainted_dirt", ModBlocks.taintedDirt);
        taint = register("taint", ModBlocks.taint);
        duct = register("duct", ModBlocks.duct);
        gatedDuct = register("gated_duct", ModBlocks.gatedDuct);
        oneWayDuct = register("one_way_duct", ModBlocks.oneWayDuct);
        tearCollector = register("tear_collector", ModBlocks.tearCollector);

        tearLantern = register("tear_lantern", ModBlocks.tearLantern);
        bottleOfTears = register("bottle_of_tears", new BottleOfTearsItem(SETTINGS));
        diviningRod = register("divining_rod", new DiviningRodItem(TOOL_SETTINGS));
        goldDiviningRod = register("gold_divining_rod", new DiviningRodItem(TOOL_SETTINGS));
        taintedSludge = register("tainted_sludge", new Item(SETTINGS));
        tearIngot = register("tear_ingot", new Item(SETTINGS));
        taintedPearl = register("tainted_pearl", new TaintedPearlItem(SMALL_SETTINGS));
        solidifiedTear = register("solidified_tear", new Item(SETTINGS));
        brokenSpawner = register("broken_spawner", new Item(SETTINGS));
        soulShell = register("soul_shell", new SoulShellItem(SMALL_SETTINGS));
        soulTotem = register("soul_totem", new SoulTotemItem(TOOL_SETTINGS));

        tearSoakedSword = register("tear_soaked_sword", new SoakedSwordItem(CustomToolMaterials.SOAKEN, 3, -2.4F, TOOL_SETTINGS));
        tearSoakedHelmet = registerArmor("tear_soaked_helmet", EquipmentSlot.HEAD);
        tearSoakedChestplate = registerArmor("tear_soaked_chestplate", EquipmentSlot.CHEST);
        tearSoakedLeggings = registerArmor("tear_soaked_leggings", EquipmentSlot.LEGS);
        tearSoakedBoots = registerArmor("tear_soaked_boots", EquipmentSlot.FEET);

        flipRune = registerRune("rune/flip", ModBlocks.flipRune);
        rune1 = registerRune("rune/tier1", ModBlocks.rune1);
        rune2 = registerRune("rune/tier2", ModBlocks.rune2);
        rune3 = registerRune("rune/tier3", ModBlocks.rune3);
        ductRune1 = registerRune("rune/duct1", ModBlocks.ductRune1);
        ductRune2 = registerRune("rune/duct2", ModBlocks.ductRune2);
        extractionRune = registerRune("rune/extraction", ModBlocks.extractionRune);
        destinationRune = registerRune("rune/destination", ModBlocks.destinationRune);
        transportRune = registerRune("rune/entity_transport", ModBlocks.transportRune);
        blockTransportRune = registerRune("rune/block_transport", ModBlocks.blockTransportRune);
        soulSwapRune = registerRune("rune/soul_swap", ModBlocks.soulSwapRune);
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

    private static SoakedArmor registerArmor(String name, EquipmentSlot slot) {
        return Registry.register(Registry.ITEM, new Identifier(Lacrimis.MODID, name), new SoakedArmor(CustomArmorMaterials.TEAR_SOAKEN, slot, TOOL_SETTINGS));
    }

    private static BlockItem registerRune(String name, Block block) {
        return register(name, new BlockItem(block, RUNE_SETTINGS));
    }

}
