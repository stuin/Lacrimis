package com.stuintech.lacrimis.init;

import com.stuintech.lacrimis.Lacrimis;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModNetworking {
    public static final Identifier CRUCIBLE_PARTICLES_ID = new Identifier(Lacrimis.MODID, "crucible_particles");
    public static final Identifier TAINT_PARTICLES_ID = new Identifier(Lacrimis.MODID, "taint_particles");

    public static void sendCrucibleParticlesPacket(ServerWorld world, BlockEntity entity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeDouble(entity.getPos().getX());
        buf.writeDouble(entity.getPos().getY());
        buf.writeDouble(entity.getPos().getZ());

        for(ServerPlayerEntity player : PlayerLookup.tracking(world, entity.getPos()))
            ServerPlayNetworking.send(player, CRUCIBLE_PARTICLES_ID, buf);
    }

    public static void sendTaintParticlesPacket(ServerWorld world, BlockPos pos, int density) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeDouble(pos.getX());
        buf.writeDouble(pos.getY());
        buf.writeDouble(pos.getZ());
        buf.writeInt(density);

        for(ServerPlayerEntity player : PlayerLookup.tracking(world, pos))
            ServerPlayNetworking.send(player, TAINT_PARTICLES_ID, buf);
    }
}
