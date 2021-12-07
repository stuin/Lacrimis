package modfest.lacrimis.item;

import modfest.lacrimis.block.rune.activatable;
import modfest.lacrimis.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;


public class DiviningRodItem extends Item {
    public DiviningRodItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
        if(block instanceof activatable)
            ((activatable)block).activate(context.getWorld(), context.getBlockPos(), context.getPlayer());
        return super.useOnBlock(context);
    }
}
