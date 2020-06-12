package modfest.soulflame.block.entity;

import modfest.soulflame.init.ModBlockEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

public class CrucibleEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {
	private static final int TEAR_CAPACITY = 1000;

	protected int tears = 0;

	public CrucibleEntity() {
		super(ModBlockEntityTypes.crucible);
	}

	@Environment(EnvType.CLIENT)
	public float getRelativeLevel() {
		return this.tears / (float) TEAR_CAPACITY;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);

		this.tears = tag.getInt("TearLevel");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putInt("TearLevel", this.tears);
		return tag;
	}

	@Override
	public CompoundTag toInitialChunkDataTag() {
		return this.toTag(new CompoundTag());
	}

	@Override
	public void tick() {
		if (this.tears < TEAR_CAPACITY && this.world.getTime() % 10 == 0) {
			for (int dy = 1; dy < 5; dy++) {
				BlockPos obsidianPos = this.pos.up(dy);
				BlockState obsidianState = this.world.getBlockState(obsidianPos);
				if (obsidianState.getBlock() == Blocks.CRYING_OBSIDIAN) {
					this.tears++;
					break;
				}
			}
		}
	}

	public int getLevel() {
		return this.tears;
	}
	
	public void setLevel(int value) {
		this.tears = value;
		if (!this.world.isClient) {
			this.markDirty();
			this.sync();
		}
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
