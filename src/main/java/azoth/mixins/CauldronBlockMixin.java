package azoth.mixins;

import org.spongepowered.asm.mixin.Mixin;

import azoth.Azoth;
import azoth.blocks.AzothConduitBlock.ConnectionType;
import azoth.blocks.ConduitConnectable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin extends Block implements ConduitConnectable {
    private CauldronBlockMixin(Block.Settings settings) {
        super(settings);
        throw new UnsupportedOperationException();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        for (int y = 1; y <= 7; y++) {
            BlockPos above = pos.up(y);
            BlockState state = world.getBlockState(above);
            if (state.isOf(Blocks.CRYING_OBSIDIAN)) {
                return Azoth.Blocks.AZOTH_CAULDRON.getDefaultState();
            } else if (!state.isAir()) {
                break;
            }
        }
        return super.getPlacementState(ctx);
    }

    @Override
    public ConnectionType getConnectionType(BlockState state, Direction side) {
        return side.getAxis().isHorizontal() ? ConnectionType.CAULDRON : ConnectionType.NONE;
    }
}
