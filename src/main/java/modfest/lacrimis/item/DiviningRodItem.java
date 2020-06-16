package modfest.lacrimis.item;

import modfest.lacrimis.block.Activatable;
import modfest.lacrimis.block.SoulTankBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
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
        PlayerEntity player = context.getPlayer();

        //Read tears
        if(block instanceof SoulTankBlock && player != null && !player.isSneaking()) {
            if(!world.isClient) {
                int level = ((SoulTankBlock) block).getTank(world, pos).getTears();
                Text text = new LiteralText(level + " Tears");
                player.sendMessage(text, false);
            }
            return ActionResult.SUCCESS;
        }

        //Activate block
        if(block instanceof Activatable && ((Activatable) block).activate(world, pos, player))
            return ActionResult.SUCCESS;
        return super.useOnBlock(context);
    }
}
