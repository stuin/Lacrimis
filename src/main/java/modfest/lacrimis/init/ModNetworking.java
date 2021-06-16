package modfest.lacrimis.init;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import io.netty.buffer.Unpooled;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.CombinerEntity;
import modfest.lacrimis.block.entity.InfusionTableEntity;

public class ModNetworking {
    public static final Identifier INFUSION_START_ID = new Identifier(Lacrimis.MODID, "infusion_start");
    public static final Identifier COMBINER_NULL_ID = new Identifier(Lacrimis.MODID, "combiner_null");
    public static final Identifier CRUCIBLE_PARTICLES_ID = new Identifier(Lacrimis.MODID, "crucible_particles");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(INFUSION_START_ID, ModNetworking::handleInfusionStartPacket);
        ServerPlayNetworking.registerGlobalReceiver(COMBINER_NULL_ID, ModNetworking::handleCombinerNullPacket);
    }

    //Server Infusion Start
    private static void handleInfusionStartPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buffer, PacketSender sender) {
        BlockPos pos = buffer.readBlockPos();

        server.execute(() -> {
            BlockEntity entity = player.world.getBlockEntity(pos);
            if(entity instanceof InfusionTableEntity)
                ((InfusionTableEntity) entity).startCrafting = true;
        });
    }

    //Server Combiner Null
    private static void handleCombinerNullPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buffer, PacketSender sender) {
        BlockPos pos = buffer.readBlockPos();
        server.execute(() -> {
            BlockEntity entity = player.world.getBlockEntity(pos);
            if(entity instanceof CombinerEntity)
                ((CombinerEntity) entity).type = null;
        });
    }

    public static void sendCrucibleParticlesPacket(ServerWorld world, BlockEntity entity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeDouble(entity.getPos().getX());
        buf.writeDouble(entity.getPos().getY());
        buf.writeDouble(entity.getPos().getZ());

        for(ServerPlayerEntity player : PlayerLookup.tracking(world, entity.getPos()))
            ServerPlayNetworking.send(player, CRUCIBLE_PARTICLES_ID, buf);
    }
}
