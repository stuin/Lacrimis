package com.stuintech.lacrimis.block.entity;

import com.stuintech.lacrimis.Lacrimis;
import com.stuintech.lacrimis.init.ModStatusEffects;
import com.stuintech.lacrimis.util.SoulTank;
import com.stuintech.lacrimis.entity.ModEntities;
import com.stuintech.lacrimis.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class CrucibleBlock extends SoulTankBlock {
    private static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(3.0D, 4.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), RAY_TRACE_SHAPE, BooleanBiFunction.ONLY_FIRST);
    private static final Box inside = new Box(0.2, 0.2, 0.2, 0.8, 0.8, 0.8);

    public CrucibleBlock(AbstractBlock.Settings settings) {
        super(settings, true);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrucibleEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return ModEntities.checkType(type, ModEntities.crucible, CrucibleEntity::tick);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack item) {
        SoulTank tank = getTank(world, pos);
        if (tank != null)
            tank.setTears(item.getOrCreateNbt().getInt("TearLevel"));
    }

    @Override
    public boolean onFasten(PlayerEntity player, World world, BlockPos pos, Vec3d vec3d, Direction direction) {
        if(player.isSneaking()) {
            SoulTank tank = getTank(world, pos);
            if(tank != null) {
                //Make item
                ItemStack item = new ItemStack(ModItems.crucible);
                if(tank.getTears() > 0) {
                    item.getOrCreateNbt().putInt("TearLevel", tank.getTears());
                    tank.setTears(0);
                }

                ItemScatterer.spawn(world, pos, new SimpleInventory(item));
                onBreak(world, pos, world.getBlockState(pos), player);
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                return true;
            }
        }
        return super.onFasten(player, world, pos, vec3d, direction);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if(entity instanceof LivingEntity && inside.offset(pos).intersects(entity.getBoundingBox())) {
            int amount = getTank(world, pos).getTears();
            if(amount > 0)
                ((LivingEntity) entity).setStatusEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, amount, 2), null);
        }

    }

    @Override
    public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        tooltip.add(new TranslatableText(Lacrimis.MODID + ".tooltip.crucible",
                stack.getOrCreateNbt().getInt("TearLevel")).formatted(Formatting.GRAY));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }
}
