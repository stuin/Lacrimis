package modfest.soulflame.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

// Based on SuspendParticle
// can't use that because of private constructor and protected fields
@Environment(EnvType.CLIENT)
public class PurpleMistParticle extends SpriteBillboardParticle {
    protected PurpleMistParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        float f = this.random.nextFloat();

        this.colorRed = 0.168F + (0.375F - 0.168F) * f;
        this.colorGreen = 0.004F + (0.02F - 0.004F) * f;
        this.colorBlue = 0.47F + (0.667F - 0.47F) * f;

        this.setBoundingBoxSpacing(0.02F, 0.02F);
        this.scale *= this.random.nextFloat() * 0.6F + 0.5F;
        this.velocityX *= 0.02D;
        this.velocityY *= 0.02D;
        this.velocityZ *= 0.02D;
        this.maxAge = (int) (20.0D / (Math.random() * 0.8D + 0.2D));
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    @Override
    public int getColorMultiplier(float tint) {
        return 15728880;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.maxAge-- <= 0) {
            this.markDead();
        } else {
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= 0.99D;
            this.velocityY *= 0.99D;
            this.velocityZ *= 0.99D;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double dx, double dy, double dz) {
            PurpleMistParticle particle = new PurpleMistParticle(world, x, y, z, dx, dy, dz);
            particle.setSprite(this.spriteProvider);
            particle.velocityX = dx;
            particle.velocityY = dy;
            particle.velocityZ = dz;
            return particle;
        }
    }
}
