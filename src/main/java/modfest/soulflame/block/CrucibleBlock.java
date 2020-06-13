package modfest.soulflame.block;

import modfest.soulflame.init.ModBlocks;
import modfest.soulflame.init.ModItems;
import modfest.soulflame.util.SoulTank;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import modfest.soulflame.block.entity.CrucibleEntity;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class CrucibleBlock extends SoulTankBlock implements Activatable {
    private static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanBiFunction.ONLY_FIRST);

    public CrucibleBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack item) {
        SoulTank tank = getTank(world, pos);
        if(tank != null)
            tank.setTears(item.getOrCreateTag().getInt("TearLevel"));
    }

    @Override
    public boolean activate(World world, BlockPos pos, PlayerEntity player) {
        SoulTank tank = getTank(world, pos);
        if(tank != null) {
            ItemStack item = new ItemStack(ModItems.crucible);
            item.getOrCreateTag().putInt("TearLevel", tank.getTears());
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            player.giveItemStack(item);
            return true;
        }
        return false;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new CrucibleEntity();
    }

    @Override
    public void buildTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
        super.buildTooltip(stack, world, tooltip, options);
        tooltip.add(new LiteralText(stack.getOrCreateTag().getInt("TearLevel") + " Tears"));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public VoxelShape getRayTraceShape(BlockState state, BlockView world, BlockPos pos) {
        return RAY_TRACE_SHAPE;
    }
}
