package azoth.common.blocks.entity;

import java.util.Random;

import azoth.common.AzothBlocks;
import azoth.common.AzothParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.util.Tickable;

public class InfusionTableBlockEntity extends AzothContainerBlockEntity implements Tickable, BlockEntityClientSerializable {
    @Environment(EnvType.CLIENT)
    private static final Random RANDOM = new Random();

    private static final int AZOTH_CAPACITY = 250;

    public InfusionTableBlockEntity() {
        super(AzothBlocks.INFUSION_TABLE_ENTITY);
    }

    @Environment(EnvType.CLIENT)
    public boolean shouldDisplayTears() {
        return this.level > 0;
    }

    @Override
    public void tick() {
        if (this.world.isClient) {
            this.tickClient();
            return;
        }
    }

    private void tickClient() {
        if (this.shouldDisplayTears() && RANDOM.nextInt(10) == 0) {
            double a = RANDOM.nextDouble() * 4 * Math.PI;

            double x = this.pos.getX() + 0.5 + 0.1 * Math.cos(a);
            double z = this.pos.getZ() + 0.5 + 0.1 * Math.sin(a);
            double y = this.pos.getY() + 0.75 + 0.05 * RANDOM.nextDouble();

            double dx = 0.005 * Math.cos(a + 1.5 * Math.PI / 2);
            double dz = 0.005 * Math.sin(a + 1.5 * Math.PI / 2);

            this.world.addParticle(AzothParticles.AZOTH_MIST, x, y, z, dx, 0.005, dz);
        }
    }

    public int tryAcceptAzoth(int amount) {
        int accepted = Math.min(amount, AZOTH_CAPACITY - this.level);
        this.setLevel(this.level + accepted);
        return amount - accepted;
    }
}
