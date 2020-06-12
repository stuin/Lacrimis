package modfest.soulflame.block.entity;

import modfest.soulflame.init.ModBlockEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class CrucibleEntity extends LiquidTankEntity {
	public CrucibleEntity() {
		super(ModBlockEntityTypes.crucible, 10);
	}

	@Environment(EnvType.CLIENT)
	public float getRelativeLevel() {
		// TODO return fluid level for rendering
		return 0.5F;
	}
}
