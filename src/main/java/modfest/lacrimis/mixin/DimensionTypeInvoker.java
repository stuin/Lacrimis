package modfest.lacrimis.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.dimension.DimensionType;

@Mixin(DimensionType.class)
public class DimensionTypeInvoker {
    @Accessor("OVERWORLD")
    public static DimensionType getOverworld() {
        return null;
    }
}
