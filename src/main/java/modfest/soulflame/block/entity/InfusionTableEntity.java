package modfest.soulflame.block.entity;

import modfest.soulflame.infusion.InfusionInventory;
import modfest.soulflame.init.ModBlockEntityTypes;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.PropertyDelegate;

public class InfusionTableEntity extends SoulTankEntity {
	private static final int TEAR_CAPACITY = 500;

	public final PropertyDelegate properties;
	public final InfusionInventory inventory;

	protected int tears = 0;

	public InfusionTableEntity() {
		super(ModBlockEntityTypes.infusionTable, TEAR_CAPACITY);

		this.properties = new PropertyDelegate() {
			@Override
			public int size() {
				return 1;
			}

			public int get(int index) {
				switch (index) {
				case 0:
					return getLevel();
				default:
					return 0;
				}
			}

			public void set(int index, int value) {
				switch (index) {
				case 0:
					getTank().setTears(value);
					break;
				}
			}
		};
		this.inventory = new InfusionInventory(this);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.inventory.readTags(tag.getList("Inventory", NbtType.COMPOUND));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.put("Inventory", this.inventory.getTags());
		return tag;
	}
}
