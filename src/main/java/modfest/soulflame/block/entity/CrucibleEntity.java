package modfest.soulflame.block.entity;

import java.util.Optional;

import modfest.soulflame.infusion.CrucibleRecipe;
import modfest.soulflame.infusion.InfusionInventory;
import modfest.soulflame.init.ModBlockEntityTypes;
import modfest.soulflame.init.ModInfusion;
import modfest.soulflame.init.ModNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class CrucibleEntity extends SoulTankEntity implements Tickable {
    private static final Box ITEM_BOX = new Box(0, 0.4, 0, 1, 1, 1);
    private static final int TEAR_CAPACITY = 1000;
    private static final int CRAFT_COOLDOWN = 15;

    public final InfusionInventory inventory;
    private int craftTime = 0;

    public CrucibleEntity() {
        super(ModBlockEntityTypes.crucible, TEAR_CAPACITY);
        this.inventory = new InfusionInventory(this, 1);
    }

    @Override
    public void tick() {
        if (getRelativeLevel() < 1 && this.world.getTime() % 10 == 0) {
            for (int dy = 1; dy < 5; dy++) {
                BlockPos obsidianPos = this.pos.up(dy);
                BlockState obsidianState = this.world.getBlockState(obsidianPos);
                if (obsidianState.getBlock() == Blocks.CRYING_OBSIDIAN) {
                    addTears(1);
                    break;
                }
            }
        }


        if (craftTime < CRAFT_COOLDOWN)
            craftTime++;

        if (craftTime >= CRAFT_COOLDOWN && !world.isClient) {
            for (ItemEntity entity : world.getEntities(ItemEntity.class, ITEM_BOX.offset(pos), null)) {
                inventory.setStack(0, ((ItemEntity) entity).getStack());

                Optional<CrucibleRecipe> optional = world.getServer()
                        .getRecipeManager()
                        .getFirstMatch(ModInfusion.CRUCIBLE_RECIPE, inventory, world);
                if (optional.isPresent()) {
                    CrucibleRecipe recipe = optional.get();
//                    if (recipe.matches(inventory, world)) {
                        // Craft item
                        ItemStack remainder = inventory.getStack(0);
                        remainder.decrement(1);
                        ((ItemEntity) entity).setStack(remainder);
                        ItemScatterer.spawn(world, pos.up(), new SimpleInventory(recipe.getOutput().copy()));
                        ModNetworking.sendCrucibleParticlesPacket(this, entity.getX(), entity.getY(), entity.getZ());
                        getTank().removeTears(recipe.getTears());
                        craftTime = 0;
                        break;
//                    }
                }
            }
        }
    }
}
