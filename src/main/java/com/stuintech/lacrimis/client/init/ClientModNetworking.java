package com.stuintech.lacrimis.client.init;

import com.stuintech.lacrimis.Lacrimis;
import com.stuintech.lacrimis.init.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

import static com.stuintech.lacrimis.init.ModNetworking.CRUCIBLE_PARTICLES_ID;
import static com.stuintech.lacrimis.init.ModNetworking.TAINT_PARTICLES_ID;

public class ClientModNetworking {

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(CRUCIBLE_PARTICLES_ID, ClientModNetworking::handleCrucibleParticlesPacket);
        ClientPlayNetworking.registerGlobalReceiver(TAINT_PARTICLES_ID, ClientModNetworking::handleTaintParticlesPacket);
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

    private static void handleTaintParticlesPacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buffer, PacketSender sender) {
        double bx = buffer.readDouble();
        double by = buffer.readDouble();
        double bz = buffer.readDouble();
        int density = buffer.readInt();

        client.execute(() -> {
            World world = client.world;
            Random random = world.getRandom();
            if(client.world != null && random != null) {
                for(int i = 0; i < density * 20; i++) {
                    double a = random.nextDouble() * 4 * Math.PI;

                    double x = bx + 0.5 + Math.cos(a) * random.nextDouble();
                    double z = bz + 0.5 + Math.sin(a) * random.nextDouble();
                    double y = by + 0.02 + 0.0625 * density + 0.2 * random.nextDouble();

                    double dx = 0.04 * Math.cos(a);
                    double dz = 0.04 * Math.sin(a);

                    world.addParticle(ModParticles.PURPLE_MIST, x, y, z, dx, 0.01, dz);
                }
            }
        });
    }
}
