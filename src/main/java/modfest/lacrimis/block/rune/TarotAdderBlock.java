package modfest.lacrimis.block.rune;

import modfest.lacrimis.block.entity.TarotAdderEntity;
import modfest.lacrimis.init.ModItems;
import modfest.lacrimis.item.TarotCardItem;
import modfest.lacrimis.tarot.CardHolder;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TarotAdderBlock extends CenterRuneBlock implements BlockEntityProvider {

    public TarotAdderBlock() {
        super(500, 3);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack item = player.getStackInHand(hand);
        TarotAdderEntity adder = (TarotAdderEntity)world.getBlockEntity(pos);
        if(adder != null) {
            if(adder.card == null && !adder.hasBlank) {
                if(item.getItem() instanceof TarotCardItem) {
                    adder.card = ((TarotCardItem) item.getItem()).getType();
                    item.decrement(1);
                    return ActionResult.SUCCESS;
                } else if(item.getItem() == ModItems.baseTarot) {
                    adder.hasBlank = true;
                    item.decrement(1);
                    return ActionResult.SUCCESS;
                }
            } else if(item.isEmpty()) {
                if(adder.card != null) {
                    player.giveItemStack(new ItemStack(ModItems.tarotCards.get(adder.card)));
                    adder.card = null;
                } else {
                    player.giveItemStack(new ItemStack(ModItems.baseTarot));
                    adder.hasBlank = false;
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new TarotAdderEntity();
    }

    @Override
    protected boolean activate(World world, BlockPos pos, BlockPos pipe, Entity entity, PlayerEntity player) {
        BlockPos.Mutable center = pos.mutableCopy();
        center.setY(pipe.getY());
        BlockEntity blockEntity = world.getBlockEntity(center);
        if(entity instanceof CardHolder && blockEntity instanceof TarotAdderEntity) {
            TarotAdderEntity adder = (TarotAdderEntity)blockEntity;
            if(adder.card != null && entity instanceof LivingEntity) {
                ((LivingEntity) entity).addStatusEffect(adder.card.effect.newInstance());
                adder.card = null;
                return true;
            } else if(adder.hasBlank) {
                adder.card = ((CardHolder) entity).removeCard();
                if(adder.card != null)
                    adder.hasBlank = false;
                return true;
            }
        }

        return false;
    }
}
