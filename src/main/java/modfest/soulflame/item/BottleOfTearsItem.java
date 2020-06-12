package modfest.soulflame.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BottleOfTearsItem extends Item {
	public BottleOfTearsItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public boolean hasEnchantmentGlint(ItemStack stack) {
		return true;
	}

}
