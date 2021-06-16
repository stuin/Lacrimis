package modfest.lacrimis.client.init;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import static modfest.lacrimis.init.ModNetworking.*;

public class ClientModNetworking {

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(CRUCIBLE_PARTICLES_ID, ClientModNetworking::handleCrucibleParticlesPacket);
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

    public static void sendCombinerNullPacket(BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        ClientPlayNetworking.send(COMBINER_NULL_ID, buf);
    }

    public static void sendInfusionStartPacket(BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        ClientPlayNetworking.send(INFUSION_START_ID, buf);
    }
}
