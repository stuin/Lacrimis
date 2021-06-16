package modfest.lacrimis.mixin;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.NetworkLinkEntity;
import modfest.lacrimis.entity.TaintedPearlEntity;
import modfest.lacrimis.init.ModEntities;
import net.minecraft.block.entity.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientNetworkMixin implements ClientPlayPacketListener {
    @Shadow
    private MinecraftClient client;
    
    @Inject(at = @At("TAIL"), method = "onEntitySpawn(Lnet/minecraft/network/packet/s2c/play/EntitySpawnS2CPacket;)V")
    public void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo info) {
        Entity entity = null;
        double d = packet.getX();
        double e = packet.getY();
        double f = packet.getZ();
        EntityType<?> entityType = packet.getEntityTypeId();

        if(entityType == ModEntities.taintedPearl)
            entity = new TaintedPearlEntity(ModEntities.taintedPearl, client.world);

        if(entity != null && client.world != null) {
            int i = packet.getId();
            entity.setPos(d, e, f);
            entity.updateTrackedPosition(d, e, f);
            entity.refreshPositionAfterTeleport(d, e, f);
            entity.pitch = (float) (packet.getPitch() * 360) / 256.0F;
            entity.yaw = (float) (packet.getYaw() * 360) / 256.0F;
            entity.setEntityId(i);
            entity.setUuid(packet.getUuid());
            client.world.addEntity(i, entity);
            Lacrimis.LOGGER.warn("Pearl spawned");
        }
    }

    @Inject(at = @At("TAIL"), method = "onBlockEntityUpdate(Lnet/minecraft/network/packet/s2c/play/BlockEntityUpdateS2CPacket;)V")
    public void onBlockEntityUpdate(BlockEntityUpdateS2CPacket packet, CallbackInfo info) {
        BlockPos blockPos = packet.getPos();
        BlockEntity blockEntity = this.client.world.getBlockEntity(blockPos);
        int i = packet.getBlockEntityType();
        if (i == 20 && blockEntity instanceof NetworkLinkEntity) {
            blockEntity.fromTag(this.client.world.getBlockState(blockPos), packet.getCompoundTag());
        }
    }
}
