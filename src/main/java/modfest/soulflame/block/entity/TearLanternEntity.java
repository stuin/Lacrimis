package modfest.soulflame.block.entity;

import modfest.soulflame.init.ModBlockEntityTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TearLanternEntity extends BlockEntity implements Tickable {
    private BlockPos pos;
    private World world;
    private BlockState state;

    public TearLanternEntity() {
        super(ModBlockEntityTypes.tearLantern);
    }


    @Override
    public void tick() {
        int range = 6;
        System.out.println(pos);
        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++) {
                    BlockEntity table = world.getBlockEntity(pos.add(x, y-1, z));
                    if (table instanceof CrucibleEntity) {
                        CrucibleEntity casted = (CrucibleEntity) table;
                        casted.addTears(5);
                        System.out.println("Added 5 to " + pos);
                        break;
                    }
                }
    }
}
