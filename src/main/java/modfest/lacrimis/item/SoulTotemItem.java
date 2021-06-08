package modfest.lacrimis.item;

import modfest.lacrimis.init.ModBlocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulTotemItem extends Item {
    public SoulTotemItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if(world.getBlockState(context.getBlockPos()).isOf(ModBlocks.networkLink)) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("X", context.getBlockPos().getX());
            tag.putInt("Y", context.getBlockPos().getY());
            tag.putInt("Z", context.getBlockPos().getZ());
            context.getStack().setTag(tag);
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        /*if(user.isSneaking()) {
            user.getStackInHand(hand).setTag(null);
            return TypedActionResult.success(user.getStackInHand(hand));
        }*/
        return super.use(world, user, hand);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.hasTag();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if(stack.hasTag())
            tooltip.add(new TranslatableText("lacrimis.tooltip.bound").formatted(Formatting.GRAY));
        else
            tooltip.add(new TranslatableText("lacrimis.tooltip.unbound").formatted(Formatting.GRAY));
    }
}
