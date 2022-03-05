package modfest.lacrimis.block.entity;

import modfest.lacrimis.block.DrainedCryingObsidianBlock;
import modfest.lacrimis.entity.ModEntities;
import modfest.lacrimis.init.ModParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class TearLanternEntity extends BlockEntity {

    public TearLanternEntity(BlockPos pos, BlockState state) {
        super(ModEntities.tearLantern, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, TearLanternEntity blockEntity) {
        int range = 6;
        BlockPos obsidianPos = pos.up(1);
        if (world != null && world.getTime() % 5 == 0) {
            //Display dripping particles
            if (world.isClient && Math.random() > 0.9)
                world.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, pos.getX() + (0.4 + Math.random() * (0.6 - 0.4)), pos.getY() - 0.1D, pos.getZ() + (0.4 + Math.random() * (0.6 - 0.4)), 0.0D, 0.0D, 0.0D);

            //Look for tear target
            BlockState obsidianState = world.getBlockState(obsidianPos);
            int remains = DrainedCryingObsidianBlock.getTearsLevel(obsidianState);
            if (remains > 0) {
                for (int x = -range; x <= range; x++)
                    for (int y = -range; y <= range; y++)
                        for (int z = -range; z <= range; z++) {
                            BlockEntity table = world.getBlockEntity(pos.add(x, y - 1, z));
                            if (table instanceof CrucibleEntity casted) {
                                int space = casted.getTank().getSpace();
                                //Display particle stream
                                if(space > 0 && world.isClient) {
                                    double xrand = Math.random() * 0.2 - 0.1;
                                    double yrand = Math.random() * 0.2 - 0.1;
                                    double zrand = Math.random() * 0.2 - 0.1;

                                    world.addParticle(ModParticles.OBSIDIAN_TEAR_FLYING,
                                            pos.getX() + 0.5 + xrand, pos.getY() + 0.2 + yrand, pos.getZ() + 0.5 + zrand,
                                            x * 0.02, y * 0.02, z * 0.02);
                                }

                                //Move tears
                                if(remains >= 3 && casted.getTank().getSpace() >= 4) {
                                    casted.getTank().addTears(4);
                                    world.setBlockState(obsidianPos, DrainedCryingObsidianBlock.removeTearState(obsidianState, 3));
                                    return;
                                } else if(space > 0) {
                                    remains = Math.min(space, remains);
                                    casted.getTank().addTears(remains);
                                    world.setBlockState(obsidianPos, DrainedCryingObsidianBlock.removeTearState(obsidianState, remains));
                                    return;
                                }
                            }
                        }
            }
        }
    }
}
