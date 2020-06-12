package modfest.soulflame;

import modfest.soulflame.block.ModBlocks;
import modfest.soulflame.infusion.RequiredTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoulFlame implements ModInitializer {
	public static final String MODID = "soulflame";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	public static final ItemGroup itemGroup = FabricItemGroupBuilder.create(
			new Identifier(MODID, "group"))
			.build();
	
	@Override
	public void onInitialize() {
		ModBlocks.register();
		RequiredTypes.register();
	}
}
