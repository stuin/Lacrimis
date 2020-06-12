package modfest.soulflame;

import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import modfest.soulflame.block.ModBlocks;
import modfest.soulflame.infusion.InfusionScreenHandler;
import modfest.soulflame.infusion.RequiredTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class SoulFlame implements ModInitializer {
	public static final String MODID = "soulflame";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final ItemGroup itemGroup = FabricItemGroupBuilder.create(
			new Identifier(MODID, "group"))
			.icon(() -> new ItemStack(ModBlocks.infusionTable))
			.build();

	public static final Identifier INFUSION_SCREEN_ID = new Identifier(MODID, "infusion");

	@Override
	public void onInitialize() {
		ModBlocks.register();
		RequiredTypes.register();

		ContainerProviderRegistry.INSTANCE.registerFactory(INFUSION_SCREEN_ID, new ContainerFactory<ScreenHandler>() {
			@Override
			public ScreenHandler create(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
				BlockPos pos = buf.readBlockPos();
				return new InfusionScreenHandler(syncId, player.inventory, ScreenHandlerContext.create(player.world, pos));
			}
		});
	}
}
