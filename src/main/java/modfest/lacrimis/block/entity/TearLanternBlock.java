package modfest.lacrimis.block.entity;

import modfest.lacrimis.entity.ModEntities;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TearLanternBlock extends LanternBlock implements BlockEntityProvider {
    public TearLanternBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HANGING, false));
    }


    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TearLanternEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return ModEntities.checkType(type, ModEntities.tearLantern, TearLanternEntity::tick);
    }
}
