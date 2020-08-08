package modfest.lacrimis.client.cardgen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.map.MapState;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.TagManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.profiler.DummyProfiler;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.TickScheduler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.DimensionType;

import java.util.List;

public class DummyWorld extends World {

    private final Scoreboard scoreboard = new Scoreboard();
    private final ChunkManager chunkManager = new DummyChunkManager(this);

    protected DummyWorld() {
        super(
                new ClientWorld.Properties(Difficulty.NORMAL, false, false),
                RegistryKey.of(Registry.DIMENSION, new Identifier("dummy")),
                DimensionType.OVERWORLD_REGISTRY_KEY,
                DimensionType.OVERWORLD_REGISTRY_KEY,
                () -> DummyProfiler.INSTANCE,
                true,
                false,
                0L
        );
    }

    @Override
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    @Override
    public ChunkManager getChunkManager() {
        return this.chunkManager;
    }

    @Override
    public void playSound(PlayerEntity player, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
    }

    @Override
    public void updateListeners(BlockPos pos, BlockState oldState, BlockState newState, int flags) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void playSoundFromEntity(PlayerEntity player, Entity entity, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public Entity getEntityById(int id) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public MapState getMapState(String id) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void putMapState(MapState mapState) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public int getNextMapId() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void setBlockBreakingInfo(int entityId, BlockPos pos, int progress) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public RecipeManager getRecipeManager() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public TagManager getTagManager() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public TickScheduler<Block> getBlockTickScheduler() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public TickScheduler<Fluid> getFluidTickScheduler() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void syncWorldEvent(PlayerEntity player, int eventId, BlockPos pos, int data) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public float getBrightness(Direction direction, boolean shaded) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public List<? extends PlayerEntity> getPlayers() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public Biome getGeneratorStoredBiome(int biomeX, int biomeY, int biomeZ) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        throw new IllegalStateException("not implemented");
    }
}
