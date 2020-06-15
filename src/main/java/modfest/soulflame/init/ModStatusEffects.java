package modfest.soulflame.init;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.entity.effect.CustomStatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.registry.Registry;

public class ModStatusEffects {

    public static StatusEffect WAVERING_SOUL;

    public static void register() {
        WAVERING_SOUL = register("wavering_soul", new CustomStatusEffect(StatusEffectType.NEUTRAL, 13793020) {

            @Override
            public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
                // If an entity has this potion effect and dies, it should drop an item
                // Not sure how to activate this on death
                ItemScatterer.spawn(entity.getEntityWorld(), entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ(), new ItemStack(ModItems.baseTarot));
                super.onRemoved(entity, attributes, amplifier);
            }
        });
    }

    private static <T extends StatusEffect> T register(String name, T StatusEffect) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(SoulFlame.MODID, name), StatusEffect);
    }
}
