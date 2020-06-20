package modfest.lacrimis.item.armor;

import modfest.lacrimis.enchantments.WardedEnchantment;
import modfest.lacrimis.init.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class SoakedArmor extends ArmorItem implements Soaked {

    public SoakedArmor(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        // 1% chance to take durability every tick
        if (Math.random() > 0.99) {
            if (EnchantmentHelper.getLevel(ModEnchantments.WARDED, stack) > 0 && !WardedEnchantment.shouldPreventDamage(stack)) {
                stack.damage(1, new Random(), null);
            } else if (EnchantmentHelper.getLevel(ModEnchantments.WARDED, stack) <= 0) {
                stack.damage(1, new Random(), null);
            }
        }
    }

}
