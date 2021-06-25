package modfest.lacrimis.block.entity;

import modfest.lacrimis.crafting.CombinerScreenHandler;
import modfest.lacrimis.init.ModEntities;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class CombinerEntity extends SoulTankEntity implements ExtendedScreenHandlerFactory {
    public static final int SIZE = 2;
    public EntityType<?> type;

    public CombinerEntity() {
        super(ModEntities.combiner, 0, SIZE);
        type = null;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("lacrimis.gui.combiner");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new CombinerScreenHandler(syncId, inv, inventory, type, pos);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        String s = "null";
        if(type != null)
            s = Registry.ENTITY_TYPE.getId(type).toString();
        buf.writeBlockPos(pos);
        buf.writeString(s);
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
