package modfest.lacrimis.mixin;

import modfest.lacrimis.init.ModStatusEffects;
import modfest.lacrimis.tarot.CardHolder;
import modfest.lacrimis.tarot.TarotCardEffect;
import modfest.lacrimis.tarot.TarotCardType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity implements CardHolder {
    private final TarotCardType[] cards = new TarotCardType[3];

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean clearStatusEffects() {
        boolean b = super.clearStatusEffects();
        for(TarotCardType card : cards) {
            if(card != null)
                addStatusEffect(ModStatusEffects.tarotEffects.get(card).newInstance());
        }
        return b;
    }

    @Override
    public boolean removeStatusEffect(StatusEffect type) {
        boolean b = super.removeStatusEffect(type);
        if(type instanceof TarotCardEffect)
            ((TarotCardEffect) type).reapply(this);
        return b;
    }

    @Override
    public void addCard(TarotCardType type) {
        for(int i = 0; i < cards.length; i++) {
            if(cards[i] == type)
                return;
            if(cards[i] == null) {
                cards[i] = type;
                return;
            }
        }
        removeStatusEffect(cards[2].effect);
        cards[2] = cards[1];
        cards[1] = cards[0];
        cards[0] = type;
    }

    @Override
    public TarotCardType[] getCards() {
        return cards;
    }
    
    @Inject(at = @At("TAIL"), method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V")
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
        if(tag.contains("tarot0"))
            cards[0] = TarotCardType.valueOf(tag.getString("tarot0"));
        else
            cards[0] = null;
        if(tag.contains("tarot1"))
            cards[1] = TarotCardType.valueOf(tag.getString("tarot1"));
        else
            cards[1] = null;
        if(tag.contains("tarot2"))
            cards[2] = TarotCardType.valueOf(tag.getString("tarot2"));
        else
            cards[2] = null;
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V")
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
        if(cards[0] != null)
            tag.putString("tarot0", cards[0].name());
        if(cards[1] != null)
            tag.putString("tarot1", cards[1].name());
        if(cards[2] != null)
            tag.putString("tarot2", cards[2].name());
    }
}
