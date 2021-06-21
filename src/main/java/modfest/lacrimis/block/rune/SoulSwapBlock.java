package modfest.lacrimis.block.rune;

import modfest.lacrimis.entity.SoulShellEntity;
import modfest.lacrimis.init.ModItems;
import modfest.lacrimis.util.DuctUtil;
import modfest.lacrimis.util.TaintPacket;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class SoulSwapBlock extends SoulExtractionBlock {
    public SoulSwapBlock() {
        super(50, 2);
    }

    @Override
    public boolean insert(BlockPos pos, World world, Object value) {
        Direction flipped = flipside(world, pos);
        int tier = testCage(world, pos, flipped, null);
        int added = 0;
        if(value instanceof PlayerContainer) {
            ItemStack stack = ((PlayerContainer) value).itemStack;
            value = ((PlayerContainer) value).entity;
            added = 100;

            if(value instanceof ServerPlayerEntity) {
                ((PlayerEntity) value).setHealth(1.0F);
                ((PlayerEntity) value).clearStatusEffects();
                ((PlayerEntity) value).incrementStat(Stats.USED.getOrCreateStat(ModItems.soulTotem));
                Criteria.USED_TOTEM.trigger((ServerPlayerEntity) value, stack);
            }
            stack.decrement(1);
        }

        //Locate destination soul shell
        if(value instanceof PlayerEntity && tier > 1) {
            List<SoulShellEntity> shells = world.getNonSpectatingEntities(
                    SoulShellEntity.class, getTargetBox(pos, flipped, tier));
            if(shells.size() > 0) {
                shells.get(0).swapWithPlayer(world, (PlayerEntity) value);

                //Spawn taint
                TaintPacket taint = new TaintPacket(actualCost(tier) + added);
                if(DuctUtil.locateSink(world, getDuct(world, pos), taint) == null)
                    taint.spawn(world, shells.get(0).getBlockPos());
                return true;
            }
        }
        return false;
    }

    public record PlayerContainer(Entity entity, ItemStack itemStack) {
    }
}
