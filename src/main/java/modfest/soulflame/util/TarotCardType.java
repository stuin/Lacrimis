package modfest.soulflame.util;

import net.minecraft.entity.EntityType;

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
    THE_HERMIT("the_hermit", EntityType.SHULKER),
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
    JUDGEMENT("judgement", EntityType.PUFFERFISH),
    THE_WORLD("the_world", EntityType.ENDERMAN),
    ;

    public final String id;
    public final EntityType<?> cover;

    TarotCardType(String id, EntityType<?> cover) {
        this.id = id;
        this.cover = cover;
    }
}