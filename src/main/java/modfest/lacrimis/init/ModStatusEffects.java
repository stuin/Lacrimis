package modfest.lacrimis.init;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.entity.effect.CustomStatusEffect;
import modfest.lacrimis.tarot.TarotCardType;
import modfest.lacrimis.tarot.TarotCardEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class ModStatusEffects {

    public static StatusEffect WAVERING_SOUL;
    public static Map<TarotCardType, TarotCardEffect> tarotEffects;
    public static StatusEffect TAROT_COOLDOWN;

    public static void register() {
        WAVERING_SOUL = register("wavering_soul", new CustomStatusEffect(StatusEffectType.NEUTRAL, 13793020) {

            @Override
            public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
                if (entity.getHealth() < 1) {
                    entity.dropStack(new ItemStack(ModItems.solidifiedTear), entity.getHeight() / 2);
                    entity.kill();
                }
                super.onRemoved(entity, attributes, amplifier);
            }
        });
        TAROT_COOLDOWN = register("tarot_cooldown", new CustomStatusEffect(StatusEffectType.NEUTRAL, 13793020));

        tarotEffects = TarotCardType.tarotInit(k -> register(String.format("tarot_card_%s", k.id), k.effect));
    }

    private static <T extends StatusEffect> T register(String name, T StatusEffect) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(Lacrimis.MODID, name), StatusEffect);
    }
}
