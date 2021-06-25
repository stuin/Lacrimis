package modfest.lacrimis;

import modfest.lacrimis.init.ModBlocks;
import modfest.lacrimis.init.*;
import modfest.lacrimis.init.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Lacrimis implements ModInitializer {
    public static final String MODID = "lacrimis";
    public static final Logger LOGGER = LogManager.getLogger("Lacrimis");

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
            new Identifier(MODID, "base"))
            .icon(() -> new ItemStack(ModBlocks.infusionTable))
            .build();
    public static final ItemGroup RUNE_ITEM_GROUP = FabricItemGroupBuilder.create(
            new Identifier(MODID, "runes"))
            .icon(() -> new ItemStack(ModBlocks.runeStone))
            .build();

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModEntities.register();
        ModItems.register();
        ModCrafting.register();
        ModSounds.register();
        ModParticles.register();
        ModStatusEffects.register();
        ModEnchantments.register();
    }
}
