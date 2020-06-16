package modfest.lacrimis.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BottleOfTearsItem extends Item {
	public static final int capacity = 250;
	public BottleOfTearsItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public boolean hasEnchantmentGlint(ItemStack stack) {
		return true;
	}

}
