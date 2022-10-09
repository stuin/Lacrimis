package com.stuintech.lacrimis.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EndSpikeFeature.class)
public abstract class EndSpikeMixin extends Feature<EndSpikeFeatureConfig> {
    public EndSpikeMixin(Codec<EndSpikeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Inject(at = @At("TAIL"), method = "generateSpike(Lnet/minecraft/world/ServerWorldAccess;Ljava/util/Random;Lnet/minecraft/world/gen/feature/EndSpikeFeatureConfig;Lnet/minecraft/world/gen/feature/EndSpikeFeature$Spike;)V")
    private void generateSpike(ServerWorldAccess world, Random random, EndSpikeFeatureConfig config, EndSpikeFeature.Spike spike, CallbackInfo ci) {
        int i = spike.getRadius();
        for(BlockPos blockPos : BlockPos.iterate(new BlockPos(spike.getCenterX() - i, 0, spike.getCenterZ() - i), new BlockPos(spike.getCenterX() + i, spike.getHeight() + 10, spike.getCenterZ() + i))) {
            if(world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN && random.nextInt(20) == 1)
                setBlockState(world, blockPos, Blocks.CRYING_OBSIDIAN.getDefaultState());
        }
    }
}
