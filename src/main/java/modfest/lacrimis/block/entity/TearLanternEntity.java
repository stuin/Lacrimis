package modfest.lacrimis.block.entity;

import modfest.lacrimis.init.ModBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;


public class TearLanternEntity extends BlockEntity implements Tickable {

    public TearLanternEntity() {
        super(ModBlockEntityTypes.tearLantern);
    }


    @Override
    public void tick() {
        int range = 6;
        BlockPos obsidianPos = this.pos.up(1);
        if(world != null) {
            BlockState obsidianState = this.world.getBlockState(obsidianPos);
            if(obsidianState.getBlock() == Blocks.CRYING_OBSIDIAN) {
                for(int x = -range; x <= range; x++)
                    for(int y = -range; y <= range; y++)
                        for(int z = -range; z <= range; z++) {
                            BlockEntity table = world.getBlockEntity(pos.add(x, y - 1, z));
                            if(table instanceof CrucibleEntity) {
                                CrucibleEntity casted = (CrucibleEntity) table;
                                casted.getTank().addTears(2);
                                break;
                            }
                        }
            }
        }
    }
}
