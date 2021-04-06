package modfest.lacrimis.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Arm;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class SoulShellEntity extends LivingEntity {
    //Inventory
    public final DefaultedList<ItemStack> main;
    public final DefaultedList<ItemStack> armor;
    public final DefaultedList<ItemStack> offHand;
    private final List<DefaultedList<ItemStack>> combinedInventory;
    public int selectedSlot;

    //Player notes
    protected HungerManager hungerManager = new HungerManager();
    public final PlayerAbilities abilities = new PlayerAbilities();
    public int experienceLevel;
    public int totalExperience;
    public float experienceProgress;

    public SoulShellEntity(EntityType<? extends SoulShellEntity> entityType, World world) {
        super(entityType, world);
        this.main = DefaultedList.ofSize(36, ItemStack.EMPTY);
        this.armor = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.offHand = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.combinedInventory = ImmutableList.of(this.main, this.armor, this.offHand);
    }

    public static DefaultAttributeContainer.Builder createSoulShellAttributes() {
        return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.10000000149011612D).add(EntityAttributes.GENERIC_ATTACK_SPEED).add(EntityAttributes.GENERIC_LUCK);
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return armor;
    }

    public ItemStack getEquippedStack(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return getMainHandStack();
        } else if (slot == EquipmentSlot.OFFHAND) {
            return offHand.get(0);
        } else {
            return slot.getType() == EquipmentSlot.Type.ARMOR ? armor.get(slot.getEntitySlotId()) : ItemStack.EMPTY;
        }
    }

    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            onEquipStack(stack);
            main.set(selectedSlot, stack);
        } else if (slot == EquipmentSlot.OFFHAND) {
            onEquipStack(stack);
            offHand.set(0, stack);
        } else if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            onEquipStack(stack);
            armor.set(slot.getEntitySlotId(), stack);
        }

    }

    protected void dropInventory() {
        this.playBreakSound();
        super.dropInventory();

        for(DefaultedList<ItemStack> itemStacks : combinedInventory) {
            for(int i = 0; i < itemStacks.size(); ++i) {
                ItemStack itemStack = itemStacks.get(i);
                if(!itemStack.isEmpty()) {
                    Block.dropStack(this.world, this.getBlockPos().up(), itemStack);
                    itemStacks.set(i, ItemStack.EMPTY);
                }
            }
        }
    }

    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);

        ListTag listTag = tag.getList("Inventory", 10);
        this.main.clear();
        this.armor.clear();
        this.offHand.clear();

        for(int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            int j = compoundTag.getByte("Slot") & 255;
            ItemStack itemStack = ItemStack.fromTag(compoundTag);
            if (!itemStack.isEmpty()) {
                if (j >= 0 && j < this.main.size()) {
                    this.main.set(j, itemStack);
                } else if (j >= 100 && j < this.armor.size() + 100) {
                    this.armor.set(j - 100, itemStack);
                } else if (j >= 150 && j < this.offHand.size() + 150) {
                    this.offHand.set(j - 150, itemStack);
                }
            }
        }

        this.selectedSlot = tag.getInt("SelectedItemSlot");
        this.experienceProgress = tag.getFloat("XpP");
        this.experienceLevel = tag.getInt("XpLevel");
        this.totalExperience = tag.getInt("XpTotal");
        this.hungerManager.fromTag(tag);
        this.abilities.deserialize(tag);
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(abilities.getWalkSpeed());
    }

    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt("DataVersion", SharedConstants.getGameVersion().getWorldVersion());

        CompoundTag compoundTag3;
        ListTag listTag = new ListTag();
        for(DefaultedList<ItemStack> itemStacks : combinedInventory) {
            for(int k = 0; k < itemStacks.size(); ++k) {
                if(!(itemStacks.get(k)).isEmpty()) {
                    compoundTag3 = new CompoundTag();
                    compoundTag3.putByte("Slot", (byte) k);
                    (itemStacks.get(k)).toTag(compoundTag3);
                    listTag.add(compoundTag3);
                }
            }
        }
        
        tag.put("Inventory", listTag);
        tag.putInt("SelectedItemSlot", this.selectedSlot);
        tag.putFloat("XpP", this.experienceProgress);
        tag.putInt("XpLevel", this.experienceLevel);
        tag.putInt("XpTotal", this.totalExperience);
        this.hungerManager.toTag(tag);
        this.abilities.serialize(tag);
    }

    private void playBreakSound() {
        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ARMOR_STAND_BREAK, this.getSoundCategory(), 1.0F, 1.0F);
    }

    public void kill() {
        this.remove();
    }

    public boolean handleAttack(Entity attacker) {
        return attacker instanceof PlayerEntity && !this.world.canPlayerModifyAt((PlayerEntity)attacker, this.getBlockPos());
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    public boolean isPushable() {
        return false;
    }

    public boolean isMobOrPlayer() {
        return false;
    }

    public boolean isAffectedBySplashPotions() {
        return false;
    }

    protected SoundEvent getFallSound(int distance) {
        return SoundEvents.ENTITY_ARMOR_STAND_FALL;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ARMOR_STAND_HIT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ARMOR_STAND_BREAK;
    }
}
