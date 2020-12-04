package azoth.mixins;

import org.spongepowered.asm.mixin.Mixin;

import azoth.Azoth;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(CryingObsidianBlock.class)
public abstract class CryingObsidianBlockMixin extends Block {
    private CryingObsidianBlockMixin(Block.Settings settings) {
        super(settings);
        throw new UnsupportedOperationException();
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        for (int y = 1; y <= 7; y++) {
            BlockPos below = pos.down(y);
            BlockState state2 = world.getBlockState(below);
            if (state2.isOf(Blocks.CAULDRON) && state2.get(CauldronBlock.LEVEL) == 0) {
                world.setBlockState(below, Azoth.Blocks.AZOTH_CAULDRON.getDefaultState());
            } else if (!state2.isAir()) {
                break;
            }
        }
    }

}
