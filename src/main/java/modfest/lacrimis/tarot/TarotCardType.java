package modfest.lacrimis.tarot;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TarotCardType {
    THE_FOOL("the_fool", EntityType.PIGLIN, null),
    THE_MAGICIAN("the_magician", EntityType.EVOKER, null),
    THE_HIGH_PRIESTESS("the_high_priestess", EntityType.WITCH, null),
    THE_EMPRESS("the_empress", EntityType.VEX, null),
    THE_EMPEROR("the_emperor", EntityType.PHANTOM, null),
    THE_HIEROPHANT("the_hierophant", EntityType.STRAY, null),
    THE_LOVERS("the_lovers", EntityType.SKELETON, null), // TODO: skeleton horseman
    THE_CHARIOT("the_chariot", EntityType.STRIDER, null),
    STRENGTH("strength", EntityType.IRON_GOLEM, null),
    THE_HERMIT("the_hermit", EntityType.SHULKER, TarotCardType::initShulker, null), //10
    WHEEL_OF_FORTUNE("wheel_of_fortune", EntityType.VILLAGER, null),
    JUSTICE("justice", EntityType.VINDICATOR, null),
    THE_HANGED_MAN("the_hanged_man", EntityType.PILLAGER, null),
    DEATH("death", EntityType.WITHER, null),
    TEMPERANCE("temperance", EntityType.TURTLE, null),
    THE_DEVIL("the_devil", EntityType.SILVERFISH, null),
    THE_TOWER("the_tower", EntityType.BLAZE, null),
    THE_STAR("the_star", null, null),
    THE_MOON("the_moon", null, null),
    THE_SUN("the_sun", null, null), //20
    JUDGEMENT("judgement", EntityType.PUFFERFISH, e -> e.setPuffState(2), null),
    THE_WORLD("the_world", EntityType.ENDERMAN, null),
    ;

    public final String id;
    public final EntityType<?> cover;
    public final TarotCardEffect effect;
    private final Consumer<? extends Entity> init;

    <T extends Entity> TarotCardType(String id, EntityType<T> cover, Consumer<T> init, TarotCardEffect effect) {
        this.id = id;
        this.cover = cover;
        this.init = init;

        if(effect == null)
            this.effect = new TarotCardEffect(this);
        else
            this.effect = effect;
    }

    TarotCardType(String id, EntityType<?> cover, TarotCardEffect effect) {
        this(id, cover, null, effect);
    }

    @SuppressWarnings("unchecked")
    public Entity create(World world) {
        if (this.cover == null) return null;
        Entity e = this.cover.create(world);
        if (this.init != null) {
            ((Consumer<Entity>) this.init).accept(e);
        }
        return e;
    }

    private static void initShulker(ShulkerEntity e) {
        e.setAiDisabled(true);
        int peekTarget = 40;
        e.setPeekAmount(peekTarget);
        while (!MathHelper.approximatelyEquals(e.getOpenProgress(1.0f), peekTarget * 0.01f)) {
            e.tick();
        }
    }

    public static <T> Map<TarotCardType, T> tarotInit(Function<TarotCardType, T> func) {
        return Arrays.stream(TarotCardType.values())
                .collect(Collectors.toMap(k -> k, func));
    }

}