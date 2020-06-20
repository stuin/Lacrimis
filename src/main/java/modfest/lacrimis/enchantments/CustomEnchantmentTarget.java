package modfest.lacrimis.enchantments;

import modfest.lacrimis.item.armor.Soaked;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public enum CustomEnchantmentTarget {

    SOAKED {
        public boolean isAcceptableItem(Item item) {
            return item instanceof Soaked || Block.getBlockFromItem(item) instanceof Soaked;
        }
    };

    public abstract boolean isAcceptableItem(Item item);
}
