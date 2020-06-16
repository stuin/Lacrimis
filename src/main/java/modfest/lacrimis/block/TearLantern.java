package modfest.lacrimis.block;

import java.util.Random;

import modfest.lacrimis.block.entity.TearLanternEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TearLantern extends LanternBlock implements BlockEntityProvider {
    public TearLantern(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HANGING, false));
    }
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new TearLanternEntity();
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(1) == 0) {
            Direction direction = Direction.random(random);
            if (direction != Direction.UP) {
                BlockPos blockPos = pos.offset(direction);
                BlockState blockState = world.getBlockState(blockPos);
                if (!state.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
                    world.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, (double)pos.getX() + (0.4 + Math.random() * (0.6 - 0.4)), (double)pos.getY() - 0.1D, (double)pos.getZ() + (0.4 + Math.random() * (0.6 - 0.4)), 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}
