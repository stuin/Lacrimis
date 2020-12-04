package azoth.common;

import azoth.Azoth;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public class AzothItems {
    private static final ItemGroup GROUP = FabricItemGroupBuilder.build(Azoth.createID("main"), () -> new ItemStack(AzothBlocks.INFUSION_TABLE));

    public static void register() {
        Registry.register(Registry.ITEM, Azoth.createID("infusion_table"), new BlockItem(AzothBlocks.INFUSION_TABLE, createSettings()));
        Registry.register(Registry.ITEM, Azoth.createID("conduit"), new BlockItem(AzothBlocks.AZOTH_CONDUIT, createSettings()));
        Registry.register(Registry.ITEM, Azoth.createID("cauldron"), new BlockItem(AzothBlocks.AZOTH_CAULDRON, createSettings().group(null)));
    }

    private static Item.Settings createSettings() {
        return new Item.Settings().group(GROUP);
    }
}
