package azoth.common.blocks;

import azoth.common.AzothBlocks;
import net.minecraft.block.entity.BlockEntity;

public class CrucibleBlockEntity extends BlockEntity {
    public CrucibleBlockEntity() {
        super(AzothBlocks.CRUCIBLE_ENTITY);
    }

    public float getLevelForDisplay() {
        return 0.5F;
    }

}
