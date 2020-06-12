package modfest.soulflame.block;

import modfest.soulflame.block.entity.CrucibleEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class Crucible extends LiquidTank {
    public Crucible(Settings settings) {
        super(settings);
    }
    
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new CrucibleEntity();
    }
}
