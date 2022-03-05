package modfest.lacrimis.entity;

import com.google.common.collect.ImmutableList;
import modfest.lacrimis.item.ModItems;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulShellEntity extends LivingEntity {
    //Inventory
    public final DefaultedList<ItemStack> main;
    public final DefaultedList<ItemStack> armor;
    public final DefaultedList<ItemStack> offHand;
    private final List<DefaultedList<ItemStack>> combinedInventory;
    private int selectedSlot = 0;

    //Player notes
    public NbtCompound hunger = new NbtCompound();
    public int experienceLevel;
    public int totalExperience;
    public float experienceProgress;

    public SoulShellEntity(World world) {
        super(ModEntities.soulShell, world);
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

    @Override
    public ItemStack getMainHandStack() {
        return main.get(selectedSlot);
    }

    public ItemStack getEquippedStack(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return main.get(selectedSlot);
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

    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if(itemStack.getItem() == ModItems.taintedPearl) {
            player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
            if (!player.isCreative())
                itemStack.decrement(1);
            swapWithPlayer(player.world, player);
            return ActionResult.SUCCESS;
        } else if (itemStack.getItem() != Items.NAME_TAG) {
            if (player.isSpectator()) {
                return ActionResult.SUCCESS;
            } else if (player.world.isClient) {
                return ActionResult.CONSUME;
            } else {
                EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
                if (itemStack.isEmpty()) {
                    EquipmentSlot equipmentSlot2 = this.slotFromPosition(hitPos);
                    if (this.hasStackEquipped(equipmentSlot2) && this.equip(player, equipmentSlot2, itemStack, hand)) {
                        return ActionResult.SUCCESS;
                    }
                } else if (this.equip(player, equipmentSlot, itemStack, hand)) {
                    return ActionResult.SUCCESS;
                }

                return ActionResult.PASS;
            }
        } else {
            return ActionResult.PASS;
        }
    }

    private EquipmentSlot slotFromPosition(Vec3d vec3d) {
        EquipmentSlot equipmentSlot = EquipmentSlot.MAINHAND;
        double d = vec3d.y;
        EquipmentSlot equipmentSlot2 = EquipmentSlot.FEET;
        if (d >= 0.1D && d < 0.1D + 0.45D && this.hasStackEquipped(equipmentSlot2)) {
            equipmentSlot = EquipmentSlot.FEET;
        } else if (d >= 0.9D && d < 0.9D + 0.7D && this.hasStackEquipped(EquipmentSlot.CHEST)) {
            equipmentSlot = EquipmentSlot.CHEST;
        } else if (d >= 0.4D && d < 0.4D + 0.8D && this.hasStackEquipped(EquipmentSlot.LEGS)) {
            equipmentSlot = EquipmentSlot.LEGS;
        } else if (d >= 1.6D && this.hasStackEquipped(EquipmentSlot.HEAD)) {
            equipmentSlot = EquipmentSlot.HEAD;
        } else if (!this.hasStackEquipped(EquipmentSlot.MAINHAND) && this.hasStackEquipped(EquipmentSlot.OFFHAND)) {
            equipmentSlot = EquipmentSlot.OFFHAND;
        }

        return equipmentSlot;
    }

    private boolean equip(PlayerEntity player, EquipmentSlot slot, ItemStack stack, Hand hand) {
        ItemStack itemStack = this.getEquippedStack(slot);

        ItemStack itemStack3;
        if (player.isCreative() && itemStack.isEmpty() && !stack.isEmpty()) {
            itemStack3 = stack.copy();
            itemStack3.setCount(1);
            this.equipStack(slot, itemStack3);
            return true;
        } else if (!stack.isEmpty() && stack.getCount() > 1) {
            if (!itemStack.isEmpty()) {
                return false;
            } else {
                itemStack3 = stack.copy();
                itemStack3.setCount(1);
                this.equipStack(slot, itemStack3);
                stack.decrement(1);
                return true;
            }
        } else {
            this.equipStack(slot, stack);
            player.setStackInHand(hand, itemStack);
            return true;
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
    
    public void swapWithPlayer(World world, PlayerEntity player) {
        SoulShellEntity other = ModEntities.soulShell.create(world);
        if(other == null || world.isClient)
            return;
        
        //Move player inventory
        List<DefaultedList<ItemStack>> playerInventory = ImmutableList.of(
                player.getInventory().main, player.getInventory().armor, player.getInventory().offHand);
        for(int i = 0; i < combinedInventory.size(); ++i) {
            for(int j = 0; j < combinedInventory.get(i).size(); ++j) {
                other.combinedInventory.get(i).set(j, playerInventory.get(i).get(j).copy());
                playerInventory.get(i).set(j, combinedInventory.get(i).get(j).copy());
            }
        }
        
        //Copy player properties
        other.experienceLevel = player.experienceLevel;
        other.experienceProgress = player.experienceProgress;
        other.totalExperience = player.totalExperience;
        other.selectedSlot = player.getInventory().selectedSlot;
        player.getHungerManager().writeNbt(other.hunger);
        other.setHealth(player.getHealth());
        other.setAbsorptionAmount(player.getAbsorptionAmount());
        other.setStingerCount(player.getStingerCount());
        other.setStuckArrowCount(player.getStuckArrowCount());
        for(StatusEffectInstance effect : player.getStatusEffects())
            other.addStatusEffect(effect);

        //Spawn other stand
        float f = (float) MathHelper.floor((MathHelper.wrapDegrees(player.bodyYaw) + 22.5F) / 45.0F) * 45.0F;
        other.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), f, 0.0F);
        other.setHeadYaw(player.headYaw);
        world.spawnEntity(other);
        world.playSound(null, other.getX(), other.getY(), other.getZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);

        //Set basic player properties
        player.experienceLevel = experienceLevel;
        player.experienceProgress = experienceProgress;
        player.totalExperience = totalExperience;
        player.getHungerManager().readNbt(hunger);
        player.setHealth(getHealth());
        player.setAbsorptionAmount(getAbsorptionAmount());
        player.setStingerCount(getStingerCount());
        player.setStuckArrowCount(getStuckArrowCount());
        player.clearStatusEffects();
        for(StatusEffectInstance effect : getStatusEffects())
            player.addStatusEffect(effect);

        //Move player
        if(player.hasVehicle())
            player.stopRiding();
        player.teleport(getX(), getY(), getZ(), true);
        player.fallDistance = 0;
        player.setHeadYaw(headYaw);
        this.remove(RemovalReason.DISCARDED);
    }

    public void swapWithEntity(LivingEntity entity) {
        if(entity instanceof ServerPlayerEntity)
            swapWithPlayer(world, (PlayerEntity) entity);
    }

    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);

        NbtList listTag = tag.getList("Inventory", 10);
        this.main.clear();
        this.armor.clear();
        this.offHand.clear();

        for(int i = 0; i < listTag.size(); ++i) {
            NbtCompound compoundTag = listTag.getCompound(i);
            int j = compoundTag.getByte("Slot") & 255;
            ItemStack itemStack = ItemStack.fromNbt(compoundTag);
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

        this.experienceProgress = tag.getFloat("XpP");
        this.experienceLevel = tag.getInt("XpLevel");
        this.totalExperience = tag.getInt("XpTotal");
        this.hunger = tag.getCompound("Hunger");
    }

    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putInt("DataVersion", SharedConstants.getGameVersion().getWorldVersion());

        int k;
        NbtList listTag = new NbtList();
        NbtCompound compoundTag3;
        for(k = 0; k < this.main.size(); ++k) {
            if (!main.get(k).isEmpty()) {
                compoundTag3 = new NbtCompound();
                compoundTag3.putByte("Slot", (byte)k);
                main.get(k).writeNbt(compoundTag3);
                listTag.add(compoundTag3);
            }
        }

        for(k = 0; k < this.armor.size(); ++k) {
            if (!armor.get(k).isEmpty()) {
                compoundTag3 = new NbtCompound();
                compoundTag3.putByte("Slot", (byte)(k + 100));
                armor.get(k).writeNbt(compoundTag3);
                listTag.add(compoundTag3);
            }
        }

        for(k = 0; k < this.offHand.size(); ++k) {
            if (!offHand.get(k).isEmpty()) {
                compoundTag3 = new NbtCompound();
                compoundTag3.putByte("Slot", (byte)(k + 150));
                offHand.get(k).writeNbt(compoundTag3);
                listTag.add(compoundTag3);
            }
        }
        
        tag.put("Inventory", listTag);
        tag.putFloat("XpP", this.experienceProgress);
        tag.putInt("XpLevel", this.experienceLevel);
        tag.putInt("XpTotal", this.totalExperience);
        tag.put("Hunger", this.hunger);
    }

    private void playBreakSound() {
        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ARMOR_STAND_BREAK, this.getSoundCategory(), 1.0F, 1.0F);
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
