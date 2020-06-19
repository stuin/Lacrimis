package modfest.lacrimis.block.entity;

import modfest.lacrimis.init.ModBlockEntityTypes;
import modfest.lacrimis.init.ModParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;


public class TearLanternEntity extends BlockEntity implements Tickable {

    public TearLanternEntity() {
        super(ModBlockEntityTypes.tearLantern);
    }


    @Override
    public void tick() {
        int range = 6;
        BlockPos obsidianPos = this.pos.up(1);
        if (world != null) {
            if (world.isClient && Math.random() > 0.9)
                world.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, pos.getX() + (0.4 + Math.random() * (0.6 - 0.4)), pos.getY() - 0.1D, pos.getZ() + (0.4 + Math.random() * (0.6 - 0.4)), 0.0D, 0.0D, 0.0D);
            BlockState obsidianState = this.world.getBlockState(obsidianPos);
            if (obsidianState.getBlock() == Blocks.CRYING_OBSIDIAN) {
                for (int x = -range; x <= range; x++)
                    for (int y = -range; y <= range; y++)
                        for (int z = -range; z <= range; z++) {
                            BlockEntity table = world.getBlockEntity(pos.add(x, y - 1, z));
                            if (table instanceof CrucibleEntity) {
                                CrucibleEntity casted = (CrucibleEntity) table;
                                if (casted.getTank().getSpace() > 0) {
                                    casted.getTank().addTears(2);
                                    if (world.isClient) {
                                        double xrand = Math.random() * 0.2 - 0.1;
                                        double yrand = Math.random() * 0.2 - 0.1;
                                        double zrand = Math.random() * 0.2 - 0.1;

                                        world.addParticle(ModParticles.PURPLE_MIST, this.pos.getX() + 0.5 + xrand, this.pos.getY() + 0.2 + yrand, this.pos.getZ() + 0.5 + zrand, x * 0.02, y * 0.02, z * 0.02);
                                    }
                                    break;
                                }
                            }
                        }
            }
        }
    }
}
