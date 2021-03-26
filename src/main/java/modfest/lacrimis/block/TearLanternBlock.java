package modfest.lacrimis.block;

import modfest.lacrimis.block.entity.TearLanternEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class TearLanternBlock extends LanternBlock implements BlockEntityProvider {
    public TearLanternBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HANGING, false));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new TearLanternEntity();
    }

}
