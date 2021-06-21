package modfest.lacrimis.mixin;

import modfest.lacrimis.block.NetworkLinkBlock;
import modfest.lacrimis.block.rune.SoulSwapBlock;
import modfest.lacrimis.init.ModItems;
import modfest.lacrimis.util.DuctUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z", cancellable = true)
    private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if(!source.isOutOfWorld()) {
            ItemStack itemStack = null;
            Hand[] var4 = Hand.values();

            //Check for totem
            for(Hand hand : var4) {
                ItemStack itemStack2 = this.getStackInHand(hand);
                if (itemStack2.getItem() == ModItems.soulTotem) {
                    itemStack = itemStack2;
                    break;
                }
            }


            //Get destination
            if (itemStack != null && itemStack.hasTag()) {
                int x = itemStack.getTag().getInt("X");
                int y = itemStack.getTag().getInt("Y");
                int z = itemStack.getTag().getInt("Z");
                BlockPos pos = new BlockPos(x, y, z);
                if(world.getBlockState(pos).getBlock() instanceof NetworkLinkBlock) {
                    DuctUtil.locateSink(world, pos, new SoulSwapBlock.PlayerContainer(this, itemStack));
                    cir.setReturnValue(true);
                }
            }
        }
    }

    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);
}
