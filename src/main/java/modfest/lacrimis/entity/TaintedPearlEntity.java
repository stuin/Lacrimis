package modfest.lacrimis.entity;

import modfest.lacrimis.item.ModItems;
import modfest.lacrimis.init.ModStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TaintedPearlEntity extends ThrownItemEntity {
    private static final int DURATION = 15 * 20;

    public TaintedPearlEntity(World world) {
        super(ModEntities.taintedPearl, world);
    }

    protected Item getDefaultItem() {
        return ModItems.taintedPearl;
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if(entityHitResult.getEntity() instanceof SoulShellEntity && getOwner() instanceof ServerPlayerEntity) {
            ((SoulShellEntity) entityHitResult.getEntity()).swapWithPlayer(world, (PlayerEntity) getOwner());
            //((LivingEntity) getOwner()).addStatusEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, DURATION));
            this.remove(RemovalReason.KILLED);
        } else if(entityHitResult.getEntity() instanceof LivingEntity)
            ((LivingEntity) entityHitResult.getEntity()).addStatusEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, DURATION));
        else
            entityHitResult.getEntity().damage(DamageSource.thrownProjectile(this, this.getOwner()), 0.0F);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        Entity entity = this.getOwner();

        for(int i = 0; i < 32; ++i)
            this.world.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());

        if (!this.world.isClient && !this.isRemoved()) {
            if (entity != null) {
                if (entity.hasVehicle())
                    entity.stopRiding();
                
                entity.requestTeleport(this.getX(), this.getY(), this.getZ());
                entity.fallDistance = 0.0F;
                
                if(entity instanceof LivingEntity)
                    ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, DURATION));
            }

            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof PlayerEntity && !entity.isAlive())
            this.remove(RemovalReason.DISCARDED);
        else
            super.tick();
    }

    @Nullable
    public Entity moveToWorld(ServerWorld destination) {
        Entity entity = this.getOwner();
        if (entity != null && entity.world.getRegistryKey() != destination.getRegistryKey())
            this.setOwner(null);

        return super.moveToWorld(destination);
    }
}
