package modfest.soulflame.block;

import modfest.soulflame.block.entity.InfusionTableEntity;
import modfest.soulflame.block.entity.TearLanternEntity;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class TearLantern extends BlockWithEntity {
    public TearLantern(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new TearLanternEntity();
    }
}
