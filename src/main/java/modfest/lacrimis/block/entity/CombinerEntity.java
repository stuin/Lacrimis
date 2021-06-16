package modfest.lacrimis.block.entity;

import modfest.lacrimis.init.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CombinerEntity extends SoulTankEntity {
    public EntityType<?> type;

    public CombinerEntity() {
        super(ModEntities.combiner, 0, 2);
        type = null;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        if(!tag.getString("entity").equals("null"))
            type = Registry.ENTITY_TYPE.get(Identifier.tryParse(tag.getString("entity")));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        if(type != null)
            tag.putString("entity", Registry.ENTITY_TYPE.getId(type).toString());
        else
            tag.putString("entity", "null");
        return super.toTag(tag);
    }
}
