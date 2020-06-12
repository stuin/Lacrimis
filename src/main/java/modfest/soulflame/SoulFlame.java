package modfest.soulflame;

import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import modfest.soulflame.block.ModBlocks;
import modfest.soulflame.infusion.ModInfusion;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class SoulFlame implements ModInitializer {
	public static final String MODID = "soulflame";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final ItemGroup itemGroup = FabricItemGroupBuilder.create(
			new Identifier(MODID, "group"))
			.icon(() -> new ItemStack(ModBlocks.infusionTable))
			.build();

	@Override
	public void onInitialize() {
		ModBlocks.register();
		ModInfusion.register();
	}
}
