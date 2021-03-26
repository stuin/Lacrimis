package modfest.lacrimis.block.rune;

import modfest.lacrimis.init.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpawnerBreakerBlock extends CenterRuneBlock {
    public SpawnerBreakerBlock() {
        super(600, 3);
    }

    @Override
    protected boolean activate(World world, BlockPos pos, BlockPos duct, Entity entity, PlayerEntity player) {
        if(world.getBlockState(pos).getBlock() instanceof SpawnerBlock) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            ItemScatterer.spawn(world, pos, new SimpleInventory(new ItemStack(ModItems.brokenSpawner)));
            return true;
        }
        return false;
    }
}
