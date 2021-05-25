package modfest.lacrimis.block;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.init.ModBlocks;
import modfest.lacrimis.init.ModStatusEffects;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TaintBlock extends FallingBlock {
    public static final IntProperty LAYERS;
    protected static final VoxelShape[] LAYERS_TO_SHAPE;

    public TaintBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LAYERS, 1));
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return LAYERS_TO_SHAPE[state.get(LAYERS)];
    }

    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return LAYERS_TO_SHAPE[state.get(LAYERS) - 1];
    }

    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return LAYERS_TO_SHAPE[state.get(LAYERS)];
    }

    public VoxelShape getVisualShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return LAYERS_TO_SHAPE[state.get(LAYERS)];
    }

    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int layers = state.get(LAYERS);
        if(random.nextInt(2) == 0 && !convert(world, pos, pos.down(), layers)) {
            Direction dir = Direction.fromHorizontal(random.nextInt(4));
            if(!convert(world, pos, pos.offset(dir), layers))
                world.setBlockState(pos, getDefaultState().with(LAYERS, Math.min(layers + random.nextInt(3) + 1, 8)));
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        if(world.getBlockState(pos.down()).isOf(this)) {
            BlockState dest = world.getBlockState(pos.down());
            world.setBlockState(pos.down(), dest.with(LAYERS, Math.min(dest.get(LAYERS) + state.get(LAYERS), 8)));
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    private boolean convert(World world, BlockPos source, BlockPos dest, int layers) {
        BlockState state = world.getBlockState(dest);
        int strength = (int)state.getHardness(world, dest);
        if(strength != -1 && strength < layers) {
            world.setBlockState(source, getDefaultState().with(LAYERS, Math.min(layers - strength, 8)));
            if(state.isIn(ModBlocks.tainted))
                return false;
            else if(state.getMaterial() == Material.SOIL)
                world.setBlockState(dest, ModBlocks.taintedDirt.getDefaultState());
            else if(state.getMaterial() == Material.STONE || state.getMaterial() == Material.PISTON || state.getMaterial() == Material.METAL)
                world.setBlockState(dest, ModBlocks.taintedStone.getDefaultState());
            else
                world.setBlockState(dest, ModBlocks.taint.getDefaultState().with(LAYERS, Math.min(strength + 1, 8)));
            return true;
        }
        return false;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, Entity entity) {
        if(entity instanceof LivingEntity)
            ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, 200));
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, 300, 2));
    }

    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        int i = state.get(LAYERS);
        if (context.getStack().getItem() == this.asItem() && i < 8) {
            if (context.canReplaceExisting()) {
                return context.getSide() == Direction.UP;
            } else {
                return true;
            }
        } else {
            return i == 1;
        }
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) {
            int i = blockState.get(LAYERS);
            return blockState.with(LAYERS, Math.min(8, i + 1));
        } else {
            return super.getPlacementState(ctx);
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    static {
        LAYERS = Properties.LAYERS;
        LAYERS_TO_SHAPE = new VoxelShape[]{VoxelShapes.empty(), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};
    }
}
