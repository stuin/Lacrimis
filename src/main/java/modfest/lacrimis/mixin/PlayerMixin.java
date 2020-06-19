package modfest.lacrimis.mixin;

import modfest.lacrimis.init.ModStatusEffects;
import modfest.lacrimis.tarot.CardHolder;
import modfest.lacrimis.tarot.TarotCardEffect;
import modfest.lacrimis.tarot.TarotCardType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

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
        cards[2] = cards[1];
        cards[1] = cards[0];
        cards[0] = type;
    }

    @Override
    public TarotCardType[] getCards() {
        return cards;
    }
}
