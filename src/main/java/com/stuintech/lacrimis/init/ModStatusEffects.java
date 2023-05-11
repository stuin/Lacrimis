package com.stuintech.lacrimis.init;

import com.stuintech.lacrimis.Lacrimis;
import com.stuintech.lacrimis.item.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStatusEffects extends DamageSource {
    public static final DamageSource TEAR_DAMAGE = new ModStatusEffects("tear_poison").setBypassesArmor().setUsesMagic();

    public static StatusEffect TEAR_POISON;
    public static StatusEffect WAVERING_SOUL;

    public static void register() {
        TEAR_POISON = register("tear_poison", new StatusEffect(StatusEffectCategory.HARMFUL, 10359895) {
            @Override
            public void applyUpdateEffect(LivingEntity entity, int amplifier) {
                if(this == ModStatusEffects.TEAR_POISON) {
                    int damage = 1;
                    if(!entity.world.isClient) {
                        for(EquipmentSlot slot : ModEnchantments.ALL_ARMOR) {
                            ItemStack item = entity.getEquippedStack(slot);
                            if(item.isEmpty())
                                damage += 1;
                            else if(EnchantmentHelper.getLevel(ModEnchantments.WARDED, item) <= 0)
                                item.damage(20, entity, (p) -> p.sendEquipmentBreakStatus(slot));
                        }
                    }

                    damage /= 2;
                    if (entity.getHealth() > damage)
                        entity.damage(ModStatusEffects.TEAR_DAMAGE, damage);
                    else if(entity.getHealth() > 1)
                        entity.damage(ModStatusEffects.TEAR_DAMAGE, 1);
                }
                super.applyUpdateEffect(entity, amplifier);
            }

            @Override
            public boolean canApplyUpdateEffect(int duration, int amplifier) {
                int k;
                if(this == ModStatusEffects.TEAR_POISON) {
                    k = 50 >> amplifier;
                    if (k > 0)
                        return duration % k == 0;
                    else
                        return true;
                }
                return super.canApplyUpdateEffect(duration, amplifier);
            }
        });


        WAVERING_SOUL = register("wavering_soul", new StatusEffect(StatusEffectCategory.NEUTRAL, 13793020) {});
        ServerLivingEntityEvents.AFTER_DEATH.register(ModStatusEffects::afterDeath);
    }

    public static boolean afterDeath(LivingEntity entity, DamageSource damageSource) {
        if(entity.hasStatusEffect(WAVERING_SOUL))
            entity.dropStack(new ItemStack(ModItems.solidifiedTear), entity.getHeight() / 2);
        return true;
    }

    private static <T extends StatusEffect> T register(String name, T StatusEffect) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(Lacrimis.MODID, name), StatusEffect);
    }

    protected ModStatusEffects(String name) {
        super(name);
    }
}
