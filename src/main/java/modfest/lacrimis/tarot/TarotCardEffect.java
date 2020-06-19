package modfest.lacrimis.tarot;

import modfest.lacrimis.entity.effect.CustomStatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

public class TarotCardEffect extends CustomStatusEffect {
    public final TarotCardType type;

    public TarotCardEffect(TarotCardType type) {
        super(StatusEffectType.BENEFICIAL, 13793020);
        this.type = type;
    }

    public StatusEffectInstance newInstance() {
        return new StatusEffectInstance(this, 100, 1);
    }

    public void reapply(LivingEntity entity) {
        if(entity instanceof CardHolder && !entity.hasStatusEffect(this)) {
            for(TarotCardType card : ((CardHolder) entity).getCards())
                if(card == type) {
                    //entity.applyStatusEffect(newInstance());
                    entity.addStatusEffect(newInstance());
                }
        }

    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if(entity instanceof CardHolder)
            ((CardHolder) entity).addCard(type);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        reapply(entity);
    }

    @Override
    protected String loadTranslationKey() {
        String translationKey = super.loadTranslationKey();
        if (translationKey.contains("effect"))
            translationKey = Util.createTranslationKey("item", Registry.STATUS_EFFECT.getId(this));

        return translationKey;
    }
}
