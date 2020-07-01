package azoth.common.blocks.entity;

import azoth.common.AzothBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;

public class CrucibleBlockEntity extends BlockEntity {
    public CrucibleBlockEntity() {
        super(AzothBlocks.CRUCIBLE_ENTITY);
    }

    @Environment(EnvType.CLIENT)
    public float getLevelForDisplay() {
        return 0.5F;
    }
}
