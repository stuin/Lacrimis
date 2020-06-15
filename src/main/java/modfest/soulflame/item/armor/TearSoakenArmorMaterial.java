package modfest.soulflame.item.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public class TearSoakenArmorMaterial implements ArmorMaterial {

    private static final int[] baseDurability = {13, 15, 16, 11};
    //Not sure what these should be
    private static final int[] protectionAmounts = {13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] armorValues;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final Lazy<Ingredient> repairIngredient;
    private final float knockbackResistance;

    TearSoakenArmorMaterial(String name, int durabilityMultiplier, int[] armorValueArr, int enchantability, SoundEvent soundEvent, float toughness, Supplier<Ingredient> repairIngredient, float knockbackResistance) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.armorValues = armorValueArr;
        this.enchantability = enchantability;
        this.equipSound = soundEvent;
        this.toughness = toughness;
        this.repairIngredient = new Lazy(repairIngredient);
        this.knockbackResistance = knockbackResistance;
    }

    public int getDurability(EquipmentSlot equipmentSlot_1) {
        return baseDurability[equipmentSlot_1.getEntitySlotId()] * this.durabilityMultiplier;
    }

    public int getProtectionAmount(EquipmentSlot equipmentSlot_1) {
        return this.protectionAmounts[equipmentSlot_1.getEntitySlotId()];
    }

    public int getEnchantability() { return this.enchantability; }

    public SoundEvent getEquipSound() { return this.equipSound; }

    public Ingredient getRepairIngredient() { return this.repairIngredient.get(); }

    @Environment(EnvType.CLIENT)
    public String getName() { return this.name; }

    public float getToughness() { return this.toughness; }

    public float getKnockbackResistance() { return this.knockbackResistance; }
}
