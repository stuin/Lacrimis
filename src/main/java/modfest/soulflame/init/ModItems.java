package modfest.soulflame.init;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.item.BottleOfTearsItem;
import modfest.soulflame.item.DiviningRodItem;
import modfest.soulflame.item.armor.CustomArmorMaterials;
import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    private static final Item.Settings SETTINGS = new Item.Settings().group(SoulFlame.ITEM_GROUP);
    private static final Item.Settings TOOLSETTINGS = new Item.Settings().group(SoulFlame.ITEM_GROUP).maxCount(1);
    private static final Item.Settings TAROT_SETTINGS = new Item.Settings().group(SoulFlame.TAROT_ITEM_GROUP);

    //Main blocks
    public static BlockItem infusionTable;
    public static BlockItem crucible;
    public static BlockItem conduit;
    public static BlockItem gatedConduit;
    public static BlockItem oneWayConduit;
    public static BlockItem tearLantern;
    public static BlockItem drainedCryingObsidian;
    public static BlockItem creativeTearsBlock;

    //Main items
    public static BottleOfTearsItem bottleOfTears;
    public static DiviningRodItem diviningRod;
    public static Item tearIngot;

    public static Item tearSoakenHelmet;
    public static Item tearSoakenChestplate;
    public static Item tearSoakenLeggings;
    public static Item tearSoakenBoots;

    // Tarot cards
    public static TarotCardItem baseTarot;

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
        conduit = register("conduit", ModBlocks.conduit);
        gatedConduit = register("gated_conduit", ModBlocks.gatedConduit);
        oneWayConduit = register("one_way_conduit", ModBlocks.oneWayConduit);
        tearLantern = register("tear_lantern", ModBlocks.tearLantern);
        drainedCryingObsidian = register("drained_crying_obsidian", ModBlocks.drainedCryingObsidian);
        creativeTearsBlock = register("creative_tears_block", ModBlocks.creativeTearsBlock);

        bottleOfTears = register("bottle_of_tears", new BottleOfTearsItem(SETTINGS));
        diviningRod = register("divining_rod", new DiviningRodItem(TOOLSETTINGS));
        tearIngot = register("tear_ingot", new Item(SETTINGS));

        tearSoakenHelmet = register("tear_soaked_helmet",
                new ArmorItem(CustomArmorMaterials.TEAR_SOAKEN, EquipmentSlot.HEAD, TOOLSETTINGS));
        tearSoakenChestplate = register("tear_soaked_chestplate",
                new ArmorItem(CustomArmorMaterials.TEAR_SOAKEN, EquipmentSlot.CHEST, TOOLSETTINGS));
        tearSoakenLeggings = register("tear_soaked_leggings",
                new ArmorItem(CustomArmorMaterials.TEAR_SOAKEN, EquipmentSlot.LEGS, TOOLSETTINGS));
        tearSoakenBoots = register("tear_soaked_boots",
                new ArmorItem(CustomArmorMaterials.TEAR_SOAKEN, EquipmentSlot.FEET, TOOLSETTINGS));

        baseTarot = register("base_tarot", new TarotCardItem(TAROT_SETTINGS));

        runeStone = register("rune/stone", ModBlocks.runeStone);
        rune1 = register("rune/tier1", ModBlocks.rune1);
        rune2 = register("rune/tier2", ModBlocks.rune2);
        rune3 = register("rune/tier3", ModBlocks.rune3);
        flipRune = register("rune/flip", ModBlocks.flipRune);
        pipeRune1 = register("rune/pipe1", ModBlocks.pipeRune1);
        pipeRune2 = register("rune/pipe2", ModBlocks.pipeRune2);
        healRune = register("rune/healing", ModBlocks.healRune);
        extractionRune = register("rune/extraction", ModBlocks.extractionRune);
        destinationRune = register("rune/destination", ModBlocks.destinationRune);
        transportRune = register("rune/entity_transport", ModBlocks.transportRune);
        blockTransportRune = register("rune/block_transport", ModBlocks.blockTransportRune);
        wardingRune = register("rune/warding", ModBlocks.wardingRune);

    }

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, new Identifier(SoulFlame.MODID, name), item);
    }

    private static BlockItem register(String name, Block block) {
        return register(name, new BlockItem(block, SETTINGS));
    }

}
