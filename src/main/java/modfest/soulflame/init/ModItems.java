package modfest.soulflame.init;

import modfest.soulflame.item.DiviningRodItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.item.BottleOfTearsItem;

public class ModItems {
    private static final Item.Settings SETTINGS = new Item.Settings().group(SoulFlame.ITEM_GROUP);

    //Main blocks
    public static BlockItem infusionTable;
    public static BlockItem crucible;
    public static BlockItem conduit;
    public static BlockItem tearLantern;
    public static BlockItem creativeTearsBlock;

    //Rune cage blocks
    public static BlockItem rune1;
    public static BlockItem rune2;
    public static BlockItem pipeRune;
    public static BlockItem healRune;
    public static BlockItem extractionRune;
    public static BlockItem destinationRune;

    //Main items
    public static BottleOfTearsItem bottleOfTears;
    public static DiviningRodItem diviningRod;

    public static void register() {
        infusionTable = register("infusion_table", ModBlocks.infusionTable);
        crucible = register("crucible", ModBlocks.crucible);
        conduit = register("conduit", ModBlocks.conduit);
        tearLantern = register("tear_lantern", ModBlocks.tearLantern);
        creativeTearsBlock = register("creative_tears_block", ModBlocks.creativeTearsBlock);
        
        rune1 = register("rune/tier1", ModBlocks.rune1);
        rune2 = register("rune/tier2", ModBlocks.rune2);
        pipeRune = register("rune/pipe1", ModBlocks.pipeRune);
        healRune = register("rune/heal", ModBlocks.healRune);
        extractionRune = register("rune/extraction", ModBlocks.extractionRune);
        destinationRune = register("rune/destination", ModBlocks.destinationRune);
        
        bottleOfTears = register("bottle_of_tears", new BottleOfTearsItem(SETTINGS));
        diviningRod = register("divining_rod", new DiviningRodItem(SETTINGS));
    }

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, new Identifier(SoulFlame.MODID, name), item);
    }

    private static BlockItem register(String name, Block block) {
        return register(name, new BlockItem(block, SETTINGS));
    }

}
