package modfest.lacrimis.entity.effect;

import net.minecraft.entity.damage.DamageSource;

public class CustomDamageSources extends DamageSource{

    public static final DamageSource TEAR_POISON = (new CustomDamageSources("tear_poison")).setBypassesArmor().setUsesMagic();

    protected CustomDamageSources(String name) {
        super(name);
    }
}
