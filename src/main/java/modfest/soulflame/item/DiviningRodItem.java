package modfest.soulflame.item;

import modfest.soulflame.block.Activatable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class DiviningRodItem extends Item {
    public DiviningRodItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        Block block = world.getBlockState(pos).getBlock();

        if(block instanceof Activatable && ((Activatable) block).activate(world, pos))
            return ActionResult.SUCCESS;
        return super.useOnBlock(context);
    }
}
