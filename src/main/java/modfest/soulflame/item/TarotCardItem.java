package modfest.soulflame.item;

import net.minecraft.item.Item;

import modfest.soulflame.util.TarotCardType;

public class TarotCardItem extends Item {

    private final TarotCardType type;

    public TarotCardItem(TarotCardType type, Item.Settings settings) {
        super(settings);
        this.type = type;
    }

    public TarotCardType getType() {
        return this.type;
    }

}
