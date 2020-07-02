package azoth.common.blocks.entity;

import azoth.Azoth;
import azoth.common.AzothBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;

public class CrucibleBlockEntity extends BlockEntity implements Tickable {
    public CrucibleBlockEntity() {
        super(AzothBlocks.CRUCIBLE_ENTITY);
    }

    @Environment(EnvType.CLIENT)
    public float getLevelForDisplay() {
        return 0.5F;
    }

    @Override
    public void tick() {
        if (!this.world.isClient) {
            Azoth.getConduitManager(this.world).offer(this.pos, 1);
        }
    }
}
