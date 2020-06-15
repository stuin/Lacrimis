package modfest.soulflame.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Random;

import modfest.soulflame.init.ModBlocks;

public class DrainedCryingObsidianBlock extends CryingObsidianBlock {

    public static final IntProperty TEARS_LEAST = IntProperty.of("tears_least_sig", 0, 49);
    public static final IntProperty TEARS_MOST = IntProperty.of("tears_most_sig", 0, 9);

    public DrainedCryingObsidianBlock(Settings settings) {
        super(settings);
        this.setDefaultState(setTearsValue(this.getDefaultState(), 499));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(TEARS_LEAST);
        builder.add(TEARS_MOST);
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.BLOCK;
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

    public static BlockState setTearsValue(BlockState self, int tears) {
        int least = tears % 50;
        int most = tears / 50;
        return self.with(TEARS_LEAST, least).with(TEARS_MOST, most);
    }

    public static int getTearsValue(BlockState self) {
        return self.get(TEARS_MOST) * 50 + self.get(TEARS_LEAST);
    }

    public static BlockState getStateForTearsLevel(int tears) {
        if (tears == 501) {
            return Blocks.CRYING_OBSIDIAN.getDefaultState();
        } else if (tears == 0) {
            return Blocks.OBSIDIAN.getDefaultState();
        } else {
            return setTearsValue(ModBlocks.drainedCryingObsidian.getDefaultState(), tears - 1);
        }
    }

    public static int getTearsLevel(BlockState self) {
        if (self.getBlock() == Blocks.OBSIDIAN) return 0;
        else if (self.getBlock() == Blocks.CRYING_OBSIDIAN) return 501;
        else return getTearsValue(self) + 1;
    }
}
