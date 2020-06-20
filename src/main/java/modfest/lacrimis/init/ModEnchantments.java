package modfest.lacrimis.init;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.enchantments.WardedEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEnchantments {

    private static EquipmentSlot[] ALL_ARMOR;
    public static Enchantment WARDED;

    public static void register() {
        ALL_ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

        WARDED = register("warded", new WardedEnchantment(Enchantment.Rarity.COMMON, ALL_ARMOR));
    }

    private static <T extends Enchantment> T register(String name, T Enchantment) {
        return Registry.register(Registry.ENCHANTMENT, new Identifier(Lacrimis.MODID, name), Enchantment);
    }

}
