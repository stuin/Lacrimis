package modfest.lacrimis.client.cardgen;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.light.LightingProvider;

public class DummyChunkManager extends ChunkManager {

    private final World world;

    public DummyChunkManager(World world) {
        this.world = world;
    }

    @Override
    public Chunk getChunk(int x, int z, ChunkStatus leastStatus, boolean create) {
        return new EmptyChunk(this.world, new ChunkPos(x, z));
    }

    @Override
    public String getDebugString() {
        return "";
    }

    @Override
    public LightingProvider getLightingProvider() {
        throw new IllegalStateException("not implemented"); // TODO
    }

    @Override
    public BlockView getWorld() {
        return this.world;
    }

}
