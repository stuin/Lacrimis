package modfest.lacrimis.mixin;

import modfest.lacrimis.tarot.TarotCardType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InteractionObserver;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(VillagerEntity.class)
public abstract class VillagerMixin extends AbstractTraderEntity implements InteractionObserver, VillagerDataContainer {
    public VillagerMixin(EntityType<? extends AbstractTraderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "prepareRecipesFor(Lnet/minecraft/entity/player/PlayerEntity;)V")
    private void prepareRecipesFor(PlayerEntity player, CallbackInfo ci) {
        if (player.hasStatusEffect(TarotCardType.WHEEL_OF_FORTUNE.effect)) {
            Iterator var5 = this.getOffers().iterator();

            while(var5.hasNext()) {
                TradeOffer tradeOffer2 = (TradeOffer)var5.next();
                double d = 0.3D + 0.0625D * (double)2;
                int k = (int)Math.floor(d * (double)tradeOffer2.getOriginalFirstBuyItem().getCount());
                tradeOffer2.increaseSpecialPrice(-Math.max(k, 1));
            }
        }
    }
}
