package modfest.lacrimis.block.entity;

import modfest.lacrimis.init.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class CombinerEntity extends SoulTankEntity {
    public EntityType<?> type;

    public CombinerEntity(BlockPos pos, BlockState state) {
        super(ModEntities.combiner, pos, state, 0, 2);
        type = null;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if(!tag.getString("entity").equals("null"))
            type = Registry.ENTITY_TYPE.get(Identifier.tryParse(tag.getString("entity")));
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        if(type != null)
            tag.putString("entity", Registry.ENTITY_TYPE.getId(type).toString());
        else
            tag.putString("entity", "null");
        return super.writeNbt(tag);
    }
}
