package modfest.lacrimis;

import modfest.lacrimis.block.ModBlocks;
import modfest.lacrimis.crafting.ModCrafting;
import modfest.lacrimis.entity.ModEntities;
import modfest.lacrimis.init.*;
import modfest.lacrimis.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
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
            .icon(() -> new ItemStack(ModBlocks.rune1))
            .build();

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModEntities.register();
        ModItems.register();
        ModCrafting.register();
        ModGameRules.register();
        ModStatusEffects.register();
        ModEnchantments.register();
        ModSounds.register();
        ModParticles.register();
    }
}
