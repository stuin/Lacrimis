package modfest.soulflame.init;

import grondag.fluidity.api.storage.Store;
import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.entity.InfusionTableEntity;
import modfest.soulflame.infusion.InfusionScreen;
import modfest.soulflame.infusion.InfusionScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModInfusion {
	public static final Identifier INFUSION_SCREEN_ID = new Identifier(SoulFlame.MODID, "infusion");

	public static void register() {
		ContainerProviderRegistry.INSTANCE.registerFactory(INFUSION_SCREEN_ID, new ContainerFactory<ScreenHandler>() {
			@Override
			public ScreenHandler create(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
				BlockPos pos = buf.readBlockPos();
				Store tank = null;
				BlockEntity entity = player.getEntityWorld().getBlockEntity(pos);
				if (entity instanceof InfusionTableEntity)
					tank = ((InfusionTableEntity) entity).getEffectiveStorage();
				return new InfusionScreenHandler(syncId, player.inventory, tank, ScreenHandlerContext.create(player.world, pos));
			}
		});
	}

	@Environment(EnvType.CLIENT)
	public static void registerClient() {
		ScreenProviderRegistry.INSTANCE.registerFactory(INFUSION_SCREEN_ID, (ContainerScreenFactory<InfusionScreenHandler>) container -> new InfusionScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText("gui.soulflame.infusion")));
	}
}
