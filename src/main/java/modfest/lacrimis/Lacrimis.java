package modfest.lacrimis;

import modfest.lacrimis.init.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import modfest.lacrimis.entity.GhostEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Lacrimis implements ModInitializer {
    public static final String MODID = "lacrimis";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
            new Identifier(MODID, "base"))
            .icon(() -> new ItemStack(ModBlocks.infusionTable))
            .build();
    public static final ItemGroup RUNE_ITEM_GROUP = FabricItemGroupBuilder.create(
            new Identifier(MODID, "runes"))
            .icon(() -> new ItemStack(ModBlocks.runeStone))
            .build();
    public static final ItemGroup TAROT_ITEM_GROUP = FabricItemGroupBuilder.create(
            new Identifier(MODID, "tarot"))
            .icon(() -> new ItemStack(ModItems.baseTarot))
            .build();

    @Override
    public void onInitialize() {
        ModEntityTypes.register();
        ModBlocks.register();
        ModBlockEntityTypes.register();
        ModItems.register();
        ModInfusion.register();
        ModNetworking.register();
        ModSounds.register();
        ModParticles.register();
        ModStatusEffects.register();

        FabricDefaultAttributeRegistry.register(ModEntityTypes.ghost, GhostEntity.createGhostAttributes());
    }
}
