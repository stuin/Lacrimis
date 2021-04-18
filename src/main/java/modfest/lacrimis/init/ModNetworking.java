package modfest.lacrimis.init;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

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

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(CRUCIBLE_PARTICLES_ID, ModNetworking::handleCrucibleParticlesPacket);
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

    public static void sendInfusionStartPacket(BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        ClientPlayNetworking.send(INFUSION_START_ID, buf);
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

    public static void sendCombinerNullPacket(BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        ClientPlayNetworking.send(COMBINER_NULL_ID, buf);
    }

    //Client Crucible Particles
    private static void handleCrucibleParticlesPacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buffer, PacketSender sender) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        
        client.execute(() -> {
            World world = client.world;
            if(client.world != null) {
                for(int i = 0; i < 10; i++) {
                    float angle = (float) (2 * Math.PI * world.random.nextFloat());
                    double dx = 0.1 * MathHelper.cos(angle);
                    double dz = 0.1 * MathHelper.sin(angle);
                    world.addParticle(ParticleTypes.ENCHANTED_HIT, true, x, y, z, dx, 0.05, dz);
                }
            }
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
