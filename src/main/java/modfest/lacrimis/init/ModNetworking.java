package modfest.lacrimis.init;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import io.netty.buffer.Unpooled;

import modfest.lacrimis.Lacrimis;

public class ModNetworking {
    public static final Identifier CRUCIBLE_PARTICLES_ID = new Identifier(Lacrimis.MODID, "crucible_particles");

    public static void sendCrucibleParticlesPacket(ServerWorld world, BlockEntity entity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeDouble(entity.getPos().getX());
        buf.writeDouble(entity.getPos().getY());
        buf.writeDouble(entity.getPos().getZ());

        for(ServerPlayerEntity player : PlayerLookup.tracking(world, entity.getPos()))
            ServerPlayNetworking.send(player, CRUCIBLE_PARTICLES_ID, buf);
    }
}
