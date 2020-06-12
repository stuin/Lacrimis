package modfest.soulflame.block.entity;

import modfest.soulflame.init.ModBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

public class CrucibleEntity extends SoulTankEntity implements Tickable {
	private static final int TEAR_CAPACITY = 1000;

	public CrucibleEntity() {
		super(ModBlockEntityTypes.crucible, TEAR_CAPACITY);
	}

	@Override
	public void tick() {
		if (getRelativeLevel() < 1 && this.world.getTime() % 10 == 0) {
			for (int dy = 1; dy < 5; dy++) {
				BlockPos obsidianPos = this.pos.up(dy);
				BlockState obsidianState = this.world.getBlockState(obsidianPos);
				if (obsidianState.getBlock() == Blocks.CRYING_OBSIDIAN) {
					getTank().addTears(1);
					break;
				}
			}
		}
	}
}
