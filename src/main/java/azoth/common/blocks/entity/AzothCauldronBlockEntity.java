package azoth.common.blocks.entity;

import azoth.Azoth;
import azoth.common.AzothBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

public class AzothCauldronBlockEntity extends AzothContainerBlockEntity implements Tickable, BlockEntityClientSerializable {
    private static final int AZOTH_CAPACITY = 1000;

    public AzothCauldronBlockEntity() {
        super(AzothBlocks.CRUCIBLE_ENTITY);
    }

    @Environment(EnvType.CLIENT)
    public float getLevelForDisplay() {
        return this.level / (float) AZOTH_CAPACITY;
    }

    @Override
    public void tick() {
        if (this.level < AZOTH_CAPACITY && this.world.getTime() % 10 == 0) {
            for (int dy = 1; dy < 5; dy++) {
                BlockPos obsidianPos = this.pos.up(dy);
                BlockState obsidianState = this.world.getBlockState(obsidianPos);
                if (obsidianState.getBlock() == Blocks.CRYING_OBSIDIAN) {
                    this.level++;
                    this.markDirty();
                    break;
                }
            }
        }
        if (!this.world.isClient && this.level > 0) {
            if (Azoth.getConduitManager(this.world).offer(this.pos, 1) != 1) {
                this.level -= 1;
                this.markDirty();
                this.sync();
            }
        }
    }
}
