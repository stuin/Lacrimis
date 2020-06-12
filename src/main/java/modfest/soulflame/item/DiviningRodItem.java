package modfest.soulflame.item;

import modfest.soulflame.block.Activatable;
import modfest.soulflame.block.SoulTankBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
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
        boolean done = false;

        if(block instanceof Activatable && ((Activatable) block).activate(world, pos))
            done = true;

        if(block instanceof SoulTankBlock && world.isClient && context.getPlayer() != null) {
            int level = ((SoulTankBlock)block).getTank(world, pos).getTears();
            Text text = new LiteralText(level + " Tears");
            context.getPlayer().sendMessage(text, false);
            done = true;
        }
        
        if(done)
            return ActionResult.SUCCESS;
        return super.useOnBlock(context);
    }
}
