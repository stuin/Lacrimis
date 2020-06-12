package modfest.soulflame.block.entity;

import modfest.soulflame.infusion.InfusionInventory;
import modfest.soulflame.init.ModBlockEntityTypes;
import modfest.soulflame.util.ConduitUtil;
import modfest.soulflame.util.ConduitUtil.Entry;

import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.Tickable;
import net.minecraft.world.World;

import java.util.List;

import static java.lang.Math.min;

public class InfusionTableEntity extends SoulTankEntity implements Tickable {
	private static final int TEAR_CAPACITY = 500;
	
	public final PropertyDelegate properties;
	public final InfusionInventory inventory;

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
		this.inventory = new InfusionInventory(null, this.properties);
	}

	@Override
	public void tick() {
		World world = this.getWorld();
		if (world == null || world.isClient()) return;

		int level = this.getLevel();
		int free = TEAR_CAPACITY - level;
		List<Entry> entries = ConduitUtil.scanConduits(world, this.getPos());
		int available = ConduitUtil.totalAmount(entries, world);
		int toPull = min(20, min(available, free));
		int pulled = ConduitUtil.pull(entries, world, toPull, false);
		this.addTears(pulled);
	}

}
