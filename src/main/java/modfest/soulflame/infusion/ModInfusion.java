package modfest.soulflame.infusion;

import grondag.fluidity.api.storage.Store;
import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.entity.InfusionTableEntity;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class ModInfusion {
    public static ScreenHandlerType<InfusionScreenHandler> infusionScreen;
    public static final Identifier INFUSION_SCREEN_ID = new Identifier(SoulFlame.MODID, "infusion");
    
    public static void register() {
        infusionScreen = Registry.register(Registry.SCREEN_HANDLER, SoulFlame.MODID + ":infusion",
                new ScreenHandlerType<>(InfusionScreenHandler::new));

        ContainerProviderRegistry.INSTANCE.registerFactory(INFUSION_SCREEN_ID, new ContainerFactory<ScreenHandler>() {
            @Override
            public ScreenHandler create(int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) {
                BlockPos pos = buf.readBlockPos();
                Store tank = null;
                BlockEntity entity = player.getEntityWorld().getBlockEntity(pos);
                if(entity instanceof InfusionTableEntity)
                    tank = ((InfusionTableEntity) entity).getEffectiveStorage();
                return new InfusionScreenHandler(syncId, player.inventory, tank, ScreenHandlerContext.create(player.world, pos));
            }
        });
    }
}
