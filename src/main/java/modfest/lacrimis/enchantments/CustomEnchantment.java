package modfest.lacrimis.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class CustomEnchantment extends Enchantment {

    public final CustomEnchantmentTarget type;

    protected CustomEnchantment(Rarity weight, CustomEnchantmentTarget type, EquipmentSlot[] slotTypes) {
        // I want to use a custom target so it doesn't enchant on vanilla armor, not sure how
        super(weight, EnchantmentTarget.ARMOR, slotTypes);
        this.type = type;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return this.type.isAcceptableItem(stack.getItem());
    }
}
