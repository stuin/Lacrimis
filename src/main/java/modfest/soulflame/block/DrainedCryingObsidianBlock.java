package modfest.soulflame.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Random;

public class DrainedCryingObsidianBlock extends CryingObsidianBlock {

    public static final IntProperty REMAINING_TEARS = IntProperty.of("tears", 1, 499);

    public DrainedCryingObsidianBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(REMAINING_TEARS, 499));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(REMAINING_TEARS);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(5) == 0) {
            Direction direction = Direction.random(random);
            if (direction != Direction.UP) {
                BlockPos blockPos = pos.offset(direction);
                BlockState blockState = world.getBlockState(blockPos);
                if (!state.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
                    double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5 + direction.getOffsetX() * 0.6;
                    double e = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5 + direction.getOffsetY() * 0.6;
                    double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5 + direction.getOffsetZ() * 0.6;
                    world.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, pos.getX() + d, pos.getY() + e, pos.getZ() + f, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

}
