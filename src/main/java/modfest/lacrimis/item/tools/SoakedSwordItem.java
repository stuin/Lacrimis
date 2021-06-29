package modfest.lacrimis.item.tools;

import modfest.lacrimis.init.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class SoakedSwordItem extends SwordItem {
    public SoakedSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, 100, 3));
        attacker.addStatusEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, 200, 3));

        return super.postHit(stack, target, attacker);
    }

}
