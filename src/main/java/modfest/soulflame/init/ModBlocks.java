package modfest.soulflame.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.ChannelBlock;
import modfest.soulflame.block.CrucibleBlock;
import modfest.soulflame.block.InfusionTableBlock;

public class ModBlocks {

    public static InfusionTableBlock infusionTableBlock;
    public static CrucibleBlock crucibleBlock;
    public static ChannelBlock channel;

    public static void register() {
        infusionTableBlock = register("infusion_table", new InfusionTableBlock(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).nonOpaque()));
        crucibleBlock = register("crucibleBlock", new CrucibleBlock(FabricBlockSettings.copy(Blocks.CAULDRON).nonOpaque()));
        channel = register("channel", new ChannelBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.25f)));
    }

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(Registry.BLOCK, new Identifier(SoulFlame.MODID, name), block);
    }

}
