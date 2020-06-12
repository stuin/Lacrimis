package modfest.soulflame.block.entity;

import modfest.soulflame.init.ModBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.PropertyDelegate;

public class InfusionTableEntity extends BlockEntity {
	private static final int TEAR_CAPACITY = 500;

	protected int tears = 0;
	public final PropertyDelegate properties;

	public InfusionTableEntity() {
		super(ModBlockEntityTypes.infusionTable);

		this.properties = new PropertyDelegate() {
			@Override
			public int size() {
				return 1;
			}

			public int get(int index) {
				switch (index) {
				case 0:
					return tears;
				default:
					return 0;
				}
			}

			public void set(int index, int value) {
				switch (index) {
				case 0:
					tears = value;
					break;
				}
			}

		};
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

	public int getLevel() {
		return this.tears;
	}

	public void setLevel(int value) {
		this.tears = value;
		if (!this.world.isClient) {
			this.markDirty();
		}
	}
}
