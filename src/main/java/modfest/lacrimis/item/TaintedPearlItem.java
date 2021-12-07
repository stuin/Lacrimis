package modfest.lacrimis.item;


import modfest.lacrimis.entity.TaintedPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class TaintedPearlItem extends Item {
    public TaintedPearlItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW,
                SoundCategory.NEUTRAL, 0.5F, 0.4F / (new Random().nextFloat() * 0.4F + 0.8F));
        user.getItemCooldownManager().set(this, 20);
        if (!world.isClient) {
            ThrownItemEntity pearlEntity = new TaintedPearlEntity(world);
            pearlEntity.setOwner(user);
            pearlEntity.setPos(user.getX(), user.getEyeY() - 0.10000000149011612D, user.getZ());
            pearlEntity.setItem(itemStack);
            pearlEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            world.spawnEntity(pearlEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.isCreative()) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
