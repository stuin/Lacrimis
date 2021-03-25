package modfest.lacrimis.item.tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

import modfest.lacrimis.init.ModStatusEffects;
import modfest.lacrimis.item.Soaked;

public class SoakedSwordItem extends SwordItem implements Soaked {
    public SoakedSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // If the user has full soaked armor, flip bad effects and hurt the enemy with them
        boolean fullSoaked = true;

        for (ItemStack armor : attacker.getArmorItems()) {
            if (!(armor.getItem() instanceof Soaked)) {
                fullSoaked = false;
                break;
            }
        }

        if (fullSoaked) {
            target.addStatusEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, 100, 3));
        } else {
            attacker.addStatusEffect(new StatusEffectInstance(ModStatusEffects.TEAR_POISON, 200, 3));
        }

        return super.postHit(stack, target, attacker);
    }

}
