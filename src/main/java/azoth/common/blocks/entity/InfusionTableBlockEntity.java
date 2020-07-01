package azoth.common.blocks.entity;

import azoth.common.AzothBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;

public class InfusionTableBlockEntity extends BlockEntity {
    public InfusionTableBlockEntity() {
        super(AzothBlocks.INFUSION_TABLE_ENTITY);
    }

    @Environment(EnvType.CLIENT)
    public boolean shouldDisplayTears() {
        return true;
    }
}
