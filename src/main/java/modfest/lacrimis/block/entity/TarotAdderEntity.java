package modfest.lacrimis.block.entity;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.init.ModBlockEntityTypes;
import modfest.lacrimis.tarot.TarotCardType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;

public class TarotAdderEntity extends BlockEntity {
    public TarotCardType card = null;
    public boolean hasBlank = false;
    
    public TarotAdderEntity() {
        super(ModBlockEntityTypes.tarotAdder);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        if(tag.getString("card").equals("null"))
            card = null;
        else try {
                card = TarotCardType.valueOf(tag.getString("card"));
        } catch(IllegalArgumentException e) {
            Lacrimis.LOGGER.warn("Background error while loading TarotAdderEntity");
        }
        hasBlank = tag.getBoolean("hasBlank");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        if(card == null)
            tag.putString("card", "null");
        else
            tag.putString("card", card.name());
        tag.putBoolean("hasBlank", hasBlank);
        return super.toTag(tag);
    }
}
