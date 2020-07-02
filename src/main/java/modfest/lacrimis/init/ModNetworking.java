package modfest.lacrimis.init;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;

import java.util.stream.Stream;

import io.netty.buffer.Unpooled;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.CombinerEntity;
import modfest.lacrimis.block.entity.InfusionTableEntity;

public class ModNetworking {
    public static final Identifier CRUCIBLE_PARTICLES_ID = new Identifier(Lacrimis.MODID, "crucible_particles");
    public static final Identifier INFUSION_START_ID = new Identifier(Lacrimis.MODID, "infusion_start");
    public static final Identifier COMBINER_NULL_ID = new Identifier(Lacrimis.MODID, "combiner_null");

    public static void register() {
        ServerSidePacketRegistry.INSTANCE.register(INFUSION_START_ID, ModNetworking::handleInfusionStartPacket);
        ServerSidePacketRegistry.INSTANCE.register(COMBINER_NULL_ID, ModNetworking::handleCombinerNullPacket);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ClientSidePacketRegistry.INSTANCE.register(CRUCIBLE_PARTICLES_ID, ModNetworking::handleCrucibleParticlesPacket);
    }

    @Environment(EnvType.CLIENT)
    private static void handleCrucibleParticlesPacket(PacketContext context, PacketByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        context.getTaskQueue().execute(() -> {
            World world = context.getPlayer().world;
            for (int i = 0; i < 10; i++) {
                float angle = (float) (2 * Math.PI * world.random.nextFloat());
                double dx = 0.1 * MathHelper.cos(angle);
                double dz = 0.1 * MathHelper.sin(angle);
                world.addParticle(ParticleTypes.ENCHANTED_HIT, true, x, y, z, dx, 0.05, dz);
            }
        });
    }

    public static void sendCrucibleParticlesPacket(BlockEntity entity, double x, double y, double z) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);

        Stream<PlayerEntity> players = PlayerStream.watching(entity);
        players.forEach(player -> {
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CRUCIBLE_PARTICLES_ID, buf);
        });
    }

    public static void handleInfusionStartPacket(PacketContext context, PacketByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        context.getTaskQueue().execute(() -> {
            BlockEntity entity = context.getPlayer().world.getBlockEntity(pos);
            if(entity instanceof InfusionTableEntity)
                ((InfusionTableEntity) entity).startCrafting = true;
        });
    }

    public static void sendInfusionStartPacket(BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);

        ClientSidePacketRegistry.INSTANCE.sendToServer(INFUSION_START_ID, buf);
    }

    public static void handleCombinerNullPacket(PacketContext context, PacketByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        context.getTaskQueue().execute(() -> {
            BlockEntity entity = context.getPlayer().world.getBlockEntity(pos);
            if(entity instanceof CombinerEntity)
                ((CombinerEntity) entity).type = null;
        });
    }

    public static void sendCombinerNullPacket(BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);

        ClientSidePacketRegistry.INSTANCE.sendToServer(COMBINER_NULL_ID, buf);
    }
}
