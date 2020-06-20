package modfest.lacrimis.enchantments;

import modfest.lacrimis.item.armor.Soaked;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class WardedEnchantment extends CustomEnchantment {
    public WardedEnchantment(Rarity weight, EquipmentSlot[] slotTypes) {
        super(weight, CustomEnchantmentTarget.SOAKED, slotTypes);
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    public static boolean shouldPreventDamage(ItemStack item) {
        // If warded, it should only have a very small chance of loosing durability
        if(item.getItem() instanceof Soaked) return (Math.random() < 0.995);
        else return false;
    }
}
