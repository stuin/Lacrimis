package com.stuintech.lacrimis.block;

import com.stuintech.lacrimis.util.TaintPacket;
import com.stuintech.lacrimis.init.ModGameRules;
import com.stuintech.lacrimis.init.ModStatusEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class TaintedBlock extends Block {
    public TaintedBlock(Settings settings) {
        super(settings);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(random.nextInt(4) == 0 && world.getGameRules().getBoolean(ModGameRules.TAINT_SPREAD)) {
            Direction dir = Direction.random(random);
            int i = (int)(state.getHardness(world, pos) + 1) * (random.nextInt(3) + 1);
            if(TaintPacket.convert(world, pos.offset(dir), i, false) == 0 && random.nextInt(4) == 0)
                TaintPacket.setLayers(world, pos, i);
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if(entity instanceof LivingEntity)
            ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, 200));
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, 300, 2));
    }
}
