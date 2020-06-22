package modfest.lacrimis.mixin;

import modfest.lacrimis.init.ModStatusEffects;
import modfest.lacrimis.tarot.CardHolder;
import modfest.lacrimis.tarot.TarotCardEffect;
import modfest.lacrimis.tarot.TarotCardType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tag.FluidTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity implements CardHolder {
    private final TarotCardType[] cards = new TarotCardType[3];
    
    @Final @Shadow
    public PlayerAbilities abilities;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    private void addEffect(LivingEntity entity, StatusEffect effect, int duration, int level) {
        if(!entity.hasStatusEffect(ModStatusEffects.TAROT_COOLDOWN)) {
            entity.addStatusEffect(new StatusEffectInstance(effect, duration, level));
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.TAROT_COOLDOWN, duration - 1, 1));
        }
    }
    
    @Override
    public boolean clearStatusEffects() {
        boolean b = super.clearStatusEffects();
        for(TarotCardType card : cards) {
            if(card != null)
                addStatusEffect(ModStatusEffects.tarotEffects.get(card).newInstance());
        }
        return b;
    }

    @Override
    public boolean removeStatusEffect(StatusEffect type) {
        boolean b = super.removeStatusEffect(type);
        if(type instanceof TarotCardEffect)
            ((TarotCardEffect) type).reapply(this);
        return b;
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(this.isInvulnerableTo(source))
            cir.setReturnValue(false);
        else if(abilities.invulnerable && !source.isOutOfWorld())
            cir.setReturnValue(false);

        //General damage effects
        if(source.isFire() && hasStatusEffect(TarotCardType.THE_CHARIOT.effect))
            cir.setReturnValue(false);
        if(hasStatusEffect(TarotCardType.THE_LOVERS.effect))
            addEffect(this, StatusEffects.REGENERATION, 100, 3);
        if(hasStatusEffect(TarotCardType.THE_HIGH_PRIESTESS.effect))
            addEffect(this, randomEffect(), 200, 1);
        if(hasStatusEffect(TarotCardType.TEMPERANCE.effect))
            addEffect(this, StatusEffects.RESISTANCE, 50, 1);
        
        //Attacker based effects
        if(source.getAttacker() instanceof LivingEntity) {
            if(hasStatusEffect(TarotCardType.JUSTICE.effect))
                addEffect((LivingEntity)source.getAttacker(), StatusEffects.WEAKNESS, 100, 2);
            if(hasStatusEffect(TarotCardType.JUDGEMENT.effect))
                source.getAttacker().damage(DamageSource.thorns(this), amount * 2);
            if(hasStatusEffect(TarotCardType.THE_TOWER.effect))
                source.getAttacker().setOnFireFor(5);
        }
    }

    @Inject(at = @At("HEAD"), method = "tick()V")
    public void tick(CallbackInfo ci) {
        //Situational tarot effects
        if(hasStatusEffect(TarotCardType.THE_HIEROPHANT.effect) && hasStatusEffect(StatusEffects.SLOWNESS))
            removeStatusEffect(StatusEffects.SLOWNESS);
        if(isSneaking() && hasStatusEffect(TarotCardType.THE_HERMIT.effect) && !hasStatusEffect(StatusEffects.RESISTANCE)) {
            addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20));
            addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 21, 2));
        }
        if(hasStatusEffect(TarotCardType.TEMPERANCE.effect) && isSubmergedIn(FluidTags.WATER))
            addEffect(this, StatusEffects.DOLPHINS_GRACE, 100, 1);
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "method_29920()Z")
    public void method_29920(CallbackInfoReturnable<Boolean> cir) {
        if(hasStatusEffect(TarotCardType.THE_HIEROPHANT.effect))
            cir.setReturnValue(false);
    }

    private StatusEffect randomEffect() {
        switch(random.nextInt(6)) {
            case 1:
                return StatusEffects.ABSORPTION;
            case 2:
                return StatusEffects.HEALTH_BOOST;
            case 3:
                return StatusEffects.HASTE;
            case 4:
                return StatusEffects.JUMP_BOOST;
            case 5:
                return StatusEffects.SPEED;
            case 6:
                return StatusEffects.STRENGTH;
            default:
                return StatusEffects.NIGHT_VISION;
        }
    }

    @Override
    public void addCard(TarotCardType type) {
        for(int i = 0; i < cards.length; i++) {
            if(cards[i] == type)
                return;
            if(cards[i] == null) {
                cards[i] = type;
                return;
            }
        }
        removeStatusEffect(cards[2].effect);
        cards[2] = cards[1];
        cards[1] = cards[0];
        cards[0] = type;
    }

    @Override
    public TarotCardType[] getCards() {
        return cards;
    }

    @Override
    public TarotCardType removeCard() {
        TarotCardType card = null;
        for(int i = cards.length; i > 0 && card == null; i--) {
            if(cards[i - 1] != null) {
                card = cards[i - 1];
                cards[i - 1] = null;
            }
        }
        return card;
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V")
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
        if(tag.contains("tarot0"))
            cards[0] = TarotCardType.valueOf(tag.getString("tarot0"));
        else
            cards[0] = null;
        if(tag.contains("tarot1"))
            cards[1] = TarotCardType.valueOf(tag.getString("tarot1"));
        else
            cards[1] = null;
        if(tag.contains("tarot2"))
            cards[2] = TarotCardType.valueOf(tag.getString("tarot2"));
        else
            cards[2] = null;
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V")
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
        if(cards[0] != null)
            tag.putString("tarot0", cards[0].name());
        if(cards[1] != null)
            tag.putString("tarot1", cards[1].name());
        if(cards[2] != null)
            tag.putString("tarot2", cards[2].name());
    }
}
