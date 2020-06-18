package modfest.lacrimis.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Consumer;

public enum TarotCardType {
    THE_FOOL("the_fool", EntityType.PIGLIN),
    THE_MAGICIAN("the_magician", EntityType.EVOKER),
    THE_HIGH_PRIESTESS("the_high_priestess", EntityType.WITCH),
    THE_EMPRESS("the_empress", EntityType.VEX),
    THE_EMPEROR("the_emperor", EntityType.PHANTOM),
    THE_HIEROPHANT("the_hierophant", EntityType.STRAY),
    THE_LOVERS("the_lovers", EntityType.SKELETON), // TODO: skeleton horseman
    THE_CHARIOT("the_chariot", EntityType.STRIDER),
    STRENGTH("strength", EntityType.IRON_GOLEM),
    THE_HERMIT("the_hermit", EntityType.SHULKER, TarotCardType::initShulker),
    WHEEL_OF_FORTUNE("wheel_of_fortune", EntityType.VILLAGER),
    JUSTICE("justice", EntityType.VINDICATOR),
    THE_HANGED_MAN("the_hanged_man", EntityType.PILLAGER),
    DEATH("death", EntityType.WITHER),
    TEMPERANCE("temperance", EntityType.TURTLE),
    THE_DEVIL("the_devil", EntityType.SILVERFISH),
    THE_TOWER("the_tower", EntityType.BLAZE),
    THE_STAR("the_star", null),
    THE_MOON("the_moon", null),
    THE_SUN("the_sun", null),
    JUDGEMENT("judgement", EntityType.PUFFERFISH, e -> e.setPuffState(2)),
    THE_WORLD("the_world", EntityType.ENDERMAN),
    ;

    public final String id;
    public final EntityType<?> cover;
    private final Consumer<? extends Entity> init;

    <T extends Entity> TarotCardType(String id, EntityType<T> cover, Consumer<T> init) {
        this.id = id;
        this.cover = cover;
        this.init = init;
    }

    TarotCardType(String id, EntityType<?> cover) {
        this(id, cover, null);
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
        int peekTarget = 30;
        e.setPeekAmount(peekTarget);
        while (!MathHelper.approximatelyEquals(e.getOpenProgress(1.0f), peekTarget * 0.01f)) {
            e.tick();
        }
    }

}