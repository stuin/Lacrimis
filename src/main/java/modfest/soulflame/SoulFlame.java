package modfest.soulflame;

import modfest.soulflame.init.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import modfest.soulflame.entity.GhostEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoulFlame implements ModInitializer {
    public static final String MODID = "soulflame";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
            new Identifier(MODID, "group"))
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
        ModSounds.register();
        ModParticles.register();

        FabricDefaultAttributeRegistry.register(ModEntityTypes.ghost, GhostEntity.createGhostAttributes());
    }
}
