package modfest.lacrimis.block.entity;

import modfest.lacrimis.util.SoulTank;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

public abstract class SoulTankEntity extends BlockEntity implements BlockEntityClientSerializable {
    private final SoulTank tank;

    public SoulTankEntity(BlockEntityType<?> type, int capacity) {
        super(type);
        tank = new SoulTank(capacity);
        tank.addListener(this::mark);
    }

    public float getRelativeLevel() {
        return tank.getTears() / (float) tank.getCapacity();
    }
    
    public SoulTank getTank() {
        return tank;
    }
    
    public void mark() {
        if (this.world != null) {
            this.markDirty();
            if(!this.world.isClient)
                this.sync();
        }
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        tank.setTears(tag.getInt("TearLevel"));
        tank.setLimit(tag.getInt("TearLimit"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("TearLevel", tank.getTears());
        tag.putInt("TearLimit", tank.getCapacity());
        return tag;
    }

    @Override
    public CompoundTag toInitialChunkDataTag() {
        return this.toTag(new CompoundTag());
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        this.fromTag(getCachedState(), tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return this.toTag(tag);
    }
}
