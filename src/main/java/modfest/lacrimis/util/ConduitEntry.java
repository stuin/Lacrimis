package modfest.lacrimis.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

import java.util.Objects;

import modfest.lacrimis.block.BlockConduitConnect;

public final class ConduitEntry {
    public final BlockConduitConnect b;
    public final BlockPos pos;
    private BlockState state;

    public ConduitEntry(BlockConduitConnect b, BlockPos pos, BlockState state) {
        this.b = b;
        this.pos = pos;
        this.state = state;
    }

    public Object extract(BlockView world) {
        Object r = this.b.extract(this.pos, world);
        if (r != null && world instanceof WorldView)
            this.updateState((WorldView) world);
        return r;
    }

    public int extractTears(BlockView world, int request, boolean simulate) {
        int found = this.b.extractTears(this.pos, world, request, simulate);
        if(world instanceof WorldView)
            this.updateState((WorldView) world);
        return found;
    }

    public boolean insert(BlockView world, Object value) {
        return this.b.insert(this.pos, world, value);
    }

    public void updateState(WorldView world) {
        this.state = world.getBlockState(this.pos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ConduitEntry entry = (ConduitEntry) o;
        return Objects.equals(this.b, entry.b) &&
                Objects.equals(this.pos, entry.pos) &&
                Objects.equals(this.state, entry.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.b, this.pos, this.state);
    }

    @Override
    public String toString() {
        return String.format("ConduitEntry { b: %s, pos: %s, state: %s }", this.b, this.pos, this.state);
    }
}